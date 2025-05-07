package com.example.demo.config;

import io.github.bucket4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.*;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final long CAPACITY = 100;
    private static final Duration WINDOW = Duration.ofMinutes(1);
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private Bucket newBucket() {
        Refill refill   = Refill.greedy(CAPACITY, WINDOW);
        Bandwidth limit = Bandwidth.classic(CAPACITY, refill);
        return Bucket.builder().addLimit(limit).build();
    }
    private String resolveKey(HttpServletRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "USER_" + auth.getName();
        }
        return "IP_" + req.getRemoteAddr();
    }
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        String key   = resolveKey(req);
        Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket());
        if (bucket.tryConsume(1)) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(429);
            res.getWriter().write("Too many requests. Try again later.");
        }
    }
}
