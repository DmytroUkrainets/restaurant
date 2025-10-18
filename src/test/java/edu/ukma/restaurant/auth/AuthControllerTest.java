package edu.ukma.restaurant.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ukma.restaurant.dto.UserDto;
import edu.ukma.restaurant.security.JwtAuthFilter;
import edu.ukma.restaurant.security.JwtUtil;
import edu.ukma.restaurant.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @MockBean
    private UserService userService;
    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void register_returnsOk() throws Exception {
        var in = new UserDto(); in.setUsername("john"); in.setPassword("p");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(content().string("registered"));
    }

    @Test
    void login_returnsToken() throws Exception {
        var in = new UserDto(); in.setUsername("john"); in.setPassword("p");
        when(userService.login(any())).thenReturn("TOKEN");

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("TOKEN"));
    }
}
