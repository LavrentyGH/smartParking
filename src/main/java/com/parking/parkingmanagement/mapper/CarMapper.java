package com.parking.parkingmanagement.mapper;

import com.parking.parkingmanagement.dto.car.CarDTO;
import com.parking.parkingmanagement.dto.car.CreateCarRequest;
import com.parking.parkingmanagement.entity.Car;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CarMapper {
    public CarDTO carToCarDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setLicensePlate(car.getLicensePlate());
        carDTO.setOwnerId(car.getOwnerId());
        carDTO.setOwnerFullName(car.getOwnerFullName());
        carDTO.setCreatedAt(car.getCreatedAt());
        return carDTO;
    }

    public Car toEntity(CreateCarRequest request) {
        Car car = new Car();
        car.setLicensePlate(request.getLicensePlate());
        car.setOwnerId(request.getOwnerId());
        car.setCreatedAt(LocalDateTime.now());
        return car;
    }
}
