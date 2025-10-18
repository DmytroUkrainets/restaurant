package edu.ukma.restaurant.service;

import edu.ukma.restaurant.auth.JwtService;
import edu.ukma.restaurant.entity.User;
import edu.ukma.restaurant.dto.UserDto;
import edu.ukma.restaurant.repository.UserRepository;
import edu.ukma.restaurant.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository repo;
    @Mock
    PasswordEncoder encoder;
    @Mock
    JwtService jwt;

    @InjectMocks
    UserServiceImpl service;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    void register_success_encodesPassword_andSaves() {
        var dto = new UserDto();
        dto.setUsername("alice");
        dto.setEmail("a@a.com");
        dto.setPassword("secret");
        when(repo.existsByUsername("alice")).thenReturn(false);
        when(repo.existsByEmail("a@a.com")).thenReturn(false);
        when(encoder.encode("secret")).thenReturn("HASH");

        service.register(dto);

        verify(repo).save(userCaptor.capture());
        var saved = userCaptor.getValue();
        assertEquals("alice", saved.getUsername());
        assertEquals("HASH", saved.getPasswordHash());
        assertTrue(saved.isActive());
    }

    @Test
    void register_usernameExists_throws() {
        var dto = new UserDto(); dto.setUsername("bob"); dto.setPassword("x");
        when(repo.existsByUsername("bob")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.register(dto));
        verify(repo, never()).save(any());
    }

    @Test
    void login_success_returnsJwt() {
        var u = new User();
        u.setUsername("kate"); u.setPasswordHash("HASH"); u.setActive(true); u.setRole(User.Role.MANAGER);
        when(repo.findByUsername("kate")).thenReturn(Optional.of(u));
        when(encoder.matches("pass", "HASH")).thenReturn(true);
        when(jwt.generate("kate", "MANAGER")).thenReturn("TOKEN");

        var dto = new UserDto(); dto.setUsername("kate"); dto.setPassword("pass");
        var token = service.login(dto);

        assertEquals("TOKEN", token);
    }

    @Test
    void login_wrongPassword_throws() {
        var u = new User(); u.setUsername("u"); u.setPasswordHash("HASH"); u.setActive(true);
        when(repo.findByUsername("u")).thenReturn(Optional.of(u));
        when(encoder.matches("bad", "HASH")).thenReturn(false);

        var dto = new UserDto(); dto.setUsername("u"); dto.setPassword("bad");
        assertThrows(NoSuchElementException.class, () -> service.login(dto));
    }

    @Test
    void create_success_returnsDto() {
        var dto = new UserDto();
        dto.setUsername("neo"); dto.setEmail("n@n.com"); dto.setPassword("p");
        when(repo.existsByUsername("neo")).thenReturn(false);
        when(repo.existsByEmail("n@n.com")).thenReturn(false);
        when(encoder.encode("p")).thenReturn("HP");
        when(repo.save(any(User.class))).thenAnswer(inv -> {
            var e = inv.getArgument(0, User.class);
            e.setId(42L);
            return e;
        });

        var out = service.create(dto);

        assertEquals(42L, out.getId());
        assertEquals("neo", out.getUsername());
        verify(repo).save(any(User.class));
    }

    @Test
    void update_changePassword_encodes() {
        var e = new User(); e.setId(7L); e.setUsername("u"); e.setPasswordHash("OLD");
        when(repo.findById(7L)).thenReturn(Optional.of(e));
        when(encoder.encode("new")).thenReturn("NEW_HASH");
        var dto = new UserDto(); dto.setPassword("new");

        var out = service.update(7L, dto);

        assertEquals("NEW_HASH", e.getPasswordHash());
        assertEquals("u", out.getUsername());
    }

    @Test
    void delete_notFound_throws() {
        when(repo.existsById(99L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.delete(99L));
    }
}
