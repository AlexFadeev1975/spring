package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.HotelDtoRequest;
import org.example.dto.HotelDtoResponse;
import org.example.exceptions.SuchItemExistException;
import org.example.mappers.HotelMapper;
import org.example.model.City;
import org.example.model.Hotel;
import org.example.repository.HotelRepository;
import org.example.search.filters.HotelFilterBuilder;
import org.example.search.utils.Filter;
import org.example.search.utils.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    private final HotelMapper mapper;

    private final HotelFilterBuilder hotelFilterBuilder;

    private final SpecificationBuilder<Hotel> specificationBuilder;

    private final CityService cityService;

    public HotelDtoResponse findHotelById(Long id) {

        Hotel hotel = hotelRepository.findById(id).orElseThrow();

        return mapper.mapHotelToDto(hotel);

    }

    @Transactional
    public HotelDtoResponse createHotel(HotelDtoRequest request) {


        List<HotelDtoResponse> savedHotels = searchHotel(null, request.getHotelName(), request.getCity(), request.getAddress(), null, null, null, null, null);
        if (savedHotels.isEmpty()) {
            Hotel hotel = mapper.mapDtoToHotel(request);
            Hotel savedHotel = hotelRepository.save(hotel);
            return mapper.mapHotelToDto(savedHotel);
        } else throw new SuchItemExistException("Данный отель уже существует");
    }

    public HotelDtoResponse updateHotel(HotelDtoRequest request) {


        Hotel hotel = hotelRepository.findById(request.getId()).orElseThrow(() -> new NoSuchElementException("Отель не найден"));

        if (request.getCity() != null && request.getDistrict() != null) {
            City city = cityService.searchCity(request.getCity(), request.getDistrict());
            hotel.setCity(city);
            ;
        }
        if (request.getHeader() != null) {
            hotel.setHeader(request.getHeader());
        }
        if (request.getHotelName() != null) {
            hotel.setHotelName(request.getHotelName());
        }
        if (request.getAddress() != null) {
            hotel.setAddress(request.getAddress());
        }
        if (request.getRemotion() != null) {
            hotel.setRemotion(request.getRemotion());
        }

        return mapper.mapHotelToDto(hotelRepository.save(hotel));

    }

    public List<HotelDtoResponse> getAllHotels() {

        Pageable pageable = PageRequest.of(0, 20);

        return hotelRepository.findAll(pageable).getContent().stream().map(mapper::mapHotelToDto).toList();
    }

    public void deleteHotelById(Long id) {

        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Отель не найден"));

        hotelRepository.deleteById(hotel.getId());
    }

    public List<HotelDtoResponse> searchHotel(Long id, String hotelName, String cityName, String address, String header, Float remotion, Float rating, Integer ratesCount, Pageable pageable) {

        List<Filter> filters = hotelFilterBuilder.createFilter(id, hotelName, cityName, address, header, remotion, rating, ratesCount);
        Specification<Hotel> specification = specificationBuilder.getSpecificationFromFilters(filters);
        if (pageable == null) {
            return hotelRepository.findAll(specification).stream().map(mapper::mapHotelToDto).toList();
        } else
            return hotelRepository.findAll(specification, pageable).getContent().stream().map(mapper::mapHotelToDto).toList();
    }

    public HotelDtoResponse changeHotelRating(HotelDtoRequest dto) throws ParseException {

        Hotel hotel = hotelRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException("Отель не найден"));

        Float rating = (hotel.getRating() == null) ? dto.getMark() : hotel.getRating();

        Integer numberOfRating = (hotel.getRatesCount() == null) ? 1 : hotel.getRatesCount();

        Float totalRating = rating * numberOfRating;

        totalRating = totalRating - rating + dto.getMark();

        rating = totalRating / numberOfRating;

        numberOfRating = numberOfRating + 1;

        hotel.setRating(rating);
        hotel.setRatesCount(numberOfRating);

        return mapper.mapHotelToDto(hotelRepository.save(hotel));
    }
}
