package com.example.web_project_1.dto;
import lombok.Data;
import java.util.List;

@Data
public class AuthorDetailDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Long> bookIds; // <-- Только ID книг
}