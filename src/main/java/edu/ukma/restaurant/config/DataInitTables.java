package edu.ukma.restaurant.config;

import edu.ukma.restaurant.dto.DiningTableDto;
import edu.ukma.restaurant.service.DiningTableService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitTables {
    @Bean
    CommandLineRunner seedTables(DiningTableService service) {
        return args -> {
            if (service.findAll().isEmpty()) {
                service.create(DiningTableDto.builder().code("T1").capacity(2).active(true).build());
                service.create(DiningTableDto.builder().code("T2").capacity(4).active(true).build());
                service.create(DiningTableDto.builder().code("VIP-1").capacity(6).active(true).build());
            }
        };
    }
}
