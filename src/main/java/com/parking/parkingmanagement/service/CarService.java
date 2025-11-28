package com.parking.parkingmanagement.service;

import com.parking.parkingmanagement.dto.car.CarDTO;
import com.parking.parkingmanagement.dto.car.CreateCarRequest;
import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.entity.Car;

import java.util.List;

public interface CarService {
    List<CarDTO> getAllCars();

    CarDTO getCarById(Long id);

    CarDTO createCar(CreateCarRequest createCarRequest);

    CarDTO updateCar(Long id, Car carDetails);

    void deleteCar(Long id);

    CarDTO searchCars(String licensePlate);

    PagedResponse<CarDTO> findAllPaged(int page, int size);
}
