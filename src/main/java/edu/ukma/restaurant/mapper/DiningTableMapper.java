package edu.ukma.restaurant.mapper;

import edu.ukma.restaurant.domain.DiningTable;
import edu.ukma.restaurant.dto.DiningTableDto;
import org.springframework.stereotype.Component;

@Component
public class DiningTableMapper {
    public DiningTableDto toDto(DiningTable e) {
        if (e == null) return null;
        return DiningTableDto.builder()
            .id(e.getId())
            .code(e.getCode())
            .capacity(e.getCapacity())
            .active(e.isActive())
            .build();
    }

    public DiningTable toNewEntity(DiningTableDto d) {
        return DiningTable.builder()
            .code(d.getCode())
            .capacity(d.getCapacity())
            .active(d.isActive())
            .build();
    }

    public void updateEntity(DiningTable e, DiningTableDto d) {
        if (d.getCode() != null) e.setCode(d.getCode());
        if (d.getCapacity() > 0) e.setCapacity(d.getCapacity());
        e.setActive(d.isActive());
    }
}