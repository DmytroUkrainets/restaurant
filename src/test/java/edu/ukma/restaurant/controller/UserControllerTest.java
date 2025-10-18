package edu.ukma.restaurant.controller;

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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

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
    void create_returnsDto() throws Exception {
        var in = new UserDto(); in.setUsername("a"); in.setPassword("p");
        var out = new UserDto(); out.setId(1L); out.setUsername("a");
        when(userService.create(any())).thenReturn(out);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void byUsername_returnsDto() throws Exception {
        var dto = new UserDto(); dto.setId(5L); dto.setUsername("bob");
        when(userService.findByUsername("bob")).thenReturn(dto);

        mvc.perform(get("/api/users/by-username/bob"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.username").value("bob"));
    }

    @Test
    void list_returnsArray() throws Exception {
        var d = new UserDto(); d.setId(2L); d.setUsername("x");
        when(userService.findAll()).thenReturn(List.of(d));

        mvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void update_returnsDto() throws Exception {
        var in = new UserDto(); in.setFullName("New");
        var out = new UserDto(); out.setId(3L); out.setFullName("New");
        when(userService.update(eq(3L), any())).thenReturn(out);

        mvc.perform(put("/api/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.fullName").value("New"));
    }
}
