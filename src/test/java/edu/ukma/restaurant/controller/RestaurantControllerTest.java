package edu.ukma.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ukma.restaurant.domain.RestaurantStatus;
import edu.ukma.restaurant.dto.RestaurantDto;
import edu.ukma.restaurant.security.JwtAuthFilter;
import edu.ukma.restaurant.security.JwtUtil;
import edu.ukma.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @MockBean
    private RestaurantService service;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void getAll_returnsList() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            RestaurantDto.builder().id(1L).name("A").address("Addr").status(RestaurantStatus.CLOSED).build()
        ));

        mvc.perform(get("/api/restaurants"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void create_returnsCreatedDto() throws Exception {
        var in = RestaurantDto.builder().name("A").address("X").build();
        var out = RestaurantDto.builder().id(10L).name("A").address("X").status(RestaurantStatus.CLOSED).build();
        when(service.create(any())).thenReturn(out);

        mvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(10));
    }
}
