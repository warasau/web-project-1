package com.example.web_project_1.controller;

import com.example.web_project_1.model.Reader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {
    private final Map<Long, Reader> readers = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public ReaderController() {
        long initialId = counter.incrementAndGet();
        Reader initialReader = new Reader();
        initialReader.setId(initialId);
        initialReader.setFirstName("Иван");
        initialReader.setLastName("Петров");
        initialReader.setEmail("i.petrov@rogaico.org");
        readers.put(initialId, initialReader);
    }


    // ЭНДПОИНТЫ
    @PostMapping
    public Reader createReader(@RequestBody Reader reader) {
        long newId = counter.incrementAndGet();
        reader.setId(newId);
        readers.put(newId, reader);
        System.out.println("Создан читатель: " + reader);
        return reader;
    }

    @GetMapping
    public List<Reader> getAllReaders() {
        System.out.println("Запрошен список всех читателей. Всего: " + readers.size());
        return new ArrayList<>(readers.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable Long id) {
        Reader reader = readers.get(id);
        if (reader != null) {
            System.out.println("Найден читатель по ID " + id + ": " + reader);
            return ResponseEntity.ok(reader);
        } else {
            System.out.println("Читатель с ID " + id + " не найден.");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable Long id, @RequestBody Reader readerDetails) {
        if (readers.containsKey(id)) {
            readerDetails.setId(id);
            readers.put(id, readerDetails);
            System.out.println("Обновлен читатель с ID " + id + ": " + readerDetails);
            return ResponseEntity.ok(readerDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        if (readers.remove(id) != null) {
            System.out.println("Удален читатель с ID " + id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}