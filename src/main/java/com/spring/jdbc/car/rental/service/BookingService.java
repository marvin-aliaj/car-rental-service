package com.spring.jdbc.car.rental.service;

import com.spring.jdbc.car.rental.dao.BookingDao;
import com.spring.jdbc.car.rental.exception.CustomException;
import com.spring.jdbc.car.rental.model.Booking;
import com.spring.jdbc.car.rental.model.filter.BookingFilter;
import com.spring.jdbc.car.rental.model.filter.CarFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final BookingDao bookingDao;
    private final CarService carService;

    public BookingService(BookingDao bookingDao, CarService carService) {
        this.bookingDao = bookingDao;
        this.carService = carService;
    }

    public void createBooking(Booking booking) throws Exception {
        try {
            boolean isAvailable = carService.checkIfCarIsAvailable(booking.getCarId(), booking.getStartDate(), booking.getEndDate());
            if (isAvailable) {
                bookingDao.createBooking(booking);
            } else {
                throw new CustomException("Car is not available at these dates");
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public List<Booking> getBookings(BookingFilter filter) throws Exception {
        try {
            return bookingDao.getBookings(filter);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
}
