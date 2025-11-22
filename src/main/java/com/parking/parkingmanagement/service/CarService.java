package com.parking.parkingmanagement.service;

import com.parking.parkingmanagement.dto.CreateCarRequest;
import com.parking.parkingmanagement.entity.Car;

import java.util.List;

public interface CarService {
    List<Car> getAllCars();

    Car getCarById(Long id);

    Car createCar(CreateCarRequest createCarRequest);

    Car updateCar(Long id, Car carDetails);

    void deleteCar(Long id);

    List<Car> searchCars(String licensePlate);
}
