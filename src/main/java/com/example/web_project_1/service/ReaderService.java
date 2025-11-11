package com.example.web_project_1.service;

import com.example.web_project_1.dto.ReaderCreateRequest;
import com.example.web_project_1.dto.ReaderDetailDto;
import com.example.web_project_1.mapper.ApplicationMapper;
import com.example.web_project_1.model.Reader;
import com.example.web_project_1.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final ApplicationMapper mapper;

    @Autowired
    public ReaderService(ReaderRepository readerRepository, ApplicationMapper mapper) {
        this.readerRepository = readerRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Optional<ReaderDetailDto> getReaderById(Long id) {
        return readerRepository.findByIdWithDetails(id)
                .map(mapper::toReaderDetailDto);
    }

    @Transactional(readOnly = true)
    public List<ReaderDetailDto> getAllReaders() {
        return readerRepository.findAllWithDetails().stream()
                .map(mapper::toReaderDetailDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReaderDetailDto createReader(ReaderCreateRequest request) {
        Reader reader = new Reader();
        reader.setFirstName(request.getFirstName());
        reader.setLastName(request.getLastName());
        reader.setEmail(request.getEmail());
        Reader savedReader = readerRepository.save(reader);
        return mapper.toReaderDetailDto(savedReader);
    }

    @Transactional
    public Optional<ReaderDetailDto> updateReader(Long id, ReaderCreateRequest request) {
        return readerRepository.findById(id)
                .map(existingReader -> {
                    existingReader.setFirstName(request.getFirstName());
                    existingReader.setLastName(request.getLastName());
                    existingReader.setEmail(request.getEmail());
                    Reader updatedReader = readerRepository.save(existingReader);
                    return mapper.toReaderDetailDto(updatedReader);
                });
    }

    @Transactional
    public boolean deleteReader(Long id) {
        if (readerRepository.existsById(id)) {
            readerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}