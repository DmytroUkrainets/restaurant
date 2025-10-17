package edu.ukma.restaurant.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiningTableDto {
    private Long id;

    @NotBlank
    @Size(max = 64)
    private String code;

    @Min(1)
    private int capacity;

    private boolean active;
}