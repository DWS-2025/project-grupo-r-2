package com.spartanwrath.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/AboutUs")
    public String AboutUs(){
        return "aboutUs";
    }
    @GetMapping("/error")
    public String error() {
        return "error";
    }

}
