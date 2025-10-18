package edu.ukma.restaurant.controller;

import edu.ukma.restaurant.dto.MenuDto;
import edu.ukma.restaurant.dto.MenuItemDto;
import edu.ukma.restaurant.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService service;
    public MenuController(MenuService service){ this.service = service; }

    @PostMapping
    public ResponseEntity<MenuDto> create(@RequestBody MenuDto dto){
        return ResponseEntity.ok(service.createMenu(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> get(@PathVariable Long id){
        return ResponseEntity.ok(service.findMenu(id));
    }

    @GetMapping
    public ResponseEntity<List<MenuDto>> byRestaurant(@RequestParam("restaurantId") Long restaurantId){
        return ResponseEntity.ok(service.findMenusByRestaurant(restaurantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDto> update(@PathVariable Long id, @RequestBody MenuDto dto){
        return ResponseEntity.ok(service.updateMenu(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{menuId}/items")
    public ResponseEntity<MenuItemDto> addItem(@PathVariable Long menuId, @RequestBody MenuItemDto dto){
        return ResponseEntity.ok(service.addItem(menuId, dto));
    }

    @GetMapping("/{menuId}/items")
    public ResponseEntity<List<MenuItemDto>> listItems(@PathVariable Long menuId){
        return ResponseEntity.ok(service.listItems(menuId));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<MenuItemDto> getItem(@PathVariable Long itemId){
        return ResponseEntity.ok(service.getItem(itemId));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<MenuItemDto> updateItem(@PathVariable Long itemId, @RequestBody MenuItemDto dto){
        return ResponseEntity.ok(service.updateItem(itemId, dto));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId){
        service.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
