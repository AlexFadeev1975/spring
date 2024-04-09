package org.example.search.filters;

import org.example.search.utils.Filter;
import org.example.search.utils.QueryOperator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoomFilterBuilder {

    public List<Filter> createFilter(Long id, String roomName, Integer number, Long hotelId, Float maxPrice, Float minPrice, LocalDate arrivalDate, LocalDate leavingDate) {

        List<Filter> filterList = new ArrayList<>();


        if (roomName != null ) {
            filterList.add(Filter.builder().field("roomName").operator(QueryOperator.EQUALS).value(roomName.toUpperCase()).build());
        }

        if (number != null) {
            filterList.add(Filter.builder().field("number").operator(QueryOperator.EQUALS_NUMBER).value(number.toString()).build());
        }

        if (hotelId != null) {
            filterList.add(Filter.builder().field("hotel").operator(QueryOperator.EQUALS_HOTEL).value(hotelId.toString()).build());
        }

        if (id != null) {
            filterList.add(Filter.builder().field("id").operator(QueryOperator.EQUALS_NUMBER).value(id.toString()).build());
        }

        if (maxPrice != null) {
            filterList.add(Filter.builder().field("price").operator(QueryOperator.NOT_GREATER).value(maxPrice.toString()).build());
        }
        if (minPrice != null) {
            filterList.add(Filter.builder().field("price").operator(QueryOperator.GREATER).value(minPrice.toString()).build());
        }

        if (arrivalDate != null & leavingDate != null) {

            arrivalDate.datesUntil(leavingDate.minusDays(1))
                    .forEach(d ->
                    {
                        filterList.add(Filter.builder().field("booking").operator(QueryOperator.NOT_BETWEEN_BOOKING_DATE).startDate("arrivalDate").endDate("leavingDate").date(d).build());

                    });

        }
        return filterList; }


}
