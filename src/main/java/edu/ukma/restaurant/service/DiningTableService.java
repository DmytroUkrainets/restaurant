package edu.ukma.restaurant.service;

import edu.ukma.restaurant.dto.DiningTableDto;

import java.time.LocalDateTime;
import java.util.List;

public interface DiningTableService {
    DiningTableDto create(DiningTableDto dto);
    DiningTableDto update(Long id, DiningTableDto dto);
    void delete(Long id);
    DiningTableDto findById(Long id);
    List<DiningTableDto> findAll();
    List<DiningTableDto> findAvailable(LocalDateTime start, LocalDateTime end, int partySize);
}