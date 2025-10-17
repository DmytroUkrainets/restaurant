package edu.ukma.restaurant.service.impl;

import edu.ukma.restaurant.domain.DiningTable;
import edu.ukma.restaurant.domain.Reservation;
import edu.ukma.restaurant.domain.ReservationStatus;
import edu.ukma.restaurant.dto.ReservationDto;
import edu.ukma.restaurant.exception.NotFoundException;
import edu.ukma.restaurant.mapper.ReservationMapper;
import edu.ukma.restaurant.repository.DiningTableRepository;
import edu.ukma.restaurant.repository.ReservationRepository;
import edu.ukma.restaurant.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepo;
    private final DiningTableRepository tableRepo;
    private final ReservationMapper mapper;

    @Override
    public ReservationDto create(ReservationDto dto) {
        validateTime(dto.getStartTime(), dto.getEndTime());
        DiningTable table = tableRepo.findById(dto.getTableId())
            .orElseThrow(() -> new NotFoundException("Table not found: id=" + dto.getTableId()));
        if (!table.isActive()) throw new IllegalArgumentException("Table is not active");
        if (!reservationRepo.findOverlaps(table.getId(), dto.getStartTime(), dto.getEndTime()).isEmpty()) {
            throw new IllegalArgumentException("Time slot overlaps with existing reservations");
        }
        Reservation saved = reservationRepo.save(mapper.toNewEntity(dto, table));
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDto findById(Long id) {
        return mapper.toDto(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> findAll() {
        return reservationRepo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public ReservationDto update(Long id, ReservationDto dto) {
        Reservation e = getOrThrow(id);

        DiningTable table = null;
        if (dto.getTableId() != null) {
            table = tableRepo.findById(dto.getTableId())
                .orElseThrow(() -> new NotFoundException("Table not found: id=" + dto.getTableId()));
            if (!table.isActive()) throw new IllegalArgumentException("Table is not active");
        }

        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            validateTime(dto.getStartTime(), dto.getEndTime());
            Long tableId = (table != null ? table.getId() : e.getTable().getId());
            var overlaps = new ArrayList<>(reservationRepo.findOverlaps(tableId, dto.getStartTime(), dto.getEndTime()));
            overlaps.removeIf(r -> r.getId().equals(id));
            if (!overlaps.isEmpty()) {
                throw new IllegalArgumentException("Time slot overlaps with existing reservations");
            }
        }

        mapper.updateEntity(e, dto, table);
        return mapper.toDto(e);
    }

    @Override
    public void delete(Long id) {
        if (!reservationRepo.existsById(id)) {
            throw new NotFoundException("Reservation not found: id=" + id);
        }
        reservationRepo.deleteById(id);
    }

    @Override
    public void confirm(Long id) {
        Reservation e = getOrThrow(id);
        e.setStatus(ReservationStatus.CONFIRMED);
    }

    @Override
    public void cancel(Long id) {
        Reservation e = getOrThrow(id);
        e.setStatus(ReservationStatus.CANCELED);
    }

    @Override
    public void seat(Long id) {
        Reservation e = getOrThrow(id);
        e.setStatus(ReservationStatus.SEATED);
    }

    private Reservation getOrThrow(Long id) {
        return reservationRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Reservation not found: id=" + id));
    }

    private void validateTime(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new IllegalArgumentException("Invalid time interval");
        }
    }
}
