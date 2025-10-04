package com.example.web_project_1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

    @GetMapping("/")
    public String hello() {
        return "Hello, that is the root!";
    }

    @GetMapping("/say/{text}")
    public String saySomething(@PathVariable String text) {
        return "You say: " + text;
    }
}