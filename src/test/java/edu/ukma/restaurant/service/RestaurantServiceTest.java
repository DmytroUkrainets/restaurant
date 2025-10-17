package edu.ukma.restaurant.service;

import edu.ukma.restaurant.domain.Restaurant;
import edu.ukma.restaurant.domain.RestaurantStatus;
import edu.ukma.restaurant.dto.RestaurantDto;
import edu.ukma.restaurant.mapper.RestaurantMapper;
import edu.ukma.restaurant.repository.RestaurantRepository;
import edu.ukma.restaurant.service.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock private RestaurantRepository repo;
    @Mock private RestaurantMapper mapper;
    @InjectMocks private RestaurantServiceImpl service;

    @Test
    void createShouldSaveAndReturnDto() {
        RestaurantDto dto = RestaurantDto.builder()
            .name("Test").address("Addr").build();

        Restaurant entity = Restaurant.builder()
            .name("Test").address("Addr").status(RestaurantStatus.CLOSED).build();

        Restaurant saved = Restaurant.builder()
            .id(1L).name("Test").address("Addr").status(RestaurantStatus.CLOSED).build();

        RestaurantDto expected = RestaurantDto.builder()
            .id(1L).name("Test").address("Addr").status(RestaurantStatus.CLOSED).build();

        when(mapper.toNewEntity(dto)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(expected);

        var result = service.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());
        verify(repo, times(1)).save(entity);
    }

    @Test
    void findById_whenFound() {
        Restaurant e = Restaurant.builder()
            .id(5L).name("A").address("B").status(RestaurantStatus.CLOSED).build();
        RestaurantDto expected = RestaurantDto.builder()
            .id(5L).name("A").address("B").status(RestaurantStatus.CLOSED).build();

        when(repo.findById(5L)).thenReturn(Optional.of(e));
        when(mapper.toDto(e)).thenReturn(expected);

        var dto = service.findById(5L);

        assertEquals("A", dto.getName());
        assertEquals(5L, dto.getId());
    }

    @Test
    void openRestaurant_setsStatusOpen() {
        Restaurant e = Restaurant.builder()
            .id(3L).name("A").address("B").status(RestaurantStatus.CLOSED).build();
        when(repo.findById(3L)).thenReturn(Optional.of(e));

        service.openRestaurant(3L);

        assertEquals(RestaurantStatus.OPEN, e.getStatus());
    }

    @Test
    void delete_callsRepoDelete() {
        when(repo.existsById(2L)).thenReturn(true);

        service.delete(2L);

        verify(repo).deleteById(2L);
    }
}