package edu.ukma.restaurant.service.impl;

import edu.ukma.restaurant.domain.DiningTable;
import edu.ukma.restaurant.domain.Reservation;
import edu.ukma.restaurant.dto.DiningTableDto;
import edu.ukma.restaurant.exception.NotFoundException;
import edu.ukma.restaurant.mapper.DiningTableMapper;
import edu.ukma.restaurant.repository.DiningTableRepository;
import edu.ukma.restaurant.repository.ReservationRepository;
import edu.ukma.restaurant.service.DiningTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiningTableServiceImpl implements DiningTableService {
    private final DiningTableRepository tableRepo;
    private final ReservationRepository reservationRepo;
    private final DiningTableMapper mapper;

    @Override
    public DiningTableDto create(DiningTableDto dto) {
        DiningTable saved = tableRepo.save(mapper.toNewEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    public DiningTableDto update(Long id, DiningTableDto dto) {
        DiningTable e = tableRepo.findById(id).orElseThrow(() -> new NotFoundException("Table not found: id=" + id));
        mapper.updateEntity(e, dto);
        return mapper.toDto(e);
    }

    @Override
    public void delete(Long id) {
        if (!tableRepo.existsById(id)) throw new NotFoundException("Table not found: id=" + id);
        tableRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DiningTableDto findById(Long id) {
        return mapper.toDto(tableRepo.findById(id).orElseThrow(() -> new NotFoundException("Table not found: id=" + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiningTableDto> findAll() {
        return tableRepo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiningTableDto> findAvailable(LocalDateTime start, LocalDateTime end, int partySize) {
        return tableRepo.findAll().stream()
            .filter(DiningTable::isActive)
            .filter(t -> t.getCapacity() >= partySize)
            .filter(t -> reservationRepo.findOverlaps(t.getId(), start, end).isEmpty())
            .map(mapper::toDto)
            .toList();
    }
}