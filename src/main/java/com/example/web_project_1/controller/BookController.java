package com.example.web_project_1.controller;

import com.example.web_project_1.dto.BookCreateRequest;
import com.example.web_project_1.dto.BookDetailDto;
import com.example.web_project_1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDetailDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDetailDto> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookDetailDto> createBook(@RequestBody BookCreateRequest request) {
        BookDetailDto createdBook = bookService.createBook(request);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDetailDto> updateBook(@PathVariable Long id, @RequestBody BookCreateRequest request) {
        return bookService.updateBook(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/expiration-date/{id}")
    public ResponseEntity<?> updateExpirationDate(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String dateString = payload.get("expirationDate");
        if (dateString == null) {
            return ResponseEntity.badRequest().body("Missing 'expirationDate' in request body.");
        }
        try {
            LocalDate newDate = LocalDate.parse(dateString);
            return bookService.updateExpirationDate(id, newDate)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use 'YYYY-MM-DD'.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}