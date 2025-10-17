package edu.ukma.restaurant.service.impl;

import edu.ukma.restaurant.domain.Restaurant;
import edu.ukma.restaurant.domain.RestaurantStatus;
import edu.ukma.restaurant.dto.RestaurantDto;
import edu.ukma.restaurant.exception.NotFoundException;
import edu.ukma.restaurant.mapper.RestaurantMapper;
import edu.ukma.restaurant.repository.RestaurantRepository;
import edu.ukma.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    @Override
    public RestaurantDto create(RestaurantDto dto) {
        Restaurant saved = repository.save(mapper.toNewEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantDto findById(Long id) {
        Restaurant e = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found: id=" + id));
        return mapper.toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public RestaurantDto update(Long id, RestaurantDto dto) {
        Restaurant e = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found: id=" + id));
        mapper.updateEntity(e, dto);
        return mapper.toDto(e);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Restaurant not found: id=" + id);
        }
        repository.deleteById(id);
    }

    @Override
    public void openRestaurant(Long id) {
        Restaurant e = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found: id=" + id));
        e.setStatus(RestaurantStatus.OPEN);
    }

    @Override
    public void closeRestaurant(Long id) {
        Restaurant e = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found: id=" + id));
        e.setStatus(RestaurantStatus.CLOSED);
    }
}