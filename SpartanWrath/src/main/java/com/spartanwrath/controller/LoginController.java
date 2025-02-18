package com.spartanwrath.controller;

import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.UserAlreadyRegister;
import com.spartanwrath.model.User;
import com.spartanwrath.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/register")
    public String registerUser(User user) throws UserAlreadyRegister, InvalidUser {
        userService.add(user);
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/admin";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "login";
    }

    @GetMapping("/private")
    public String privatePage() {
        return "private";
    }

}
