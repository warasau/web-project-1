package com.example.web_project_1.dto;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class ReaderDetailDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isBanned;
    private Set<BookSummaryDto> books;
    private List<FineDto> fines;
}