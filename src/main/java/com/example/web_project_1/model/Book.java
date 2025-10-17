package com.example.web_project_1.model;

import lombok.Data;
import java.util.List;

@Data
public class Book {
    private Long id;
    private String readerId;
    private String title;
    // private List<Author> authors;
}