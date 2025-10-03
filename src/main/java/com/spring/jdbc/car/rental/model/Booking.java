package com.spring.jdbc.car.rental.model;

import com.spring.jdbc.car.rental.model.enums.BOOKING_STATUS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Booking {
    private long id;
    private String location;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String startDate;
    private String endDate;
    private long carId;
    private Car car;
    private int calculatedPrice;  // price in cents
    private BOOKING_STATUS status;
    private String cDate;
    private String mDate;
}
