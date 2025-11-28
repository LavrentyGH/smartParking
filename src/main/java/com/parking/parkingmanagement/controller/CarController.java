package com.parking.parkingmanagement.controller;

import com.parking.parkingmanagement.dto.ApiResponse;
import com.parking.parkingmanagement.dto.car.CarDTO;
import com.parking.parkingmanagement.dto.car.CreateCarRequest;
import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.entity.Car;
import com.parking.parkingmanagement.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:3000")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ApiResponse<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();
        return ApiResponse.success(cars, "Автомобили получены успешно");
    }

    @GetMapping("/paged")
    public ApiResponse<PagedResponse<CarDTO>> getCarsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        PagedResponse<CarDTO> cars = carService.findAllPaged(page, size);
        return ApiResponse.success(cars, "Автомобили получены успешно");
    }

    @GetMapping("/{id}")
    public ApiResponse<CarDTO> getCarById(@PathVariable Long id) {
            CarDTO car = carService.getCarById(id);
            return ApiResponse.success(car, "Car retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CarDTO>> createCar(@Valid @RequestBody CreateCarRequest createCarRequest) {
        CarDTO car = carService.createCar(createCarRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(car, "Car created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CarDTO>> updateCar(@PathVariable Long id, @Valid @RequestBody Car carDetails) {
        CarDTO car = carService.updateCar(id, carDetails);
        return ResponseEntity.ok(ApiResponse.success(car, "Car updated successfully"));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Car deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("CAR_NOT_FOUND", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete car: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<CarDTO>> searchCars(@RequestParam String licensePlate) {

        CarDTO cars = carService.searchCars(licensePlate);
        return ResponseEntity.ok(ApiResponse.success(cars, "Cars search completed"));

    }
}
