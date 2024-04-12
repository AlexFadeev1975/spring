package org.example.dto;

import lombok.Data;

@Data
public class HotelDtoResponse {

    private Long id;

    private String hotelName;

    private String header;

    private String city;

    private String address;

    private Float remotion;

    private Float rating;

    private Integer ratesCount;

}
