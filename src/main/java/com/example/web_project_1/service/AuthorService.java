package com.example.web_project_1.service;

import com.example.web_project_1.dto.AuthorCreateRequest;
import com.example.web_project_1.dto.AuthorDetailDto;
import com.example.web_project_1.mapper.ApplicationMapper;
import com.example.web_project_1.model.Author;
import com.example.web_project_1.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ApplicationMapper mapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, ApplicationMapper mapper) {
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<AuthorDetailDto> getAllAuthors() {
        return authorRepository.findAllWithDetails().stream()
                .map(mapper::toAuthorDetailDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AuthorDetailDto> getAuthorById(Long id) {
        return authorRepository.findByIdWithDetails(id)
                .map(mapper::toAuthorDetailDto);
    }

    @Transactional
    public AuthorDetailDto createAuthor(AuthorCreateRequest request) {
        Author author = new Author();
        author.setFirstName(request.getFirstName());
        author.setLastName(request.getLastName());
        Author savedAuthor = authorRepository.save(author);
        return mapper.toAuthorDetailDto(savedAuthor);
    }

    @Transactional
    public Optional<AuthorDetailDto> updateAuthor(Long id, AuthorCreateRequest request) {
        return authorRepository.findById(id)
                .map(existingAuthor -> {
                    existingAuthor.setFirstName(request.getFirstName());
                    existingAuthor.setLastName(request.getLastName());
                    Author updatedAuthor = authorRepository.save(existingAuthor);
                    return mapper.toAuthorDetailDto(updatedAuthor);
                });
    }

    @Transactional
    public boolean deleteAuthor(Long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}