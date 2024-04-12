package org.example.mappers;

import lombok.RequiredArgsConstructor;
import org.example.dto.HotelDtoRequest;
import org.example.model.City;
import org.example.model.Hotel;
import org.example.services.CityService;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CityMapper {

    private final CityService cityService;


    public City map(String city) {

        String[] splitCity = city.split(";");
        return cityService.searchCity(splitCity[0], splitCity[1]);

    }
}
