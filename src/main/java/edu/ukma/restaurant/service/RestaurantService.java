package edu.ukma.restaurant.service;

import edu.ukma.restaurant.dto.RestaurantDto;

import java.util.List;

public interface RestaurantService {
    RestaurantDto create(RestaurantDto dto);
    RestaurantDto findById(Long id);
    List<RestaurantDto> findAll();
    RestaurantDto update(Long id, RestaurantDto dto);
    void delete(Long id);
    void openRestaurant(Long id);
    void closeRestaurant(Long id);
}