package com.example.web_project_1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

    @GetMapping("/")
    public String index() {
        String title = "Lab 1: Introduction to Spring Boot";
        String content = """
                         <h1>Welcome to Lab 1</h1>
                         <p>This is a simple web application built using the Spring Boot framework.</p>
                         <p>It demonstrates the basics of handling web requests using controllers.</p>
                         """;
        return getStyledHtml(title, content);
    }

    @GetMapping("/say/{text}")
    public String saySomething(@PathVariable String text) {
        String title = "Your message";
        String content = "<h1>You said:</h1><p style='font-size: 24px; color: #a9f5a5;'>%s</p>".formatted(text);
        return getStyledHtml(title, content);
    }

    private String getStyledHtml(String title, String content) {
        return """
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>%s</title>
                    <style>
                        body {
                            background-color: #2b2b2b;
                            color: #eeeeee;
                            font-family: Arial, sans-serif;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            margin: 0;
                            text-align: center;
                        }
                        div {
                            border: 1px solid #444;
                            padding: 40px;
                            border-radius: 8px;
                            background-color: #3c3f41;
                        }
                        h1 {
                           color: #ffffff;
                        }
                        p {
                            font-size: 18px;
                        }
                    </style>
                </head>
                <body>
                    <div>
                        %s
                    </div>
                </body>
                </html>
                """.formatted(title, content); // .formatted() подставляет аргументы в %s
    }
}