package com.example.uni_manage.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("username", request.getRemoteUser());
        model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
        model.addAttribute("isTeacher", request.isUserInRole("TEACHER"));
        model.addAttribute("isStudent", request.isUserInRole("STUDENT"));
        return "home";
    }
}
