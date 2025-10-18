package edu.ukma.restaurant.mapper;

import edu.ukma.restaurant.dto.MenuDto;
import edu.ukma.restaurant.entity.Menu;
import edu.ukma.restaurant.domain.Restaurant;

public class MenuMapper {
    public static MenuDto toDto(Menu e){
        if(e==null) return null;
        MenuDto d = new MenuDto();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setRestaurantId(e.getRestaurant()!=null ? e.getRestaurant().getId() : null);
        return d;
    }

    public static Menu toNewEntity(MenuDto d, Restaurant r){
        Menu e = new Menu();
        e.setName(d.getName());
        e.setRestaurant(r);
        return e;
    }

    public static void updateEntity(Menu e, MenuDto d, Restaurant r){
        if(d.getName()!=null) e.setName(d.getName());
        if(r!=null) e.setRestaurant(r);
    }
}
