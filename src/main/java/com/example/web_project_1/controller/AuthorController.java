package com.example.web_project_1.controller;

import com.example.web_project_1.dto.AuthorCreateRequest;
import com.example.web_project_1.dto.AuthorDetailDto;
import com.example.web_project_1.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDetailDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDetailDto> getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AuthorDetailDto> createAuthor(@RequestBody AuthorCreateRequest request) {
        AuthorDetailDto createdAuthor = authorService.createAuthor(request);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDetailDto> updateAuthor(@PathVariable Long id, @RequestBody AuthorCreateRequest request) {
        return authorService.updateAuthor(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        if (authorService.deleteAuthor(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}