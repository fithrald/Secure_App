package com.example.demo.controllers;

import com.example.demo.security.PersonDetails;
import com.example.demo.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HelloController {
    private final AdminService adminService;
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    public HelloController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public String sayHello(Principal principal) {
        String user = principal != null ? principal.getName() : "anonymous";
        log.info(">>> GET /hello invoked by user: {}", user);
        return "hello";
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());

        return "hello";
    }

    @GetMapping("/admin")
    public String adminPage() {
        adminService.doAdminStuff();
        return "admin";
    }
}
