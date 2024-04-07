package org.example.search.filters;

import org.example.search.utils.Filter;
import org.example.search.utils.QueryOperator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HotelFilterBuilder {

    public List<Filter> createFilter(Long id, String hotelName, String cityName, String address, String header, Float remotion, Float rating, Integer ratesCount) {

        List<Filter> filterList = new ArrayList<>();
        String adrs = "";
        if (address != null) {
             String simpleAddress = address.replace(",", " ");

            String[] partAddress = simpleAddress.split(" ");

            adrs = (partAddress.length < 3) ? partAddress[0] : partAddress[1];
        }

        if (hotelName != null) {
            filterList.add(Filter.builder().field("hotelName").operator(QueryOperator.EQUALS).value(hotelName.toUpperCase()).build());
        }

        if (cityName != null) {
            filterList.add(Filter.builder().field("city").operator(QueryOperator.LIKE_CITY).value(cityName.toUpperCase()).build());
        }

        if (!adrs.isEmpty()) {
            filterList.add(Filter.builder().field("address").operator(QueryOperator.LIKE).value(adrs.toUpperCase()).build());
        }

        if (id != null) {
            filterList.add(Filter.builder().field("id").operator(QueryOperator.EQUALS_NUMBER).value(id.toString()).build());
        }

        if (header != null) {
            filterList.add(Filter.builder().field("header").operator(QueryOperator.LIKE).value(header.toUpperCase()).build());
        }

        if (remotion != null) {
            filterList.add(Filter.builder().field("remotion").operator(QueryOperator.NOT_GREATER).value(remotion.toString()).build());
        }

        if (rating != null) {
            filterList.add(Filter.builder().field("rating").operator(QueryOperator.GREATER).value(rating.toString()).build());
        }

        if (ratesCount != null) {
            filterList.add(Filter.builder().field("ratesCount").operator(QueryOperator.GREATER).value(ratesCount.toString()).build());
        }

        return filterList;
    }

}
