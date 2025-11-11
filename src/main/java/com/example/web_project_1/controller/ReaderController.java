package com.example.web_project_1.controller;

import com.example.web_project_1.dto.ReaderCreateRequest;
import com.example.web_project_1.dto.ReaderDetailDto;
import com.example.web_project_1.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public List<ReaderDetailDto> getAllReaders() {
        return readerService.getAllReaders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDetailDto> getReaderById(@PathVariable Long id) {
        return readerService.getReaderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReaderDetailDto> createReader(@RequestBody ReaderCreateRequest request) {
        ReaderDetailDto createdReader = readerService.createReader(request);
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderDetailDto> updateReader(@PathVariable Long id, @RequestBody ReaderCreateRequest request) {
        return readerService.updateReader(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        if (readerService.deleteReader(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}