package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate arrivalDate;
    @NotNull(message = "Поле Дата отъезда не может быть пустым")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate leavingDate;
}
