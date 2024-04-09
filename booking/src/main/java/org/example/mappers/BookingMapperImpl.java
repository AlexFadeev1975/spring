package org.example.mappers;

import org.example.dto.BookingDtoResponse;
import org.example.model.Booking;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class BookingMapperImpl implements BookingMapper{
    @Override
    public BookingDtoResponse mapBookingToBookingDto(Booking booking) {

        BookingDtoResponse dto = new BookingDtoResponse();

        dto.setId(booking.getId());
      dto.setUser(booking.getUser());
     dto.setRoom(booking.getRoom());
        dto.setArrivalDate(booking.getArrivalDate());
        dto.setLeavingDate(booking.getLeavingDate());

        return dto;
    }
}
