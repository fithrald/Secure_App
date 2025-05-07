package com.example.demo.controllers;

import com.example.demo.models.Person;
import com.example.demo.repositories.PeopleRepository;
import com.example.demo.services.PersonDetailsService;
import com.example.demo.services.RegistrationService;
import com.example.demo.util.PersonValidator;
import com.example.demo.util.TwoFactorAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final long startTime = System.currentTimeMillis();

    private final AtomicLong loginCount = new AtomicLong();

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final PeopleRepository peopleRepository;
    private final  PasswordEncoder passwordEncoder;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator, PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, PersonDetailsService personDetailsService) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("/login")
    public String loginPage() throws InterruptedException {
        long count = loginCount.incrementAndGet();
        long elapsedMs = System.currentTimeMillis() - startTime;
        long elapsedSec = TimeUnit.MILLISECONDS.toSeconds(elapsedMs);
        long minutes = elapsedSec / 60;
        long seconds = elapsedSec % 60;
        System.out.printf(
                "Сторінку входу було запрошено %d разів; час роботи додатку: %d хв %d сек%n",
                count, minutes, seconds
        );
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person, Model model) {
        String secretKey = TwoFactorAuthUtil.generateSecretKey();
        person.setTwoFactorSecret(secretKey);
        person.setTwoFactorEnabled(true);
        registrationService.register(person);
        String otpAuthURL = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                "MyApp",
                person.getUsername(),
                secretKey,
                "MyApp"
        );
        String qrCode = TwoFactorAuthUtil.generateQRCode(otpAuthURL, 250, 250);
        model.addAttribute("qrCode", qrCode);
        model.addAttribute("person", person);
        return "auth/2fa-setup";
    }
}
