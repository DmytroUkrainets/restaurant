package edu.ukma.restaurant.service;

import edu.ukma.restaurant.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    ReservationDto create(ReservationDto dto);
    ReservationDto findById(Long id);
    List<ReservationDto> findAll();
    ReservationDto update(Long id, ReservationDto dto);
    void delete(Long id);
    void confirm(Long id);
    void cancel(Long id);
    void seat(Long id);
}