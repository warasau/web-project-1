package com.example.web_project_1.controller;

import com.example.web_project_1.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/api/books")
public class BookController {
    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public BookController() {
        long initialId = counter.incrementAndGet();
        Book initialBook = new Book();
        initialBook.setId(initialId);
        initialBook.setTitle("Изучаем Java");
        books.put(initialId, initialBook);
    }


    // ЭНДПОИНТЫ
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        long newId = counter.incrementAndGet();
        book.setId(newId);
        books.put(newId, book);
        System.out.println("Создана книга: " + book);
        return book;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        System.out.println("Запрошен список всех книг. Всего: " + books.size());
        return new ArrayList<>(books.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = books.get(id);
        if (book != null) {
            System.out.println("Найдена книга по ID " + id + ": " + book);
            return ResponseEntity.ok(book);
        } else {
            System.out.println("Книга с ID " + id + " не найдена.");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        if (books.containsKey(id)) {
            bookDetails.setId(id);
            books.put(id, bookDetails);
            System.out.println("Обновлена книга с ID " + id + ": " + bookDetails);
            return ResponseEntity.ok(bookDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (books.remove(id) != null) {
            System.out.println("Удалена книга с ID " + id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}