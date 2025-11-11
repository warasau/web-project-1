package com.example.web_project_1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "fines")
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid = false;

    @ManyToOne(fetch = FetchType.LAZY) // Связь со читателем. Много штрафов -> один читатель
    @JoinColumn(name = "reader_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Reader reader;
}