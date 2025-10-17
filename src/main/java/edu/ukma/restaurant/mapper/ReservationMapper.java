package edu.ukma.restaurant.mapper;

import edu.ukma.restaurant.domain.DiningTable;
import edu.ukma.restaurant.domain.Reservation;
import edu.ukma.restaurant.domain.ReservationStatus;
import edu.ukma.restaurant.dto.ReservationDto;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDto toDto(Reservation e) {
        if (e == null) return null;
        return ReservationDto.builder()
            .id(e.getId())
            .tableId(e.getTable().getId())
            .customerName(e.getCustomerName())
            .customerPhone(e.getCustomerPhone())
            .startTime(e.getStartTime())
            .endTime(e.getEndTime())
            .status(e.getStatus())
            .build();
    }

    public Reservation toNewEntity(ReservationDto d, DiningTable table) {
        ReservationStatus st = d.getStatus() != null ? d.getStatus() : ReservationStatus.PENDING;
        return Reservation.builder()
            .table(table)
            .customerName(d.getCustomerName())
            .customerPhone(d.getCustomerPhone())
            .startTime(d.getStartTime())
            .endTime(d.getEndTime())
            .status(st)
            .build();
    }

    public void updateEntity(Reservation e, ReservationDto d, DiningTable table) {
        if (table != null) e.setTable(table);
        if (d.getCustomerName() != null) e.setCustomerName(d.getCustomerName());
        if (d.getCustomerPhone() != null) e.setCustomerPhone(d.getCustomerPhone());
        if (d.getStartTime() != null) e.setStartTime(d.getStartTime());
        if (d.getEndTime() != null) e.setEndTime(d.getEndTime());
        if (d.getStatus() != null) e.setStatus(d.getStatus());
    }
}