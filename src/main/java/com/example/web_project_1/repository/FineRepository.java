package com.example.web_project_1.repository;

import com.example.web_project_1.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    @Query("SELECT f FROM Fine f JOIN FETCH f.reader WHERE f.id = :id")
    Optional<Fine> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT f FROM Fine f JOIN FETCH f.reader")
    List<Fine> findAllWithDetails();
}