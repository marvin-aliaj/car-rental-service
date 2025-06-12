package com.spring.jdbc.car.rental.dao;

import com.spring.jdbc.car.rental.model.Booking;
import com.spring.jdbc.car.rental.model.Car;
import com.spring.jdbc.car.rental.model.enums.BOOKING_STATUS;
import com.spring.jdbc.car.rental.model.filter.BookingFilter;
import com.spring.jdbc.car.rental.model.filter.CarFilter;
import com.spring.jdbc.car.rental.service.BookingService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingDao {
    private final JdbcTemplate jdbcTemplate;

    public BookingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createBooking(Booking booking) throws Exception {
        String sql = "Insert into booking(startdate, enddate, carid, location, customerName, customerEmail, customerPhone, calculatedprice, status, cDate, mDate) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); // "id" is the name of the primary key column
                ps.setString(1, booking.getStartDate());
                ps.setString(2, booking.getEndDate());
                ps.setLong(3, booking.getCarId());
                ps.setString(4, booking.getLocation());
                ps.setString(5, booking.getCustomerName());
                ps.setString(6, booking.getCustomerEmail());
                ps.setString(7, booking.getCustomerPhone());
                ps.setInt(8, booking.getCalculatedPrice()); // Saving the amount in cents
                ps.setString(9, BOOKING_STATUS.COMPLETED.getDescription());
                ps.setObject(10, LocalDateTime.now());
                ps.setObject(11, LocalDateTime.now());
                return ps;
            }, keyHolder);

            booking.setId(keyHolder.getKey().longValue());

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Booking> getBookings(BookingFilter filter) throws Exception {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("Select booking.*" +
                ", car.brand as brand " +
                ", car.model as model " +
                " from booking " +
                " left join car on booking.carid = car.id " +
                " WHERE 1=1 ");
        if (filter.hasDateFiltersSet()) {
            sql.append(" and booking.startdate BETWEEN ? AND ?" +
                    " OR booking.enddate BETWEEN ? AND ? ");
            params.add(filter.getStartDate());
            params.add(filter.getEndDate());
            params.add(filter.getStartDate());
            params.add(filter.getEndDate());
        }
        String string_sql = sql.toString();
        try {
            return jdbcTemplate.query(string_sql, (rs, rowNum) -> {
                Booking booking = new Booking();
                booking.setId(rs.getLong("id"));
                booking.setStartDate(rs.getString("startdate"));
                booking.setEndDate(rs.getString("enddate"));
                Car car = new Car();
                car.setId(rs.getLong("carid"));
                car.setModel(rs.getString("model"));
                car.setBrand(rs.getString("brand"));
                booking.setCar(car);
                booking.setCustomerName(rs.getString("customerName"));
                booking.setLocation(rs.getString("location"));
                booking.setCustomerEmail(rs.getString("customerEmail"));
                booking.setCustomerPhone(rs.getString("customerPhone"));
                booking.setCalculatedPrice(rs.getInt("calculatedprice"));
                booking.setStatus(BOOKING_STATUS.fromDescription(rs.getString("status")));

                return booking;
            }, params.toArray());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
