package edu.ukma.restaurant.mapper;

import edu.ukma.restaurant.dto.MenuItemDto;
import edu.ukma.restaurant.entity.Menu;
import edu.ukma.restaurant.entity.MenuItem;

public class MenuItemMapper {
    public static MenuItemDto toDto(MenuItem e){
        if(e==null) return null;
        MenuItemDto d = new MenuItemDto();
        d.setId(e.getId());
        d.setMenuId(e.getMenu()!=null ? e.getMenu().getId() : null);
        d.setName(e.getName());
        d.setDescription(e.getDescription());
        d.setPrice(e.getPrice());
        d.setAvailable(e.isAvailable());
        return d;
    }

    public static MenuItem toNewEntity(MenuItemDto d, Menu menu){
        MenuItem e = new MenuItem();
        e.setMenu(menu);
        e.setName(d.getName());
        e.setDescription(d.getDescription());
        e.setPrice(d.getPrice());
        e.setAvailable(d.isAvailable());
        return e;
    }

    public static void updateEntity(MenuItem e, MenuItemDto d, Menu menu){
        if(menu!=null) e.setMenu(menu);
        if(d.getName()!=null) e.setName(d.getName());
        if(d.getDescription()!=null) e.setDescription(d.getDescription());
        if(d.getPrice()!=null) e.setPrice(d.getPrice());
        e.setAvailable(d.isAvailable() || !d.isAvailable() ? d.isAvailable() : e.isAvailable());
    }
}
