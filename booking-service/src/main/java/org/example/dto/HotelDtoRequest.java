package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDtoRequest {

    private Long id;
    @NotBlank(message = "Поле Название отеля не может быть пустым")
    private String hotelName;

    private String header;
    @NotBlank(message = "Поле Город не может быть пустым")
    private String city;
    @NotBlank(message = "Поле Область, край не может быть пустым")
    private String district;
    @NotBlank(message = "Поле Адрес не может быть пустым")
    private String address;

    private Float remotion;

    private Float rating;

    private Integer mark;

    private Integer ratesCount;

    @JsonProperty(defaultValue = "1")
    private Integer page;
    @JsonProperty(defaultValue = "5")
    private Integer size;

    public String getCity() {

        return this.city + ";" + this.district;
    }
}
