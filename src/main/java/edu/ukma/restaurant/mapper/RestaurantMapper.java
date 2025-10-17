package edu.ukma.restaurant.mapper;

import edu.ukma.restaurant.entity.Restaurant;
import edu.ukma.restaurant.dto.RestaurantDto;

public class RestaurantMapper {
    public static RestaurantDto toDto(Restaurant r){
        if(r==null) return null;
        RestaurantDto d = new RestaurantDto();
        d.setId(r.getId());
        d.setName(r.getName());
        d.setAddress(r.getAddress());
        d.setStatus(r.getStatus() != null ? r.getStatus().name() : null);
        return d;
    }

    public static Restaurant toEntity(RestaurantDto d) {
        if(d==null) return null;
        Restaurant r = new Restaurant();
        r.setName(d.getName());
        r.setAddress(d.getAddress());
        if(d.getStatus()!=null){
            try {
                r.setStatus(Restaurant.Status.valueOf(d.getStatus()));
            } catch(Exception e){}
        }
        return r;
    }
}

