package com.example.web_project_1.repository;

import com.example.web_project_1.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT DISTINCT b FROM Book b " +
            "JOIN FETCH b.reader r " +
            "WHERE b.reader IS NOT NULL AND b.expirationDate < :currentDate")
    List<Book> findOverdueBooks(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.reader LEFT JOIN FETCH b.authors WHERE b.id = :id")
    Optional<Book> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.reader LEFT JOIN FETCH b.authors")
    List<Book> findAllWithDetails();
}