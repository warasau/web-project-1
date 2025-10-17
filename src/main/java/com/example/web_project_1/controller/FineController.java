package com.example.web_project_1.controller;

import com.example.web_project_1.model.Fine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/fines")
public class FineController {
    private final Map<Long, Fine> fines = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public FineController() {
        long initialId = counter.incrementAndGet();
        Fine initialFine = new Fine();
        initialFine.setId(initialId);
        initialFine.setReaderId(1L);
        initialFine.setDescription("Просрочка возврата книги");
        initialFine.setPaid(false);
        fines.put(initialId, initialFine);
    }


    // ЭНДПОИНТЫ
    @PostMapping
    public Fine createFine(@RequestBody Fine fine) {
        long newId = counter.incrementAndGet();
        fine.setId(newId);
        fines.put(newId, fine);
        System.out.println("Создан штраф: " + fine);
        return fine;
    }

    @GetMapping
    public List<Fine> getAllFines() {
        System.out.println("Запрошен список всех штрафов. Всего: " + fines.size());
        return new ArrayList<>(fines.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getFineById(@PathVariable Long id) {
        Fine fine = fines.get(id);
        if (fine != null) {
            System.out.println("Найден штраф по ID " + id + ": " + fine);
            return ResponseEntity.ok(fine);
        } else {
            System.out.println("Штраф с ID " + id + " не найден.");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fine> updateFine(@PathVariable Long id, @RequestBody Fine fineDetails) {
        if (fines.containsKey(id)) {
            fineDetails.setId(id);
            fines.put(id, fineDetails);
            System.out.println("Обновлен штраф с ID " + id + ": " + fineDetails);
            return ResponseEntity.ok(fineDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        if (fines.remove(id) != null) {
            System.out.println("Удален штраф с ID " + id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<Fine> markAsPaid(@PathVariable Long id) {
        Fine fine = fines.get(id);
        if (fine != null) {
            fine.setPaid(true);
            System.out.println("Штраф с ID " + id + " отмечен как оплаченный");
            return ResponseEntity.ok(fine);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}