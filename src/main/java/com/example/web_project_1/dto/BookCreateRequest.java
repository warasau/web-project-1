package com.example.web_project_1.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookCreateRequest {
    private String title;
    private List<Long> authorIds;
}