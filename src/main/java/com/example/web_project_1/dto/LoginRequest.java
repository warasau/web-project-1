package com.example.web_project_1.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}