package edu.ukma.restaurant.service.impl;

import edu.ukma.restaurant.auth.JwtService;
import edu.ukma.restaurant.entity.User;
import edu.ukma.restaurant.dto.UserDto;
import edu.ukma.restaurant.mapper.UserMapper;
import edu.ukma.restaurant.repository.UserRepository;
import edu.ukma.restaurant.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwt;

    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder, JwtService jwt) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
    }

    @Override
    public UserDto create(UserDto dto) {
        validateUsername(dto.getUsername());
        checkUniq(dto.getUsername(), dto.getEmail());
        User e = new User();
        e.setUsername(dto.getUsername());
        e.setFullName(dto.getFullName());
        e.setEmail(dto.getEmail());
        e.setRole(parseRole(dto.getRole(), User.Role.WAITER));
        e.setActive(dto.isActive());
        if (dto.getPassword() == null || dto.getPassword().isBlank())
            throw new IllegalArgumentException("password is required");
        e.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return UserMapper.toDto(repo.save(e));
    }

    @Override @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return UserMapper.toDto(repo.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    @Override @Transactional(readOnly = true)
    public java.util.List<UserDto> findAll() {
        return repo.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        if (dto.getUsername()!=null && !dto.getUsername().equals(e.getUsername()))
            checkUsernameUniq(dto.getUsername());
        if (dto.getEmail()!=null && !dto.getEmail().equals(e.getEmail()))
            checkEmailUniq(dto.getEmail());

        UserMapper.updateEntity(e, dto);
        if (dto.getPassword()!=null && !dto.getPassword().isBlank()) {
            e.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }
        return UserMapper.toDto(e);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NoSuchElementException("User not found");
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        return UserMapper.toDto(repo.findByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    @Override
    public void register(UserDto dto) {
        validateUsername(dto.getUsername());
        if (dto.getPassword()==null || dto.getPassword().isBlank())
            throw new IllegalArgumentException("password is required");
        checkUniq(dto.getUsername(), dto.getEmail());

        User e = new User();
        e.setUsername(dto.getUsername());
        e.setFullName(dto.getFullName());
        e.setEmail(dto.getEmail());
        e.setRole(parseRole(dto.getRole(), User.Role.WAITER));
        e.setActive(true);
        e.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        repo.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public String login(UserDto dto) {
        validateUsername(dto.getUsername());
        if (dto.getPassword()==null || dto.getPassword().isBlank())
            throw new IllegalArgumentException("password is required");

        User e = repo.findByUsername(dto.getUsername())
            .orElseThrow(() -> new NoSuchElementException("Invalid username or password"));
        if (!e.isActive()) throw new IllegalStateException("User is inactive");

        if (!passwordEncoder.matches(dto.getPassword(), e.getPasswordHash()))
            throw new NoSuchElementException("Invalid username or password");

        return jwt.generate(e.getUsername(), e.getRole()!=null ? e.getRole().name() : "WAITER");
    }

    private void validateUsername(String u) {
        if (u==null || u.isBlank()) throw new IllegalArgumentException("username is required");
    }
    private void checkUniq(String username, String email){
        checkUsernameUniq(username);
        if (email!=null && !email.isBlank()) checkEmailUniq(email);
    }
    private void checkUsernameUniq(String username){
        if (repo.existsByUsername(username)) throw new IllegalArgumentException("username already exists");
    }
    private void checkEmailUniq(String email){
        if (repo.existsByEmail(email)) throw new IllegalArgumentException("email already exists");
    }
    private User.Role parseRole(String role, User.Role def){
        if (role==null) return def;
        try { return User.Role.valueOf(role); } catch (Exception e){ return def; }
    }
}
