package com.spring.jdbc.car.rental.controller;

import com.spring.jdbc.car.rental.exception.CustomException;
import com.spring.jdbc.car.rental.exception.ForbiddenException;
import com.spring.jdbc.car.rental.model.Booking;
import com.spring.jdbc.car.rental.model.Car;
import com.spring.jdbc.car.rental.model.filter.BookingFilter;
import com.spring.jdbc.car.rental.model.filter.CarFilter;
import com.spring.jdbc.car.rental.service.AuthenticationService;
import com.spring.jdbc.car.rental.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.spring.jdbc.car.rental.model.enums.CONSTANT_VARIABLES.ENTITY_MINUS_ONE;

@CrossOrigin(origins = "*")
@RestController
public class BookingController {
    private final BookingService bookingService;
    private final AuthenticationService authService;

    public BookingController(BookingService bookingService, AuthenticationService authService) {
        this.bookingService = bookingService;
        this.authService = authService;
    }

    @PostMapping("/bookings")
    public ResponseEntity<Object> createBooking(
            @RequestHeader(value = "x-api-key") String apiKey
            , @RequestBody Booking booking) {
        try {
            if (!authService.hasAccess(apiKey)) {
                throw new ForbiddenException("Action not allowed");
            }
            bookingService.createBooking(booking);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<Object> getAccounts(
            @RequestHeader(value = "Authorization") String authorizationHeader
            , @RequestHeader(value = "x-api-key") String apiKey
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
    ) {
        try {
            if (!authService.hasAccess(apiKey)) {
                throw new ForbiddenException("Action not allowed");
            }
            if (!authService.validateToken(authorizationHeader)) {
                throw new ForbiddenException("You don't have permission");
            }
            BookingFilter filter = new BookingFilter();
            filter.setStartDate(startDate);
            filter.setEndDate(endDate);
            List<Booking> bookings = bookingService.getBookings(filter);

            if (bookings.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>("Action is not allowed!", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
