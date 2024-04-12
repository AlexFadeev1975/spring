package org.example.mappers;

import org.example.dto.BookingDtoResponse;
import org.example.model.Booking;
import org.example.model.Room;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    @Mapping(target = "userId", expression = "java(booking.getUser().getId().toString())")
    @Mapping(target = "room", expression = "java(booking.getRoom().toString())")
    BookingDtoResponse mapBookingToBookingDto(Booking booking);


}
