package edu.ukma.restaurant.config;

import edu.ukma.restaurant.domain.RestaurantStatus;
import edu.ukma.restaurant.dto.RestaurantDto;
import edu.ukma.restaurant.service.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInit {
    @Bean
    CommandLineRunner seed(RestaurantService service) {
        return args -> {
            if (service.findAll().isEmpty()) {
                service.create(RestaurantDto.builder()
                    .name("Restaurant 1")
                    .address("Address 1")
                    .phone("+380441112233")
                    .status(RestaurantStatus.CLOSED)
                    .build());
                service.create(RestaurantDto.builder()
                    .name("Restaurant 2")
                    .address("Address 2")
                    .phone("+380322223344")
                    .status(RestaurantStatus.OPEN)
                    .build());
            }
        };
    }
}