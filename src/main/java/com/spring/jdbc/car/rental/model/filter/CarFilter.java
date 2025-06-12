package com.spring.jdbc.car.rental.model.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.spring.jdbc.car.rental.model.enums.CONSTANT_VARIABLES.ENTITY_MINUS_ONE;

@Getter
@Setter
@RequiredArgsConstructor
public class CarFilter extends Filter {
    private String brand;
    private String model;
    private String transmission;
    private String fuelType;
    private String startDate;
    private String endDate;
    private long carId = ENTITY_MINUS_ONE;
    private short seats = (short) ENTITY_MINUS_ONE;

    public boolean hasBrandSet() {
        return brand != null && !brand.isEmpty();
    }

    public boolean hasModelSet() {
        return model != null && !model.isEmpty();
    }

    public boolean hasTransmissionSet() {
        return transmission != null && !transmission.isEmpty();
    }

    public boolean hasFuelTypeSet() {
        return fuelType != null && !fuelType.isEmpty();
    }

    public boolean hasSeatsSet() {
        return seats != (short) ENTITY_MINUS_ONE;
    }

    public boolean hasCarIdSet() {
        return carId != ENTITY_MINUS_ONE;
    }

    public boolean hasDateFiltersSet() {
        return startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty();
    }
}