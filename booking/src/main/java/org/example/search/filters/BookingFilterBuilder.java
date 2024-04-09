package org.example.search.filters;

import org.example.search.utils.Filter;
import org.example.search.utils.QueryOperator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Component
public class BookingFilterBuilder {
    public List<Filter> createFilter(Long roomId, LocalDate currentDate, Long userId) {

        List<Filter> filterList = new ArrayList<>();


        if (roomId != null) {
            filterList.add(Filter.builder().field("room").operator(QueryOperator.EQUALS_ROOM).value(roomId.toString()).build());
        }

        if (currentDate != null) {
            filterList.add(Filter.builder().startDate("arrivalDate")
                    .endDate("leavingDate")
                    .operator(QueryOperator.BETWEEN_DATE)
                    .date(currentDate).build());
        }

        if (userId != null) {
            filterList.add(Filter.builder().field("user").operator(QueryOperator.EQUALS_USER).value(userId.toString()).build());
        }

        return filterList;
    }
}
