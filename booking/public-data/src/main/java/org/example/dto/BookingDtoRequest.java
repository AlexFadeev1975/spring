package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoRequest {

    Long roomId;
    @NotNull(message = "Поле Дата заезда не может быть пустым")
    LocalDate arrivalDate;
    @NotNull(message = "Поле Дата отъезда не может быть пустым")
    LocalDate leavingDate;
}
