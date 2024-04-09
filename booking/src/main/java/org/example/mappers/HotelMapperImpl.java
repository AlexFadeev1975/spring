package org.example.mappers;

import lombok.RequiredArgsConstructor;
import org.example.dto.HotelDtoRequest;
import org.example.dto.HotelDtoResponse;
import org.example.model.Hotel;
import org.example.services.CityService;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
@RequiredArgsConstructor
public class HotelMapperImpl implements HotelMapper {

    private final CityService cityService;

    @Override
    public HotelDtoResponse mapHotelToDto(Hotel hotel) {

        HotelDtoResponse dto = new HotelDtoResponse();
        dto.setId(hotel.getId());
        dto.setCity(hotel.getCity().toString());
        dto.setName(hotel.getHotelName());
        dto.setHeader(hotel.getHeader());
        dto.setAddress(hotel.getAddress());
        dto.setRating(hotel.getRating());
        dto.setRemotion(hotel.getRemotion());
        dto.setRatesCount(hotel.getRatesCount());
        return dto;
    }

    @Override
    public Hotel mapDtoToHotel(HotelDtoRequest request) {

        Hotel hotel = new Hotel();
        hotel.setId(request.getId());
        hotel.setAddress(request.getAddress());
        hotel.setHotelName(request.getName());
        hotel.setHeader(request.getHeader());
        hotel.setCity(cityService.searchCity(request.getCity(), request.getDistrict()));
        hotel.setRemotion(request.getRemotion());

        return hotel;
    }
}
