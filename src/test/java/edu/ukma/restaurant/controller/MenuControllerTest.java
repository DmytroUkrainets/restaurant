package edu.ukma.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ukma.restaurant.dto.MenuDto;
import edu.ukma.restaurant.dto.MenuItemDto;
import edu.ukma.restaurant.security.JwtAuthFilter;
import edu.ukma.restaurant.security.JwtUtil;
import edu.ukma.restaurant.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @MockBean
    private MenuService service;
    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void createMenu_returnsDto() throws Exception {
        var in = new MenuDto(); in.setName("Main"); in.setRestaurantId(1L);
        var out = new MenuDto(); out.setId(10L); out.setName("Main"); out.setRestaurantId(1L);
        when(service.createMenu(any())).thenReturn(out);

        mvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void listMenusByRestaurant_returnsArray() throws Exception {
        var dto = new MenuDto(); dto.setId(11L); dto.setRestaurantId(1L); dto.setName("A");
        when(service.findMenusByRestaurant(1L)).thenReturn(List.of(dto));

        mvc.perform(get("/api/menus").param("restaurantId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(11));
    }

    @Test
    void addItem_returnsDto() throws Exception {
        var in = new MenuItemDto(); in.setName("Soup"); in.setPrice(new BigDecimal("9.99")); in.setAvailable(true);
        var out = new MenuItemDto(); out.setId(5L); out.setName("Soup");
        when(service.addItem(eq(7L), any())).thenReturn(out);

        mvc.perform(post("/api/menus/7/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void listItems_returnsArray() throws Exception {
        var i = new MenuItemDto(); i.setId(1L); i.setName("Tea");
        when(service.listItems(3L)).thenReturn(List.of(i));

        mvc.perform(get("/api/menus/3/items"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Tea"));
    }
}
