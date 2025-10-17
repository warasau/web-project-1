package com.example.web_project_1.model;

import lombok.Data;

@Data
public class Fine {
    private Long id;
    private Long readerId;
    private String description;
    private boolean paid;
}