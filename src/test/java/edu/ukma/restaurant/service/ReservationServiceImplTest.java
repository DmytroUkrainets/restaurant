package edu.ukma.restaurant.service;

import edu.ukma.restaurant.domain.DiningTable;
import edu.ukma.restaurant.domain.Reservation;
import edu.ukma.restaurant.domain.ReservationStatus;
import edu.ukma.restaurant.dto.ReservationDto;
import edu.ukma.restaurant.exception.NotFoundException;
import edu.ukma.restaurant.mapper.ReservationMapper;
import edu.ukma.restaurant.repository.DiningTableRepository;
import edu.ukma.restaurant.repository.ReservationRepository;
import edu.ukma.restaurant.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock private ReservationRepository reservationRepo;
    @Mock private DiningTableRepository tableRepo;

    @Spy
    private ReservationMapper mapper = new ReservationMapper();

    @InjectMocks private ReservationServiceImpl service;

    @Test
    void create_success() {
        var start = LocalDateTime.parse("2025-10-17T18:00:00");
        var end   = LocalDateTime.parse("2025-10-17T19:00:00");
        var dto = ReservationDto.builder()
            .tableId(10L).customerName("X").startTime(start).endTime(end).build();
        var table = DiningTable.builder().id(10L).code("T10").capacity(4).active(true).build();

        when(tableRepo.findById(10L)).thenReturn(Optional.of(table));
        when(reservationRepo.findOverlaps(10L, start, end)).thenReturn(List.of());

        when(reservationRepo.save(any(Reservation.class))).thenAnswer(inv -> {
            Reservation r = inv.getArgument(0);
            r.setId(1L);
            return r;
        });

        var result = service.create(dto);
        assertEquals(1L, result.getId());
        assertEquals(ReservationStatus.PENDING, result.getStatus());
    }

    @Test
    void create_throwsOnInactiveTable() {
        var dto = ReservationDto.builder().tableId(1L)
            .startTime(LocalDateTime.parse("2025-10-17T18:00:00"))
            .endTime(LocalDateTime.parse("2025-10-17T19:00:00")).build();
        var table = DiningTable.builder().id(1L).code("T1").capacity(4).active(false).build();
        when(tableRepo.findById(1L)).thenReturn(Optional.of(table));
        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    }

    @Test
    void create_throwsOnOverlap() {
        var start = LocalDateTime.parse("2025-10-17T18:00:00");
        var end   = LocalDateTime.parse("2025-10-17T19:00:00");
        var dto = ReservationDto.builder().tableId(2L).startTime(start).endTime(end).build();
        var table = DiningTable.builder().id(2L).code("T2").capacity(4).active(true).build();
        when(tableRepo.findById(2L)).thenReturn(Optional.of(table));
        when(reservationRepo.findOverlaps(2L, start, end)).thenReturn(List.of(new Reservation()));
        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    }

    @Test
    void update_checksOverlapIgnoringSelf_andAppliesChanges() {
        var id = 5L;
        var existing = Reservation.builder()
            .id(id)
            .table(DiningTable.builder().id(7L).code("T7").active(true).capacity(4).build())
            .startTime(LocalDateTime.parse("2025-10-17T17:00:00"))
            .endTime(LocalDateTime.parse("2025-10-17T18:00:00"))
            .status(ReservationStatus.PENDING)
            .build();

        var newStart = LocalDateTime.parse("2025-10-17T18:00:00");
        var newEnd   = LocalDateTime.parse("2025-10-17T19:00:00");
        var dto = ReservationDto.builder().startTime(newStart).endTime(newEnd).build();

        when(reservationRepo.findById(id)).thenReturn(Optional.of(existing));
        when(reservationRepo.findOverlaps(7L, newStart, newEnd))
            .thenReturn(new ArrayList<>(List.of(existing)));

        var out = service.update(id, dto);
        assertEquals(newStart, existing.getStartTime());
        assertEquals(newEnd, existing.getEndTime());
        assertNotNull(out);
    }

    @Test
    void delete_notFoundThrows() {
        when(reservationRepo.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.delete(99L));
    }

    @Test
    void statusTransitions_confirm_cancel_seat() {
        var e = Reservation.builder().id(3L)
            .status(ReservationStatus.PENDING)
            .table(DiningTable.builder().id(1L).code("T1").active(true).capacity(2).build())
            .startTime(LocalDateTime.parse("2025-10-17T18:00:00"))
            .endTime(LocalDateTime.parse("2025-10-17T19:00:00")).build();
        when(reservationRepo.findById(3L)).thenReturn(Optional.of(e));

        service.confirm(3L); assertEquals(ReservationStatus.CONFIRMED, e.getStatus());
        service.seat(3L);    assertEquals(ReservationStatus.SEATED, e.getStatus());
        service.cancel(3L);  assertEquals(ReservationStatus.CANCELED, e.getStatus());
    }
}