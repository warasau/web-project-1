package com.example.web_project_1.dto;

import lombok.Data;

@Data
public class FineCreateRequest {
    private Long readerId;
    private String description;
}