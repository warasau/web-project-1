package com.example.web_project_1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY - загружать читателя только когда он явно нужен
    @JoinColumn(name = "reader_id")
    @ToString.Exclude // Исключаем из toString, чтобы избежать рекурсии
    @EqualsAndHashCode.Exclude // Исключаем для корректной работы equals/hashCode
    private Reader reader;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })  // Связь Многие-ко-Многим с Автором
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Author> authors = new HashSet<>();
}