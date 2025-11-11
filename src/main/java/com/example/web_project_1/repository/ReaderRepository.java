package com.example.web_project_1.repository;

import com.example.web_project_1.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
    @Query("SELECT r FROM Reader r LEFT JOIN FETCH r.books b LEFT JOIN FETCH b.authors LEFT JOIN FETCH r.fines WHERE r.id = :id")
    Optional<Reader> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT r FROM Reader r LEFT JOIN FETCH r.books b LEFT JOIN FETCH b.authors LEFT JOIN FETCH r.fines")
    List<Reader> findAllWithDetails();
}