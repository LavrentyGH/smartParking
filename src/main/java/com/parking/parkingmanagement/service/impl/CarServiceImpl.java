package com.parking.parkingmanagement.service.impl;

import com.parking.parkingmanagement.dto.owner.OwnerDTO;
import com.parking.parkingmanagement.dto.car.CarDTO;
import com.parking.parkingmanagement.dto.car.CreateCarRequest;
import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.entity.Car;
import com.parking.parkingmanagement.exception.CarAlreadyExistsException;
import com.parking.parkingmanagement.exception.CarNotFoundException;
import com.parking.parkingmanagement.mapper.CarMapper;
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

    private final CarMapper carMapper;

    public CarServiceImpl(CarRepository carRepository, OwnerService ownerService, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.ownerService = ownerService;
        this.carMapper = carMapper;
    }

    @Override
    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAllWithOwner();
        if (cars.isEmpty()) {
                throw new CarNotFoundException("Машины не найдены");
        }
        return cars.stream().map(carMapper::carToCarDTO).toList();
    }

    @Override
    public CarDTO getCarById(Long id) {
        return carMapper.carToCarDTO(getCarByIdWithoutDTO(id));
    }


    @Override
    public CarDTO createCar(CreateCarRequest createCarRequest) {
        if (carRepository.findByLicensePlate(createCarRequest.getLicensePlate())
                .isPresent()) {
            throw new CarAlreadyExistsException(createCarRequest.getLicensePlate());
        }

        Car car = carMapper.toEntity(createCarRequest);
        car.setCreatedAt(LocalDateTime.now());
        carRepository.save(car);
        return carMapper.carToCarDTO(car);
    }

    @Override
    public CarDTO updateCar(Long id, Car carDetails) {
        Car car = getCarByIdWithoutDTO(id);

        if (!car.getLicensePlate().equals(carDetails.getLicensePlate()) &&
                carRepository.existsByLicensePlate(carDetails.getLicensePlate())) {
            throw new CarAlreadyExistsException("Автомобиль с таким номером "
                    + carDetails.getLicensePlate() + " уже существует");
        }

        car.setLicensePlate(carDetails.getLicensePlate());

        if (!car.getOwnerId().equals(carDetails.getOwnerId())) {
            OwnerDTO owner = ownerService.getOwnerById(carDetails.getOwnerId());
            car.setOwnerId(owner.getId());
        }
        carRepository.save(car);
        return carMapper.carToCarDTO(car);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public CarDTO searchCars(String licensePlate) {
        Car car = carRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() ->
                new CarNotFoundException("Машин с номером: " + licensePlate + " не найдены"));

        return carMapper.carToCarDTO(car);
    }

    @Override
    public PagedResponse<CarDTO> findAllPaged(int page, int size) {

        PagedResponse<Car> carPage = carRepository.findAllPaged(page, size);
        if (carPage.getContent().isEmpty()) {
            throw new CarNotFoundException("Ошибка в поиске автомобиля");
        }

        List<CarDTO> carDTOs = carPage.getContent().stream()
                .map(carMapper::carToCarDTO)
                .toList();

        return new PagedResponse<>(carDTOs,
                carPage.getCurrentPage(),
                carPage.getTotalPages(),
                carPage.getTotalItems(),
                carPage.getPageSize());
    }

    private Car getCarByIdWithoutDTO(Long id) {
        return (carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id)));
    }
}
