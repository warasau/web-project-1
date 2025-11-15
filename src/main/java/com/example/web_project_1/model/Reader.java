package com.example.web_project_1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "readers")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(name = "is_banned", nullable = false)
    private boolean isBanned = false;

    @OneToMany(mappedBy = "reader")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Book> books = new HashSet<>();

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true) // Один ко многим: для нескольких штрафов - readerId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Fine> fines = new ArrayList<>();
}