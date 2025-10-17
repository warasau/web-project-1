package com.example.web_project_1.model;

import lombok.Data;
import java.util.List;

@Data
public class Reader {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    // private List<Book> books;
    // private List<Fine> fines;
}