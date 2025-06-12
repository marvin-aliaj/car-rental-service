package com.spring.jdbc.car.rental.dao;

import com.spring.jdbc.car.rental.exception.CustomException;
import com.spring.jdbc.car.rental.exception.ForbiddenException;
import com.spring.jdbc.car.rental.model.Booking;
import com.spring.jdbc.car.rental.model.Car;
import com.spring.jdbc.car.rental.model.enums.BOOKING_STATUS;
import com.spring.jdbc.car.rental.model.filter.CarFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CarDao {
    private final JdbcTemplate jdbcTemplate;

    public CarDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Car> getCars(CarFilter filter) throws Exception {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("Select car.id as id" +
                ", car.brand as brand" +
                ", car.model as model " +
                ", car.year as year " +
                ", car.transmission as transmission " +
                ", car.engine as engine " +
                ", car.fuelType as fuelType " +
                ", car.seats as seats " +
                ", car.bags as bags " +
                ", car.lkm as lkm " +
                ", car.pricePerDay as pricePerDay " +
                ", car.features as features " +
                ", car.imageUrl as imageUrl " +
                ", car.description as description " +
                ", car.reviewStars as reviewStars " +
                ", car.reviewerCount as reviewerCount " +
                " from car ");
        if (filter.hasDateFiltersSet()) {
            sql.append(" LEFT JOIN booking " +
                    "                   ON car.id = booking.carId " +
                    "                       AND booking.startDate < ? " +
                    "                       AND booking.endDate > ? " +
                    " WHERE booking.id IS NULL " +
                    " AND 1=1 "
            );
            params.add(filter.getEndDate());
            params.add(filter.getStartDate());
        } else {
            sql.append(" WHERE 1=1 ");
        }
        if (filter.hasCarIdSet()) {
            sql.append(" and car.id = ? ");
            params.add(filter.getCarId());
        }
        if (filter.hasBrandSet()) {
            sql.append(" and LOWER(car.brand) LIKE = ? ");
            params.add("%" + filter.getBrand().toLowerCase() + "%");
        }
        if (filter.hasModelSet()) {
            sql.append(" and LOWER(car.model) LIKE = ? ");
            params.add("%" + filter.getModel().toLowerCase() + "%");
        }
        if (filter.hasFuelTypeSet()) {
            sql.append(" and LOWER(car.fuelType) LIKE = ? ");
            params.add("%" + filter.getFuelType().toLowerCase() + "%");
        }
        if (filter.hasTransmissionSet()) {
            sql.append(" and LOWER(car.transmission) LIKE = ? ");
            params.add("%" + filter.getTransmission().toLowerCase() + "%");
        }
        if (filter.hasSeatsSet()) {
            sql.append(" and car.seats = ? ");
            params.add(filter.getSeats());
        }

        String string_sql = sql.toString();
        try {
            return jdbcTemplate.query(string_sql, (rs, rowNum) -> {
                Car car = new Car();
                car.setId(rs.getLong("id"));
                car.setBrand(rs.getString("brand"));
                car.setModel(rs.getString("model"));
                car.setYear(rs.getShort("year"));
                car.setTransmission(rs.getString("transmission"));
                car.setEngine(rs.getString("engine"));
                car.setFuelType(rs.getString("fuelType"));
                car.setSeats(rs.getShort("seats"));
                car.setBags(rs.getShort("bags"));
                car.setLkm(rs.getShort("lkm"));
                car.setPricePerDay(rs.getInt("pricePerDay") / 100);
                car.setFeatures(rs.getString("features"));
                car.setImageUrl(rs.getString("imageUrl"));
                car.setDescription(rs.getString("description"));
                car.setReviewStars(rs.getFloat("reviewStars"));
                car.setReviewerCount(rs.getShort("reviewerCount"));
                return car;
            }, params.toArray());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public void createCar(Car car) throws Exception {
        String sql = "Insert into car(description, brand, model, year, transmission, engine, fuelType, seats, bags, lkm, pricePerDay, features, imageUrl, reviewStars, reviewerCount) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); // "id" is the name of the primary key column
                ps.setString(1, car.getDescription());
                ps.setString(2, car.getBrand());
                ps.setString(3, car.getModel());
                ps.setInt(4, car.getYear());
                ps.setString(5, car.getTransmission());
                ps.setString(6, car.getEngine());
                ps.setString(7, car.getFuelType());
                ps.setInt(8, car.getSeats());
                ps.setInt(9, car.getBags());
                ps.setFloat(10, car.getLkm());
                ps.setInt(11, car.getPricePerDay() * 100);
                ps.setString(12, car.getFeatures());
                ps.setString(13, car.getImageUrl());
                ps.setFloat(14, car.getReviewStars());
                ps.setInt(15, car.getReviewerCount());
                return ps;
            }, keyHolder);

            car.setId(keyHolder.getKey().longValue());

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
