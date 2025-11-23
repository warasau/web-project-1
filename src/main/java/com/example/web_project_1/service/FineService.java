package com.example.web_project_1.service;

import com.example.web_project_1.dto.FineCreateRequest;
import com.example.web_project_1.dto.FineDetailDto;
import com.example.web_project_1.mapper.ApplicationMapper;
import com.example.web_project_1.model.Fine;
import com.example.web_project_1.model.Reader;
import com.example.web_project_1.repository.FineRepository;
import com.example.web_project_1.repository.ReaderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FineService {

    private final FineRepository fineRepository;
    private final ReaderRepository readerRepository;
    private final ApplicationMapper mapper;

    @Autowired
    public FineService(FineRepository fineRepository, ReaderRepository readerRepository, ApplicationMapper mapper) {
        this.fineRepository = fineRepository;
        this.readerRepository = readerRepository;
        this.mapper = mapper;
    }

    @Transactional
    public FineDetailDto createFine(FineCreateRequest request) {
        Reader reader = readerRepository.findById(request.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader with id " + request.getReaderId() + " not found."));

        Fine fine = new Fine();
        fine.setDescription(request.getDescription());
        fine.setReader(reader);
        fine.setPaid(false);

        Fine savedFine = fineRepository.save(fine);
        return mapper.toFineDetailDto(savedFine);
    }

    @Transactional(readOnly = true)
    public List<FineDetailDto> getAllFines() {
        return fineRepository.findAllWithDetails().stream()
                .map(mapper::toFineDetailDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<FineDetailDto> getFineById(Long id) {
        return fineRepository.findByIdWithDetails(id)
                .map(mapper::toFineDetailDto);
    }

    @Transactional
    public Optional<FineDetailDto> updateFine(Long fineId, FineCreateRequest request) {
        return fineRepository.findById(fineId)
                .map(existingFine -> {
                    if (request.getDescription() != null) {
                        existingFine.setDescription(request.getDescription());
                    }
                    if (request.getReaderId() != null) {
                        Reader newReader = readerRepository.findById(request.getReaderId())
                                .orElseThrow(() -> new EntityNotFoundException("Reader with id " + request.getReaderId() + " not found."));
                        existingFine.setReader(newReader);
                    }
                    Fine updatedFine = fineRepository.save(existingFine);
                    return mapper.toFineDetailDto(updatedFine);
                });
    }


    @Transactional
    public boolean deleteFine(Long id) {
        if (fineRepository.existsById(id)) {
            fineRepository.deleteById(id);
            return true;
        }
        return false;
    }
}