package org.example.search;

import jakarta.persistence.criteria.Join;
import org.example.model.Category;
import org.example.model.News;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecificationBuilder<T> {

    public Specification<T> getSpecificationFromFilters(List<Filter> filter) {

        Specification<T> specification =
                Specification.where(createSpecification(filter.remove(0)));
        for (Filter input : filter) {

            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private Specification<T> createSpecification(Filter input) {
        return switch (input.getOperator()) {
            /* создание спецификаций с выбранными условиями поиска */
            case EQUALS_CATEGORY -> (root, query, criteriaBuilder) -> {
                Join<News, Category> newsCategory = root.join("category");
                return criteriaBuilder.equal(newsCategory.get("categoryName"), input.getValue());
            };

            case IN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.in(root.get(input.getField()))
                            .value(castToRequiredTypes(
                                    root.get(input.getField()).getJavaType(),
                                    input.getValues()));

            default -> throw new RuntimeException("Operator not found");
        };
    }

    private Object castToRequiredType(Class fieldType, Object value) {


        if (fieldType.isAssignableFrom(Long.class)) {
            return Long.valueOf((String) value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf((String) value);
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, (String) value);
        }
        return value;
    }

    private Object castToRequiredTypes(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }
}

