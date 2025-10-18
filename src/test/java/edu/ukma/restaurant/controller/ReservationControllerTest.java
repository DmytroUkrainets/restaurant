package edu.ukma.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ukma.restaurant.dto.ReservationDto;
import edu.ukma.restaurant.exception.GlobalExceptionHandler;
import edu.ukma.restaurant.exception.NotFoundException;
import edu.ukma.restaurant.security.JwtAuthFilter;
import edu.ukma.restaurant.security.JwtUtil;
import edu.ukma.restaurant.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @MockBean
    private ReservationService service;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void create_validationOk_returnsDto() throws Exception {
        var in = ReservationDto.builder()
            .tableId(1L)
            .customerName("John")
            .startTime(LocalDateTime.parse("2025-10-17T18:00:00"))
            .endTime(LocalDateTime.parse("2025-10-17T19:00:00"))
            .build();

        var out = ReservationDto.builder().id(1L).build();
        when(service.create(any())).thenReturn(out);

        mvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void get_notFound_isMappedTo404ByAdvice() throws Exception {
        when(service.findById(77L)).thenThrow(new NotFoundException("Reservation not found: id=77"));

        mvc.perform(get("/api/reservations/77"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Reservation not found: id=77"))
            .andExpect(jsonPath("$.path").value("/api/reservations/77"));
    }

    @Test
    void create_badRequest_whenMissingRequiredFields() throws Exception {
        var invalid = ReservationDto.builder().build();

        mvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest());
    }
}
