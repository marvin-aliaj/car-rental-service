package com.spring.jdbc.car.rental.service;

import com.spring.jdbc.car.rental.dao.CarDao;
import com.spring.jdbc.car.rental.exception.CustomException;
import com.spring.jdbc.car.rental.model.Car;
import com.spring.jdbc.car.rental.model.filter.CarFilter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarService {
    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public boolean checkIfCarIsAvailable(long carId, String startDate, String endDate) throws Exception {
        CarFilter filter = new CarFilter();
        filter.setCarId(carId);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        List<Car> cars = getCars(filter);
        return !cars.isEmpty();
    }

    public List<Car> getCars(CarFilter filter) throws Exception {
        try {
            return carDao.getCars(filter);
        } catch (Exception e) {
            throw new CustomException();
        }
    }

    public void createCar(Car car) throws Exception {
        try {
            carDao.createCar(car);
        } catch (Exception e) {
            throw new CustomException();
        }
    }
}
