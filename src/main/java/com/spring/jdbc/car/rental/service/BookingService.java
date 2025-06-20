package com.spring.jdbc.car.rental.service;

import com.spring.jdbc.car.rental.dao.BookingDao;
import com.spring.jdbc.car.rental.exception.CustomException;
import com.spring.jdbc.car.rental.model.Booking;
import com.spring.jdbc.car.rental.model.filter.BookingFilter;
import com.spring.jdbc.car.rental.model.filter.CarFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    private final BookingDao bookingDao;
    private final CarService carService;

    @Autowired
    private EmailSenderService senderService;
    public BookingService(BookingDao bookingDao, CarService carService) {
        this.bookingDao = bookingDao;
        this.carService = carService;
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void createBooking(Booking booking) throws Exception {
        try {
            boolean isAvailable = carService.checkIfCarIsAvailable(booking.getCarId(), booking.getStartDate(), booking.getEndDate());
            if (isAvailable) {
                bookingDao.createBooking(booking);

                Map<String, String> vars = Map.of(
                        "customerName", booking.getCustomerName (),
                        "customerPhone", booking.getCustomerPhone (),
                        "carId", String.valueOf (booking.getCarId ()),
                        "location", booking.getLocation (),
                        "pickupDate", booking.getStartDate (),
                        "returnDate", booking.getEndDate ()
                );
                senderService.sendSimpleEmail(vars,
                        "goldcarsalbania0@gmail.com",
                        "Booking Confirmation");
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
