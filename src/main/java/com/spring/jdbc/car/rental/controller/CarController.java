package com.spring.jdbc.car.rental.controller;

import com.spring.jdbc.car.rental.exception.ForbiddenException;
import com.spring.jdbc.car.rental.model.Car;
import com.spring.jdbc.car.rental.model.filter.CarFilter;
import com.spring.jdbc.car.rental.service.AuthenticationService;
import com.spring.jdbc.car.rental.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.spring.jdbc.car.rental.model.enums.CONSTANT_VARIABLES.ENTITY_MINUS_ONE;

@CrossOrigin(origins = "*")
@RestController
public class CarController {
    private final CarService carService;
    private final AuthenticationService authService;

    public CarController(CarService carService, AuthenticationService authService) {
        this.carService = carService;
        this.authService = authService;
    }

    @GetMapping("/cars")
    public ResponseEntity<Object> getAccounts(
            @RequestHeader(value = "x-api-key") String apiKey
            , @RequestParam(value = "brand", defaultValue = "") String brand
            , @RequestParam(value = "model", defaultValue = "") String model
            , @RequestParam(value = "transmission", defaultValue = "") String transmission
            , @RequestParam(value = "fuelType", defaultValue = "") String fuelType
            , @RequestParam(value = "seats", defaultValue = ENTITY_MINUS_ONE + "") short seats
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
    ) {
        try {
            if (!authService.hasAccess(apiKey)) {
                throw new ForbiddenException("Action not allowed");
            }
            CarFilter filter = new CarFilter();
            filter.setBrand(brand);
            filter.setModel(model);
            filter.setTransmission(transmission);
            filter.setFuelType(fuelType);
            filter.setSeats(seats);
            filter.setStartDate(startDate);
            filter.setEndDate(endDate);
            List<Car> cars = new ArrayList<>(carService.getCars(filter));

            if (cars.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>("Action is not allowed!", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/cars")
    public ResponseEntity<Object> createAccount(
            @RequestHeader(value = "Authorization") String authorizationHeader
            , @RequestHeader(value = "x-api-key") String apiKey
            , @RequestBody Car car) {
        try {
            if (!authService.hasAccess(apiKey)) {
                throw new ForbiddenException("Action not allowed");
            }
            if (!authService.validateToken(authorizationHeader)) {
                throw new ForbiddenException("You don't have permission");
            }
            carService.createCar(car);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
//    @PutMapping("/accounts/{id}")
//    public ResponseEntity<Object> updateAccount(
//            @RequestHeader(value = "Authorization") String authorizationHeader,
//            @PathVariable long id,
//            @RequestBody Account account) {
//        try {
//            if (account.getId() != id) {
//                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
//            }
//            User currentUser = authService.validateTokenAndReturnUser(authorizationHeader, true);
//            if (currentUser == null) {
//                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//            }
//            carService.updateAccount(account, currentUser);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
