package edu.ukma.restaurant.service.impl;

import edu.ukma.restaurant.service.RestaurantService;
import edu.ukma.restaurant.repository.RestaurantRepository;
import edu.ukma.restaurant.entity.Restaurant;
import edu.ukma.restaurant.dto.RestaurantDto;
import edu.ukma.restaurant.mapper.RestaurantMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantDto create(RestaurantDto dto) {
        Restaurant r = RestaurantMapper.toEntity(dto);
        Restaurant saved = restaurantRepository.save(r);
        return RestaurantMapper.toDto(saved);
    }

    @Override
    public RestaurantDto findById(Long id) {
        return restaurantRepository.findById(id).map(RestaurantMapper::toDto)
            .orElseThrow(() -> new NoSuchElementException("Restaurant not found: " + id));
    }

    @Override
    public List<RestaurantDto> findAll() {
        return restaurantRepository.findAll().stream().map(RestaurantMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public RestaurantDto update(Long id, RestaurantDto dto) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No restaurant"));
        r.setName(dto.getName());
        r.setAddress(dto.getAddress());
        Restaurant updated = restaurantRepository.save(r);
        return RestaurantMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public void openRestaurant(Long id) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No restaurant"));
        r.setStatus(Restaurant.Status.OPEN);
        restaurantRepository.save(r);
    }

    @Override
    public void closeRestaurant(Long id) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No restaurant"));
        r.setStatus(Restaurant.Status.CLOSED);
        restaurantRepository.save(r);
    }
}

