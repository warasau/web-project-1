package com.example.web_project_1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class GreetingController {

    @GetMapping("/")
    public String index(Model model) {
        String content = """
                     <h1>Welcome to Lab 1</h1>
                     <p>This is a simple web application built using the Spring Boot framework.</p>
                     <p>It demonstrates the basics of handling web requests using controllers.</p>
                     """;
        model.addAttribute("title", "Lab 1: Introduction to Spring Boot");
        model.addAttribute("content", content);
        return "layout";
    }

    @GetMapping("/say/{text}")
    public String saySomething(@PathVariable String text, Model model) {
        String content = "<h1>You said:</h1><p style='font-size: 24px; color: #a9f5a5;'>%s</p>".formatted(text);
        model.addAttribute("title", "Your message");
        model.addAttribute("content", content);
        return "layout";
    }
}


