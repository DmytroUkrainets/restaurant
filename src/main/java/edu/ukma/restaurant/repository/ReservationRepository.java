package edu.ukma.restaurant.repository;

import edu.ukma.restaurant.domain.Reservation;
import edu.ukma.restaurant.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.table.id = :tableId and r.startTime < :end and r.endTime > :start")
    List<Reservation> findOverlaps(@Param("tableId") Long tableId,
                                   @Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end);

    List<Reservation> findByStatusIn(List<ReservationStatus> statuses);
}