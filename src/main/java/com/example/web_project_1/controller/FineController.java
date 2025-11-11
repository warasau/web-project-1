package com.example.web_project_1.controller;

import com.example.web_project_1.dto.FineCreateRequest;
import com.example.web_project_1.dto.FineDetailDto;
import com.example.web_project_1.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    @Autowired
    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping
    public ResponseEntity<FineDetailDto> createFine(@RequestBody FineCreateRequest request) {
        FineDetailDto createdFine = fineService.createFine(request);
        return new ResponseEntity<>(createdFine, HttpStatus.CREATED);
    }

    @GetMapping
    public List<FineDetailDto> getAllFines() {
        return fineService.getAllFines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineDetailDto> getFineById(@PathVariable Long id) {
        return fineService.getFineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        if (fineService.deleteFine(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}