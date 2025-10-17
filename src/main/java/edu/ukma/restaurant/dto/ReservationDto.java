package edu.ukma.restaurant.dto;

import edu.ukma.restaurant.domain.ReservationStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long id;

    @NotNull
    private Long tableId;

    @NotBlank
    @Size(max = 128)
    private String customerName;

    @Size(max = 32)
    private String customerPhone;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private ReservationStatus status;
}