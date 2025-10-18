package edu.ukma.restaurant.service;

import edu.ukma.restaurant.dto.MenuDto;
import edu.ukma.restaurant.dto.MenuItemDto;

import java.util.List;

public interface MenuService {
    MenuDto createMenu(MenuDto dto);
    MenuDto findMenu(Long id);
    List<MenuDto> findMenusByRestaurant(Long restaurantId);
    MenuDto updateMenu(Long id, MenuDto dto);
    void deleteMenu(Long id);

    MenuItemDto addItem(Long menuId, MenuItemDto dto);
    MenuItemDto getItem(Long itemId);
    List<MenuItemDto> listItems(Long menuId);
    MenuItemDto updateItem(Long itemId, MenuItemDto dto);
    void deleteItem(Long itemId);
}
