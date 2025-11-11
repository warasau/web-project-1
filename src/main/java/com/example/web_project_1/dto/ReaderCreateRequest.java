package com.example.web_project_1.dto;

import lombok.Data;

@Data
public class ReaderCreateRequest {
    private String firstName;
    private String lastName;
    private String email;
}