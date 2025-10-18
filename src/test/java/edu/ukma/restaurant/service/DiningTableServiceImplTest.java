package edu.ukma.restaurant.service;

import edu.ukma.restaurant.domain.DiningTable;
import edu.ukma.restaurant.dto.DiningTableDto;
import edu.ukma.restaurant.mapper.DiningTableMapper;
import edu.ukma.restaurant.repository.DiningTableRepository;
import edu.ukma.restaurant.repository.ReservationRepository;
import edu.ukma.restaurant.service.impl.DiningTableServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiningTableServiceImplTest {

    @Mock private DiningTableRepository tableRepo;
    @Mock private ReservationRepository reservationRepo;
    @Mock private DiningTableMapper mapper;

    @InjectMocks private DiningTableServiceImpl service;

    @Test
    void findAvailable_filtersByActiveCapacityAndOverlaps() {
        var t1 = DiningTable.builder().id(1L).code("T1").capacity(2).active(true).build();
        var t2 = DiningTable.builder().id(2L).code("T2").capacity(4).active(true).build();
        var t3 = DiningTable.builder().id(3L).code("T3").capacity(4).active(false).build();
        when(tableRepo.findAll()).thenReturn(List.of(t1, t2, t3));

        LocalDateTime start = LocalDateTime.parse("2025-10-17T18:00:00");
        LocalDateTime end   = LocalDateTime.parse("2025-10-17T19:00:00");

        when(reservationRepo.findOverlaps(eq(2L), any(), any())).thenReturn(List.of());
        when(mapper.toDto(t2)).thenReturn(DiningTableDto.builder()
            .id(2L).code("T2").capacity(4).active(true).build());

        var result = service.findAvailable(start, end, 3);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
    }
}