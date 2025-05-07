package com.example.demo.services;

import com.example.demo.models.Person;
import com.example.demo.repositories.PeopleRepository;
import com.example.demo.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final PeopleRepository peopleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Person getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson();
    }
    public boolean changePassword(String username, String newPassword) {
        Person user = peopleRepository.findByUsername(username).get();
        if (user == null) {
            return false;
        }
        System.out.println("пароль для користувача "+username+ "було змінено на "+ newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        peopleRepository.save(user);
        return true;
    }
}
