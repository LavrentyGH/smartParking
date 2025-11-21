package com.parking.parkingmanagement.service.impl;

import com.parking.parkingmanagement.entity.Car;
import com.parking.parkingmanagement.entity.Owner;
import com.parking.parkingmanagement.repository.CarRepository;
import com.parking.parkingmanagement.service.CarService;
import com.parking.parkingmanagement.service.OwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Car createCar(Car car) {
        if (carRepository.existsByLicensePlate(car.getLicensePlate())) {
            throw new RuntimeException("Автомобиль с таким номером уже существует");
        }

        Owner owner = ownerService.getOwnerById(car.getOwnerId());

        car.setOwnerId(owner.getId());
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
