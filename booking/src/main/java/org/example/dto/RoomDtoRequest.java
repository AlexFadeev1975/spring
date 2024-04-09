package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RoomDtoRequest {

    Long id;

    @NotBlank (message = "Поле Название комнаты не может быть пустым")
    private String roomName;

    private String description;

    @NotNull (message = "Поле Номер комнаты не может быть пустым")
    private Integer number;

    private Float price;

    private Float maxPrice;

    private Float minPrice;

    private LocalDate arrivalDate;

    private LocalDate leavingDate;

    @JsonProperty(defaultValue = "1")
    private Integer page;
    @JsonProperty (defaultValue = "5")
    private Integer size;


    private Integer population;
    @NotNull(message = "Поле Id отеля не может быть пустым")
    private Long hotelId;

}
