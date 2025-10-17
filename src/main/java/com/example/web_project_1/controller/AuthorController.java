package com.example.web_project_1.controller;

import com.example.web_project_1.model.Author;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final Map<Long, Author> authors = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public AuthorController() {
        long initialId = counter.incrementAndGet();
        Author initialAuthor = new Author();
        initialAuthor.setId(initialId);
        initialAuthor.setFirstName("Лев");
        initialAuthor.setLastName("Толстой");
        authors.put(initialId, initialAuthor);
    }


    // ЭНДПОИНТЫ
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        long newId = counter.incrementAndGet();
        author.setId(newId);
        authors.put(newId, author);
        System.out.println("Создан автор: " + author);
        return author;
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        System.out.println("Запрошен список всех авторов. Всего: " + authors.size());
        return new ArrayList<>(authors.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Author author = authors.get(id);
        if (author != null) {
            System.out.println("Найден автор по ID " + id + ": " + author);
            return ResponseEntity.ok(author);
        } else {
            System.out.println("Автор с ID " + id + " не найден.");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        if (authors.containsKey(id)) {
            authorDetails.setId(id);
            authors.put(id, authorDetails);
            System.out.println("Обновлен автор с ID " + id + ": " + authorDetails);
            return ResponseEntity.ok(authorDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        if (authors.remove(id) != null) {
            System.out.println("Удален автор с ID " + id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}