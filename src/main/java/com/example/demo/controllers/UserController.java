package com.example.demo.controllers;

import com.example.demo.dtos.PasswordForm;

import com.example.demo.services.PersonDetailsService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/change-password")
    public String showChangePassword(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "user/changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 PasswordForm passwordForm,
                                 Model model) {
        boolean success = userService.changePassword(userDetails.getUsername(), passwordForm.getNewPassword());
        if (!success) {
            model.addAttribute("error", "Ошибка при смене пароля!");
            return "user/changePassword";
        }
        return "redirect:/post/info";
    }
}
