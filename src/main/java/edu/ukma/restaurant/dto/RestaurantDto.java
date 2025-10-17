package edu.ukma.restaurant.dto;

import edu.ukma.restaurant.domain.RestaurantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String address;

    @Size(max = 32)
    private String phone;

    private RestaurantStatus status;
}