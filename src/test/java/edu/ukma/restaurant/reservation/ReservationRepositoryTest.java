package edu.ukma.restaurant.reservation;

import edu.ukma.restaurant.domain.DiningTable;
import edu.ukma.restaurant.domain.Reservation;
import edu.ukma.restaurant.domain.ReservationStatus;
import edu.ukma.restaurant.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void overlaps_found() {
        var table = em.persist(DiningTable.builder().code("T1").capacity(4).active(true).build());

        var r1 = Reservation.builder()
            .table(table)
            .customerName("A")
            .startTime(LocalDateTime.parse("2025-10-17T18:00:00"))
            .endTime(LocalDateTime.parse("2025-10-17T19:00:00"))
            .status(ReservationStatus.PENDING)
            .build();
        em.persist(r1);
        em.flush();

        var overlaps = repo.findOverlaps(
            table.getId(),
            LocalDateTime.parse("2025-10-17T18:30:00"),
            LocalDateTime.parse("2025-10-17T19:30:00")
        );

        assertFalse(overlaps.isEmpty());
    }
}

