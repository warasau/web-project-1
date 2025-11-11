package com.example.web_project_1.controller;

import com.example.web_project_1.dto.BookDetailDto;
import com.example.web_project_1.dto.FineDetailDto;
import com.example.web_project_1.dto.ReaderDetailDto;
import com.example.web_project_1.service.LibraryManagementService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryManagementService libraryService;

    @Autowired
    public LibraryController(LibraryManagementService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody Map<String, Long> payload) {
        Long bookId = payload.get("bookId");
        Long readerId = payload.get("readerId");
        if (bookId == null || readerId == null) {
            return ResponseEntity.badRequest().body("Missing 'bookId' or 'readerId' in request body.");
        }
        try {
            BookDetailDto borrowedBook = libraryService.borrowBook(bookId, readerId);
            return ResponseEntity.ok(borrowedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody Map<String, Long> payload) {
        Long bookId = payload.get("bookId");
        if (bookId == null) {
            return ResponseEntity.badRequest().body("Missing 'bookId' in request body.");
        }
        try {
            BookDetailDto returnedBook = libraryService.returnBook(bookId);
            return ResponseEntity.ok(returnedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/pay-fine")
    public ResponseEntity<?> payFine(@RequestBody Map<String, Long> payload) {
        Long fineId = payload.get("fineId");
        if (fineId == null) {
            return ResponseEntity.badRequest().body("Missing 'fineId' in request body.");
        }
        try {
            ReaderDetailDto updatedReader = libraryService.payFine(fineId);
            return ResponseEntity.ok(updatedReader);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/check-ban")
    public ResponseEntity<?> checkBanForReader(@RequestBody Map<String, Long> payload) {
        Long readerId = payload.get("readerId");
        if (readerId == null) {
            return ResponseEntity.badRequest().body("Missing 'readerId' in request body.");
        }
        try {
            ReaderDetailDto updatedReader = libraryService.checkAndBanReader(readerId);
            return ResponseEntity.ok(updatedReader);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/process-overdue")
    public ResponseEntity<List<FineDetailDto>> processOverdueBooks() {
        // This method now internally calls the single-user checkAndBanReader
        List<FineDetailDto> fines = libraryService.processOverdueBooks();
        return ResponseEntity.ok(fines);
    }
}