package org.example.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.dto.BookingDtoRequest;
import org.example.dto.BookingDtoResponse;
import org.example.exceptions.SuchItemExistException;
import org.example.model.Booking;
import org.example.model.Room;
import org.example.model.User;
import org.example.model.kafka.BookingMessage;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import org.example.repository.UserRepository;
import org.example.search.filters.BookingFilterBuilder;
import org.example.search.utils.Filter;
import org.example.search.utils.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@Service
public class BookingService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingFilterBuilder bookingFilterBuilder;
    private final SpecificationBuilder<Booking> specificationBuilder;

    private final org.example.mappers.BookingMapper mapper;
    @Value(value = "${spring.kafka.kafkaBookingMessageTopic}")
    private String staticTopic;

    private final KafkaTemplate<String, BookingMessage> kafkaTemplate;


    @Transactional
    public BookingDtoResponse bookingRoom(BookingDtoRequest dto) {

        List<Booking> listBookingIsExist = new ArrayList<>();
        dto.getArrivalDate().datesUntil(dto.getLeavingDate().minusDays(1))
                .forEach(d -> {
                    List<Booking> bookingList = searchBooking(dto.getRoomId(), d);
                    listBookingIsExist.addAll(bookingList);
                });

        if (listBookingIsExist.isEmpty()) {
            Booking booking = new Booking();
            booking.setUser(getUserFromPrincipal());
            booking.setRoom(findRoomById(dto.getRoomId()));
            booking.setArrivalDate(dto.getArrivalDate());
            booking.setLeavingDate(dto.getLeavingDate());

            Booking savedBooking = bookingRepository.save(booking);


            return mapper.mapBookingToBookingDto(savedBooking);


        } else throw new SuchItemExistException("На указанный период комната уже забронирована");
    }


    public List<BookingDtoResponse> findAllByUser() {

        User user = getUserFromPrincipal();
        List<Filter> filters = bookingFilterBuilder.createFilter(null, null, user.getId());
        Specification<Booking> specification = specificationBuilder.getSpecificationFromFilters(filters);

        return bookingRepository.findAll(specification).stream().map(mapper::mapBookingToBookingDto).toList();
    }

    public List<BookingDtoResponse> findAllByRoom(Long roomId) {

        List<Filter> filters = bookingFilterBuilder.createFilter(roomId, null, null);
        Specification<Booking> specification = specificationBuilder.getSpecificationFromFilters(filters);

        return bookingRepository.findAll(specification).stream().map(mapper::mapBookingToBookingDto).toList();

    }

    public List<BookingDtoResponse> findAllBooking() {

        return bookingRepository.findAll().stream().map(mapper::mapBookingToBookingDto).toList();

    }

    public List<Booking> searchBooking(Long roomId, LocalDate currentDate) {

        List<Filter> filters = bookingFilterBuilder.createFilter(roomId, currentDate, null);

        Specification<Booking> specification = specificationBuilder.getSpecificationFromFilters(filters);

        return bookingRepository.findAll(specification);
    }

    private User getUserFromPrincipal() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        var authentication = (Authentication) request.getUserPrincipal();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("Пользователь с таким именем не существует"));


    }

    private Room findRoomById(Long roomId) {

        return roomRepository.findById(roomId).orElseThrow(() -> new NoSuchElementException("Комнаты с таким id не существует"));

    }


}
