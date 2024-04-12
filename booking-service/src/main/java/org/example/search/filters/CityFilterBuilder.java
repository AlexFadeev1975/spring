package org.example.search.filters;

import org.example.search.utils.Filter;
import org.example.search.utils.QueryOperator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CityFilterBuilder {

    public List<Filter> createFilter(String city, String district) {

        List<Filter> filterList = new ArrayList<>();


        if (city != null) {
            filterList.add(Filter.builder().field("cityName").operator(QueryOperator.LIKE).value(city.toUpperCase()).build());
        }

        if (district != null) {
            filterList.add(Filter.builder().field("district").operator(QueryOperator.LIKE).value(district.toUpperCase()).build());
        }
        return filterList;
    }
}
