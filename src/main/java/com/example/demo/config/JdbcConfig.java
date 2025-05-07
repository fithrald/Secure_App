package com.example.demo.config;

import com.example.demo.models.Post;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class JdbcConfig {

    @Bean
    public RowMapper<Post> postRowMapper() {
        return new BeanPropertyRowMapper<>(Post.class);
    }
}
