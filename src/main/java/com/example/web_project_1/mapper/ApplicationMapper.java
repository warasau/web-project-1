package com.example.web_project_1.mapper;

import com.example.web_project_1.dto.*;
import com.example.web_project_1.model.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {

    // --- Мапперы для Reader ---
    public ReaderDetailDto toReaderDetailDto(Reader reader) {
        ReaderDetailDto dto = new ReaderDetailDto();
        dto.setId(reader.getId());
        dto.setFirstName(reader.getFirstName());
        dto.setLastName(reader.getLastName());
        dto.setEmail(reader.getEmail());
        dto.setBanned(reader.isBanned());
        dto.setBooks(reader.getBooks().stream().map(this::toBookSummaryDto).collect(Collectors.toSet()));
        dto.setFines(reader.getFines().stream().map(this::toFineDto).collect(Collectors.toList()));
        return dto;
    }

    public BookSummaryDto toBookSummaryDto(Book book) {
        BookSummaryDto dto = new BookSummaryDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setExpirationDate(book.getExpirationDate());
        dto.setAuthors(book.getAuthors().stream().map(this::toAuthorDto).collect(Collectors.toSet()));
        return dto;
    }

    // --- Мапперы для Book ---
    public BookDetailDto toBookDetailDto(Book book) {
        BookDetailDto dto = new BookDetailDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setExpirationDate(book.getExpirationDate());
        dto.setReaderId(book.getReader() != null ? book.getReader().getId() : null);
        dto.setAuthors(book.getAuthors().stream().map(this::toAuthorDto).collect(Collectors.toSet()));
        return dto;
    }

    // --- Мапперы для Author ---
    public AuthorDetailDto toAuthorDetailDto(Author author) {
        AuthorDetailDto dto = new AuthorDetailDto();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setBookIds(author.getBooks().stream().map(Book::getId).collect(Collectors.toList()));
        return dto;
    }

    // --- Вспомогательные мапперы ---
    public FineDto toFineDto(Fine fine) {
        FineDto dto = new FineDto();
        dto.setId(fine.getId());
        dto.setDescription(fine.getDescription());
        dto.setPaid(fine.isPaid());
        return dto;
    }

    public FineDetailDto toFineDetailDto(Fine fine) {
        FineDetailDto dto = new FineDetailDto();
        dto.setId(fine.getId());
        dto.setDescription(fine.getDescription());
        dto.setPaid(fine.isPaid());
        dto.setReaderId(fine.getReader() != null ? fine.getReader().getId() : null);
        return dto;
    }

    public AuthorDto toAuthorDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        return dto;
    }
}