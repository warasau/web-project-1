package com.example.web_project_1.service;

import com.example.web_project_1.dto.BookCreateRequest;
import com.example.web_project_1.dto.BookDetailDto;
import com.example.web_project_1.mapper.ApplicationMapper;
import com.example.web_project_1.model.Author;
import com.example.web_project_1.model.Book;
import com.example.web_project_1.repository.AuthorRepository;
import com.example.web_project_1.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ApplicationMapper mapper;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, ApplicationMapper mapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<BookDetailDto> getAllBooks() {
        return bookRepository.findAllWithDetails().stream()
                .map(mapper::toBookDetailDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BookDetailDto> getBookById(Long id) {
        return bookRepository.findByIdWithDetails(id)
                .map(mapper::toBookDetailDto);
    }

    @Transactional
    public BookDetailDto createBook(BookCreateRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());

        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            List<Author> authors = authorRepository.findAllById(request.getAuthorIds());
            if (authors.size() != request.getAuthorIds().size()) {
                throw new EntityNotFoundException("One or more authors not found");
            }
            book.setAuthors(new HashSet<>(authors));
        } else {
            book.setAuthors(Collections.emptySet());
        }
        Book savedBook = bookRepository.save(book);
        return mapper.toBookDetailDto(savedBook);
    }

    @Transactional
    public Optional<BookDetailDto> updateBook(Long id, BookCreateRequest request) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(request.getTitle());
                    if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
                        List<Author> authors = authorRepository.findAllById(request.getAuthorIds());
                        if (authors.size() != request.getAuthorIds().size()) {
                            throw new EntityNotFoundException("One or more authors not found");
                        }
                        existingBook.setAuthors(new HashSet<>(authors));
                    } else {
                        existingBook.setAuthors(Collections.emptySet());
                    }
                    Book updatedBook = bookRepository.save(existingBook);
                    return mapper.toBookDetailDto(updatedBook);
                });
    }

    @Transactional
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<BookDetailDto> updateExpirationDate(Long bookId, LocalDate newDate) {
        return bookRepository.findByIdWithDetails(bookId)
                .map(book -> {
                    if (book.getReader() == null) {
                        throw new IllegalStateException("Cannot set expiration date for a book that is not borrowed.");
                    }
                    book.setExpirationDate(newDate);
                    Book savedBook = bookRepository.save(book);
                    return mapper.toBookDetailDto(savedBook);
                });
    }
}