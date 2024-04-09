package org.example.mappers;

import org.example.dto.BookingDtoResponse;
import org.example.model.Booking;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDtoResponse mapBookingToBookingDto (Booking booking);
}
