package edu.ukma.restaurant.service.impl;

import edu.ukma.restaurant.dto.MenuDto;
import edu.ukma.restaurant.dto.MenuItemDto;
import edu.ukma.restaurant.entity.Menu;
import edu.ukma.restaurant.entity.MenuItem;
import edu.ukma.restaurant.domain.Restaurant;
import edu.ukma.restaurant.mapper.MenuItemMapper;
import edu.ukma.restaurant.mapper.MenuMapper;
import edu.ukma.restaurant.repository.MenuItemRepository;
import edu.ukma.restaurant.repository.MenuRepository;
import edu.ukma.restaurant.repository.RestaurantRepository;
import edu.ukma.restaurant.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepo;
    private final MenuItemRepository itemRepo;
    private final RestaurantRepository restaurantRepo;

    public MenuServiceImpl(MenuRepository menuRepo, MenuItemRepository itemRepo, RestaurantRepository restaurantRepo) {
        this.menuRepo = menuRepo;
        this.itemRepo = itemRepo;
        this.restaurantRepo = restaurantRepo;
    }

    @Override
    public MenuDto createMenu(MenuDto dto) {
        if (dto.getRestaurantId()==null) throw new IllegalArgumentException("restaurantId is required");
        Restaurant r = restaurantRepo.findById(dto.getRestaurantId())
            .orElseThrow(() -> new NoSuchElementException("Restaurant not found"));
        Menu saved = menuRepo.save(MenuMapper.toNewEntity(dto, r));
        return MenuMapper.toDto(saved);
    }

    @Override @Transactional(readOnly = true)
    public MenuDto findMenu(Long id) {
        Menu e = menuRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Menu not found"));
        return MenuMapper.toDto(e);
    }

    @Override @Transactional(readOnly = true)
    public java.util.List<MenuDto> findMenusByRestaurant(Long restaurantId) {
        Restaurant r = restaurantRepo.findById(restaurantId)
            .orElseThrow(() -> new NoSuchElementException("Restaurant not found"));
        return menuRepo.findAll().stream()
            .filter(m -> m.getRestaurant()!=null && restaurantId.equals(m.getRestaurant().getId()))
            .map(MenuMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public MenuDto updateMenu(Long id, MenuDto dto) {
        Menu e = menuRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Menu not found"));
        Restaurant r = null;
        if (dto.getRestaurantId()!=null) {
            r = restaurantRepo.findById(dto.getRestaurantId())
                .orElseThrow(() -> new NoSuchElementException("Restaurant not found"));
        }
        MenuMapper.updateEntity(e, dto, r);
        return MenuMapper.toDto(e);
    }

    @Override
    public void deleteMenu(Long id) {
        if (!menuRepo.existsById(id)) throw new NoSuchElementException("Menu not found");
        menuRepo.deleteById(id);
    }

    @Override
    public MenuItemDto addItem(Long menuId, MenuItemDto dto) {
        Menu menu = menuRepo.findById(menuId).orElseThrow(() -> new NoSuchElementException("Menu not found"));
        MenuItem saved = itemRepo.save(MenuItemMapper.toNewEntity(dto, menu));
        return MenuItemMapper.toDto(saved);
    }

    @Override @Transactional(readOnly = true)
    public MenuItemDto getItem(Long itemId) {
        MenuItem e = itemRepo.findById(itemId).orElseThrow(() -> new NoSuchElementException("Item not found"));
        return MenuItemMapper.toDto(e);
    }

    @Override @Transactional(readOnly = true)
    public java.util.List<MenuItemDto> listItems(Long menuId) {
        Menu menu = menuRepo.findById(menuId).orElseThrow(() -> new NoSuchElementException("Menu not found"));
        return menu.getItems()==null ? java.util.List.of() :
            menu.getItems().stream().map(MenuItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MenuItemDto updateItem(Long itemId, MenuItemDto dto) {
        MenuItem e = itemRepo.findById(itemId).orElseThrow(() -> new NoSuchElementException("Item not found"));
        Menu menu = null;
        if (dto.getMenuId()!=null) {
            menu = menuRepo.findById(dto.getMenuId()).orElseThrow(() -> new NoSuchElementException("Menu not found"));
        }
        MenuItemMapper.updateEntity(e, dto, menu);
        return MenuItemMapper.toDto(e);
    }

    @Override
    public void deleteItem(Long itemId) {
        if (!itemRepo.existsById(itemId)) throw new NoSuchElementException("Item not found");
        itemRepo.deleteById(itemId);
    }
}
