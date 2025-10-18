package edu.ukma.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ukma.restaurant.dto.DiningTableDto;
import edu.ukma.restaurant.security.JwtAuthFilter;
import edu.ukma.restaurant.security.JwtUtil;
import edu.ukma.restaurant.service.DiningTableService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiningTableController.class)
@AutoConfigureMockMvc(addFilters = false)
class DiningTableControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @MockBean
    private DiningTableService service;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void available_endpoint_parsesIsoDatesAndReturnsList() throws Exception {
        when(service.findAvailable(any(), any(), anyInt())).thenReturn(List.of(
            DiningTableDto.builder().id(2L).code("T2").capacity(4).active(true).build()
        ));

        mvc.perform(get("/api/tables/available")
                .param("start", "2025-10-17T18:00:00")
                .param("end", "2025-10-17T20:00:00")
                .param("partySize", "3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void create_table_returnsDto() throws Exception {
        var in = DiningTableDto.builder().code("T9").capacity(2).active(true).build();
        var out = DiningTableDto.builder().id(9L).code("T9").capacity(2).active(true).build();
        when(service.create(any())).thenReturn(out);

        mvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(9));
    }
}
