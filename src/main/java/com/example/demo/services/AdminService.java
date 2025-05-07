package com.example.demo.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public void doAdminStuff() {
        System.out.println("Only admin here");
    }
}
