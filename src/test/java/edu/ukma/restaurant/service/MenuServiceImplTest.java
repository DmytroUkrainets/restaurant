package edu.ukma.restaurant.service;

import edu.ukma.restaurant.dto.MenuDto;
import edu.ukma.restaurant.dto.MenuItemDto;
import edu.ukma.restaurant.entity.Menu;
import edu.ukma.restaurant.entity.MenuItem;
import edu.ukma.restaurant.domain.Restaurant;
import edu.ukma.restaurant.repository.MenuItemRepository;
import edu.ukma.restaurant.repository.MenuRepository;
import edu.ukma.restaurant.repository.RestaurantRepository;
import edu.ukma.restaurant.service.impl.MenuServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    MenuRepository menuRepo;
    @Mock
    MenuItemRepository itemRepo;
    @Mock
    RestaurantRepository restaurantRepo;

    @InjectMocks
    MenuServiceImpl service;

    @Test
    void createMenu_success() {
        var r = new Restaurant(); r.setId(1L);
        when(restaurantRepo.findById(1L)).thenReturn(Optional.of(r));
        when(menuRepo.save(any(Menu.class))).thenAnswer(inv -> {
            var m = inv.getArgument(0, Menu.class);
            m.setId(10L);
            return m;
        });

        var dto = new MenuDto(); dto.setName("Main"); dto.setRestaurantId(1L);
        var out = service.createMenu(dto);

        assertEquals(10L, out.getId());
        assertEquals("Main", out.getName());
        assertEquals(1L, out.getRestaurantId());
    }

    @Test
    void createMenu_restaurantMissing_throws() {
        when(restaurantRepo.findById(9L)).thenReturn(Optional.empty());
        var dto = new MenuDto(); dto.setRestaurantId(9L);
        assertThrows(NoSuchElementException.class, () -> service.createMenu(dto));
    }

    @Test
    void findMenusByRestaurant_filters() {
        var r1 = new Restaurant(); r1.setId(1L);
        var r2 = new Restaurant(); r2.setId(2L);
        var m1 = new Menu(); m1.setId(11L); m1.setName("A"); m1.setRestaurant(r1);
        var m2 = new Menu(); m2.setId(22L); m2.setName("B"); m2.setRestaurant(r2);
        when(restaurantRepo.findById(1L)).thenReturn(Optional.of(r1));
        when(menuRepo.findAll()).thenReturn(List.of(m1, m2));

        var list = service.findMenusByRestaurant(1L);
        assertEquals(1, list.size());
        assertEquals(11L, list.get(0).getId());
    }

    @Test
    void addItem_success() {
        var menu = new Menu(); menu.setId(5L);
        when(menuRepo.findById(5L)).thenReturn(Optional.of(menu));
        when(itemRepo.save(any(MenuItem.class))).thenAnswer(inv -> {
            var e = inv.getArgument(0, MenuItem.class);
            e.setId(100L);
            return e;
        });

        var in = new MenuItemDto();
        in.setName("Soup"); in.setPrice(new BigDecimal("9.99")); in.setAvailable(true);
        var out = service.addItem(5L, in);

        assertEquals(100L, out.getId());
        assertEquals("Soup", out.getName());
    }

    @Test
    void listItems_returnsMappedDtos() {
        var menu = new Menu(); menu.setId(7L);
        var mi = new MenuItem(); mi.setId(1L); mi.setName("Tea"); mi.setPrice(new BigDecimal("2.50")); mi.setMenu(menu);
        menu.setItems(List.of(mi));
        when(menuRepo.findById(7L)).thenReturn(Optional.of(menu));

        var list = service.listItems(7L);
        assertEquals(1, list.size());
        assertEquals("Tea", list.get(0).getName());
    }

    @Test
    void updateItem_changesFields() {
        var item = new MenuItem(); item.setId(8L); item.setName("Old"); item.setAvailable(false);
        when(itemRepo.findById(8L)).thenReturn(Optional.of(item));

        var dto = new MenuItemDto(); dto.setName("New"); dto.setAvailable(true);
        var out = service.updateItem(8L, dto);

        assertEquals("New", out.getName());
        assertTrue(out.isAvailable());
    }

    @Test
    void deleteItem_notFound_throws() {
        when(itemRepo.existsById(123L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.deleteItem(123L));
    }
}
