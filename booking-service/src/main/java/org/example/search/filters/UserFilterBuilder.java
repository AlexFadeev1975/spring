package org.example.search.filters;

import org.example.search.utils.Filter;
import org.example.search.utils.QueryOperator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFilterBuilder {
    public List<Filter> createFilter(String userName, String email) {

        List<Filter> filterList = new ArrayList<>();


        if (!userName.isEmpty()) {
            filterList.add(Filter.builder().field("userName").operator(QueryOperator.EQUALS).value(userName.toUpperCase()).build());
        }

        if (!email.isEmpty()) {
            filterList.add(Filter.builder().field("email").operator(QueryOperator.EQUALS).value(email.toUpperCase()).build());
        }
        return filterList;
    }

}
