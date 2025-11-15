package com.example.web_project_1.service;

import com.example.web_project_1.dto.BookDetailDto;
import com.example.web_project_1.dto.FineDetailDto;
import com.example.web_project_1.dto.ReaderDetailDto;
import com.example.web_project_1.mapper.ApplicationMapper;
import com.example.web_project_1.model.Book;
import com.example.web_project_1.model.Fine;
import com.example.web_project_1.model.Reader;
import com.example.web_project_1.repository.BookRepository;
import com.example.web_project_1.repository.FineRepository;
import com.example.web_project_1.repository.ReaderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryManagementService {
    private static final long BORROWING_PERIOD_WEEKS = 2;

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final FineRepository fineRepository;
    private final ApplicationMapper mapper;

    @Autowired
    public LibraryManagementService(BookRepository bookRepository, ReaderRepository readerRepository, FineRepository fineRepository, ApplicationMapper mapper) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.fineRepository = fineRepository;
        this.mapper = mapper;
    }

    /** 1. Выдать книгу читателю */
    @Transactional
    public BookDetailDto borrowBook(Long bookId, Long readerId) {
        Book book = bookRepository.findByIdWithDetails(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        if (book.getReader() != null) {
            throw new IllegalStateException("Book is already borrowed by another reader.");
        }
        if (reader.isBanned()) {
            throw new IllegalStateException("Reader is banned and cannot borrow books.");
        }

        book.setReader(reader);
        book.setExpirationDate(LocalDate.now().plusWeeks(BORROWING_PERIOD_WEEKS));
        Book savedBook = bookRepository.save(book);
        return mapper.toBookDetailDto(savedBook);
    }

    /** 2. Вернуть книгу */
    @Transactional
    public BookDetailDto returnBook(Long bookId) {
        Book book = bookRepository.findByIdWithDetails(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (book.getReader() == null) {
            throw new IllegalStateException("Book is not currently borrowed.");
        }

        book.setReader(null);
        book.setExpirationDate(null);
        Book savedBook = bookRepository.save(book);
        return mapper.toBookDetailDto(savedBook);
    }

    /** 3. Оплатить штраф и проверить разбан */
    @Transactional
    public ReaderDetailDto payFine(Long fineId) {
        Fine fine = fineRepository.findByIdWithDetails(fineId)
                .orElseThrow(() -> new EntityNotFoundException("Fine not found"));

        if (fine.isPaid()) {
            throw new IllegalStateException("Fine with id " + fineId + " has already been paid.");
        }

        fine.setPaid(true);
        fineRepository.save(fine);

        Reader reader = fine.getReader();
        long unpaidFinesCount = reader.getFines().stream().filter(f -> !f.isPaid()).count();

        if (reader.isBanned() && unpaidFinesCount < 3) {
            reader.setBanned(false);
            Reader savedReader = readerRepository.save(reader);
            return mapper.toReaderDetailDto(savedReader);
        }
        return mapper.toReaderDetailDto(reader);
    }

    /** 4. Проверка одного пользователя */
    @Transactional
    public ReaderDetailDto checkAndBanReader(Long readerId) {
        Reader reader = readerRepository.findByIdWithDetails(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        if (reader.isBanned()) { return mapper.toReaderDetailDto(reader); }

        long unpaidFinesCount = reader.getFines().stream().filter(f -> !f.isPaid()).count();

        if (unpaidFinesCount >= 3) {
            reader.setBanned(true);
            Reader savedReader = readerRepository.save(reader);
            return mapper.toReaderDetailDto(savedReader);
        }

        return mapper.toReaderDetailDto(reader);
    }

    /** 5. Поиск просроченных книг */
    @Transactional
    public List<FineDetailDto> processOverdueBooks() {
        List<Book> overdueBooks = bookRepository.findOverdueBooks(LocalDate.now());
        List<Fine> createdFines = new ArrayList<>();

        for (Book book : overdueBooks) {
            Reader reader = book.getReader();
            boolean alreadyFined = reader.getFines().stream()
                    .anyMatch(fine -> !fine.isPaid() && fine.getDescription().contains("'" + book.getTitle() + "'"));

            if (!alreadyFined) {
                Fine fine = new Fine();
                fine.setReader(reader);
                fine.setDescription("Overdue fine for book: '" + book.getTitle() + "'");
                fine.setPaid(false);

                createdFines.add(fineRepository.save(fine));
                checkAndBanReader(reader.getId());
            }
        }
        return createdFines.stream()
                .map(mapper::toFineDetailDto)
                .collect(Collectors.toList());
    }
}