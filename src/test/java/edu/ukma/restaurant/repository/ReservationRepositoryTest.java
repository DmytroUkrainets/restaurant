package edu.ukma.restaurant.repository;

import edu.ukma.restaurant.domain.DiningTable;
import edu.ukma.restaurant.domain.Reservation;
import edu.ukma.restaurant.domain.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ReservationRepositoryTest {

    @Autowired private ReservationRepository repo;
    @Autowired private TestEntityManager em;

    @Test
    void overlaps_found_whenIntervalsIntersect() {
        DiningTable t = em.persist(DiningTable.builder().code("T1").capacity(4).active(true).build());
        Reservation r = Reservation.builder()
            .table(t)
            .customerName("A")
            .startTime(LocalDateTime.parse("2025-10-17T18:00:00"))
            .endTime(LocalDateTime.parse("2025-10-17T19:00:00"))
            .status(ReservationStatus.PENDING)
            .build();
        em.persistAndFlush(r);

        List<Reservation> overlaps = repo.findOverlaps(
            t.getId(),
            LocalDateTime.parse("2025-10-17T18:30:00"),
            LocalDateTime.parse("2025-10-17T19:30:00")
        );
        assertFalse(overlaps.isEmpty(), "Expected overlap to be detected");
    }

    @Test
    void adjacency_isNotOverlap_withOpenIntervalLogic() {
        DiningTable t = em.persist(DiningTable.builder().code("T2").capacity(2).active(true).build());
        Reservation r = Reservation.builder()
            .table(t)
            .customerName("B")
            .startTime(LocalDateTime.parse("2025-10-17T18:00:00"))
            .endTime(LocalDateTime.parse("2025-10-17T19:00:00"))
            .status(ReservationStatus.PENDING)
            .build();
        em.persistAndFlush(r);

        List<Reservation> overlaps = repo.findOverlaps(
            t.getId(),
            LocalDateTime.parse("2025-10-17T19:00:00"),
            LocalDateTime.parse("2025-10-17T20:00:00")
        );
        assertTrue(overlaps.isEmpty(), "Adjacent interval should not be considered overlap");
    }
}
