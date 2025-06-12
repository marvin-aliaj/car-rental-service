package com.spring.jdbc.car.rental.model.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.spring.jdbc.car.rental.model.enums.CONSTANT_VARIABLES.ENTITY_MINUS_ONE;

@Getter
@Setter
@RequiredArgsConstructor
public class BookingFilter extends Filter {
    private String startDate;
    private String endDate;

    public boolean hasDateFiltersSet() {
        return startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty();
    }
}