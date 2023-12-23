package org.example.search;

import org.example.dto.NewsSearchDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class NewsFilterBuilder {

    public List<Filter> createFilter(NewsSearchDto dto, List<Long> userIds) {

        List<Filter> filterList = new ArrayList<>();

        /* создание фильтра поиска по АВТОРУ */
        if (!userIds.isEmpty()) {

            filterList.add(Filter.builder().field("userId").operator(QueryOperator.IN).values(userIds.stream().map(Objects::toString).toList()).build());
        }

        if (dto.getCategoryName() != null) {
            filterList.add(Filter.builder().field("category").operator(QueryOperator.EQUALS_CATEGORY).value(dto.getCategoryName()).build());
        }

        return filterList;
    }
}
