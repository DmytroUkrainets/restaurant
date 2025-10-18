package edu.ukma.restaurant.auth;

import edu.ukma.restaurant.dto.UserDto;
import edu.ukma.restaurant.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto dto) {
        userService.register(dto);
        return ResponseEntity.ok("registered");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody UserDto dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
