package com.example.web_project_1.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class BookDetailDto {
    private Long id;
    private String title;
    private LocalDate expirationDate;
    private Long readerId; // <-- Только ID читателя
    private Set<AuthorDto> authors;
}