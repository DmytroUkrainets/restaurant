package edu.ukma.restaurant.service;

import edu.ukma.restaurant.repository.RestaurantRepository;
import edu.ukma.restaurant.entity.Restaurant;
import edu.ukma.restaurant.dto.RestaurantDto;
import edu.ukma.restaurant.service.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository repo;

    private RestaurantServiceImpl service;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        service = new RestaurantServiceImpl(repo);
    }

    @Test
    void createShouldSaveAndReturnDto(){
        RestaurantDto dto = new RestaurantDto();
        dto.setName("Test");
        dto.setAddress("Addr");
        Restaurant saved = new Restaurant("Test","Addr");
        saved.setId(1L);
        when(repo.save(any(Restaurant.class))).thenReturn(saved);

        var result = service.create(dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());
        verify(repo, times(1)).save(any(Restaurant.class));
    }

    @Test
    void findById_whenFound(){
        Restaurant r = new Restaurant("A","B"); r.setId(5L);
        when(repo.findById(5L)).thenReturn(Optional.of(r));
        var dto = service.findById(5L);
        assertEquals("A", dto.getName());
    }

    @Test
    void openRestaurant_setsStatusOpen(){
        Restaurant r = new Restaurant("A","B"); r.setId(3L);
        when(repo.findById(3L)).thenReturn(Optional.of(r));
        when(repo.save(any(Restaurant.class))).thenAnswer(i->i.getArgument(0));
        service.openRestaurant(3L);
        assertEquals(Restaurant.Status.OPEN, r.getStatus());
        verify(repo).save(r);
    }

    @Test
    void delete_callsRepoDelete(){
        doNothing().when(repo).deleteById(2L);
        service.delete(2L);
        verify(repo).deleteById(2L);
    }
}

