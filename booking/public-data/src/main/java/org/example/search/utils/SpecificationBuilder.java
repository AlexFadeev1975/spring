package org.example.search.utils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.model.City;
import org.example.model.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

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

            case LIKE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.upper(
                            root.get(input.getField())),
                            "%" + input.getValue() + "%");
            case EQUALS -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.upper(root.get(input.getField())),
                            castToRequiredType(root.get(input.getField()).getJavaType(),
                                    input.getValue()));
            case EQUALS_NUMBER -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(input.getField()),
                            castToRequiredType(root.get(input.getField()).getJavaType(),
                                    input.getValue()));
            case LIKE_CITY -> (root, query, criteriaBuilder) -> {
                Join<City, Hotel> hotelCity = root.join(input.getField());
                    return criteriaBuilder.like(criteriaBuilder.upper(hotelCity.get("cityName")),
                                    "%" +input.getValue() + "%"); };
            case EQUALS_HOTEL,  EQUALS_USER, EQUALS_ROOM -> (root, query, criteriaBuilder) -> {
                Join<?,?> item = root.join(input.getField());
                return criteriaBuilder.equal(item.get("id"),
                        castToRequiredType(root.get("id").getJavaType(),
                                input.getValue())); };
            case BETWEEN_DATE-> (root, query, criteriaBuilder) -> {
                Predicate startFrom = criteriaBuilder.greaterThanOrEqualTo(root.get(input.getStartDate()), input.getDate());
                Predicate endTo = criteriaBuilder.lessThan(root.get(input.getEndDate()), input.getDate());
                return criteriaBuilder.and(startFrom, endTo);
                  };
            case NOT_BETWEEN_BOOKING_DATE-> (root, query, criteriaBuilder) -> {
                Join<?,?> booking = root.join(input.getField());
                Predicate startFrom = criteriaBuilder.greaterThanOrEqualTo(booking.get(input.getStartDate()), input.getDate());
                Predicate endTo = criteriaBuilder.lessThan(root.get(input.getEndDate()), input.getDate());
                return criteriaBuilder.not(criteriaBuilder.and(startFrom, endTo));
            };
            case NOT_GREATER -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.not(criteriaBuilder.gt(root.get(input.getField()),
                            (Number) castToRequiredType(
                                    root.get(input.getField()).getJavaType(),
                                    input.getValue())));
            case GREATER -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.gt(root.get(input.getField()),
                            (Number) castToRequiredType(
                                    root.get(input.getField()).getJavaType(),
                                    input.getValue()));




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

}

