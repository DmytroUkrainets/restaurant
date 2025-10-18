package edu.ukma.restaurant.service;

import edu.ukma.restaurant.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto create(UserDto dto);
    UserDto findById(Long id);
    List<UserDto> findAll();
    UserDto update(Long id, UserDto dto);
    void delete(Long id);
    UserDto findByUsername(String username);

    void register(UserDto dto);
    String login(UserDto dto);
}
