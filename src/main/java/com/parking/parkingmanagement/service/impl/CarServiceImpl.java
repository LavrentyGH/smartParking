package com.parking.parkingmanagement.service.impl;

import com.parking.parkingmanagement.dto.CreateCarRequest;
import com.parking.parkingmanagement.entity.Car;
import com.parking.parkingmanagement.entity.Owner;
import com.parking.parkingmanagement.repository.CarRepository;
import com.parking.parkingmanagement.service.CarService;
import com.parking.parkingmanagement.service.OwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    private final OwnerService ownerService;

    public CarServiceImpl(CarRepository carRepository, OwnerService ownerService) {
        this.carRepository = carRepository;
        this.ownerService = ownerService;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAllWithOwner();
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобиль не найден с id: " + id));
    }

    @Override
    public Car createCar(CreateCarRequest createCarRequest) {
        if (carRepository.findByLicensePlate(createCarRequest.getLicensePlate()).contains(createCarRequest.getLicensePlate())) {
            throw new RuntimeException("Car with license plate " + createCarRequest.getLicensePlate() + " already exists");
        }

        Owner owner = ownerService.getOwnerById(createCarRequest.getOwnerId());

        Car car = new Car();
        car.setLicensePlate(createCarRequest.getLicensePlate());
        car.setOwnerId(owner.getId());
        car.setCreatedAt(LocalDateTime.now());
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Long id, Car carDetails) {
        Car car = getCarById(id);

        if (!car.getLicensePlate().equals(carDetails.getLicensePlate()) &&
                carRepository.existsByLicensePlate(carDetails.getLicensePlate())) {
            throw new RuntimeException("Автомобиль с таким номером уже существует");
        }

        car.setLicensePlate(carDetails.getLicensePlate());

        if (!car.getOwnerId().equals(carDetails.getOwnerId())) {
            Owner owner = ownerService.getOwnerById(carDetails.getOwnerId());
            car.setOwnerId(owner.getId());
        }

        return carRepository.save(car);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public List<Car> searchCars(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate);
    }
}
