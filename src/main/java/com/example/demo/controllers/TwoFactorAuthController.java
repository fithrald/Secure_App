package com.example.demo.controllers;

import com.example.demo.models.Person;
import com.example.demo.repositories.PeopleRepository;
import com.example.demo.security.PersonDetails;
import com.example.demo.util.TwoFactorAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/2fa")

public class TwoFactorAuthController {

    private final PeopleRepository peopleRepository;

    public TwoFactorAuthController(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @GetMapping
    public String showTwoFactorAuthPage() {
        return "auth/2fa";
    }

    @PostMapping
    public String verifyTwoFactorCode(@RequestParam("code") int code, Authentication authentication) {
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        boolean isValid = TwoFactorAuthUtil.verifyCode(personDetails.getTwoFactorSecret(), code);

        if (isValid) {
            return "redirect:/post/info";
        }

        return "redirect:/2fa?error";
    }
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("code") int code,
                            @RequestParam("username") String username,
                            @RequestParam("qrCode") String qrCode,
                            Model model) {
        System.out.println("were here");
        Person person = peopleRepository.findByUsername(username).get();
        boolean isValid = TwoFactorAuthUtil.verifyCode(person.getTwoFactorSecret(), code);
        if (isValid) {
            return "redirect:/auth/login";
        } else {
            model.addAttribute("error", "Invalid code. Please try again.");
            model.addAttribute("qrCode", qrCode);
            model.addAttribute("person", person);
            return "auth/2fa-setup";
        }
    }
}
