package edu.ukma.restaurant.mapper;

import edu.ukma.restaurant.domain.Restaurant;
import edu.ukma.restaurant.domain.RestaurantStatus;
import edu.ukma.restaurant.dto.RestaurantDto;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {
    public RestaurantDto toDto(Restaurant e) {
        if (e == null) return null;
        return RestaurantDto.builder()
            .id(e.getId())
            .name(e.getName())
            .address(e.getAddress())
            .phone(e.getPhone())
            .status(e.getStatus())
            .build();
    }

    public void updateEntity(Restaurant e, RestaurantDto d) {
        if (d.getName() != null) e.setName(d.getName());
        if (d.getAddress() != null) e.setAddress(d.getAddress());
        if (d.getPhone() != null) e.setPhone(d.getPhone());
        if (d.getStatus() != null) e.setStatus(d.getStatus());
    }

    public Restaurant toNewEntity(RestaurantDto d) {
        RestaurantStatus st = d.getStatus() != null ? d.getStatus() : RestaurantStatus.CLOSED;
        return Restaurant.builder()
            .name(d.getName())
            .address(d.getAddress())
            .phone(d.getPhone())
            .status(st)
            .build();
    }
}