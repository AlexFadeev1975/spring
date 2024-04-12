package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Hotel;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RoomDtoResponse {

    private Long Id;

    private String roomName;

    private String description;

    private Integer number;

    private Float price;

    private Integer population;

    private List<LocalDate> closedDates;

    private String hotelName;
}
