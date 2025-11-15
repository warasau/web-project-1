package com.example.web_project_1.dto;
import lombok.Data;

@Data
public class FineDetailDto {
    private Long id;
    private String description;
    private boolean isPaid;
    private Long readerId;
}