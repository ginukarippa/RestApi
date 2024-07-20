package com.mmu.ggk.controller;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mmu.ggk.model.User;
import com.mmu.ggk.service.UserService;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);         
        User user = userService.findByUsername(username);
        Long userId = user.getId();
        model.addAttribute("users", userService.findAll());   
        model.addAttribute("userId", userId);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
