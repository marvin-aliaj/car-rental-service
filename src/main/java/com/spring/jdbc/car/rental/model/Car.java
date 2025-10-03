package com.spring.jdbc.car.rental.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Car {
    private long id;
    private String brand;
    private String model;
    private short year;
    private String transmission;
    private String engine;
    private String fuelType;
    private short seats;
    private short bags;
    private float lkm;  // liters per kilometer
    private int pricePerDay;  // price in cents
    private String features;
    private String imageUrl;
    private String description;
    private float reviewStars;
    private short reviewerCount;
}
