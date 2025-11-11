package com.example.web_project_1.repository;

import com.example.web_project_1.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :id")
    Optional<Author> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books")
    List<Author> findAllWithDetails();
}