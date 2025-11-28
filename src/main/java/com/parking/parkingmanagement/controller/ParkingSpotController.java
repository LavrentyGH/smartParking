package com.parking.parkingmanagement.controller;

import com.parking.parkingmanagement.dto.ApiResponse;
import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.parkingspot.CreateParkingSpotRequest;
import com.parking.parkingmanagement.dto.parkingspot.ParkingSpotDTO;
import com.parking.parkingmanagement.dto.parkingspot.UpdateParkingSpotRequest;
import com.parking.parkingmanagement.service.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-spots")
@CrossOrigin(origins = "http://localhost:3000")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @GetMapping("/paged")
    public ApiResponse<PagedResponse<ParkingSpotDTO>> getParkingSpotsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<ParkingSpotDTO> spots = parkingSpotService.findAllPaged(page, size);
        return ApiResponse.success(spots, "Парковочные места получены успешно");
    }

    @GetMapping
    public ApiResponse<List<ParkingSpotDTO>> getAllParkingSpots() {
        List<ParkingSpotDTO> spots = parkingSpotService.getAllParkingSpots();
        return ApiResponse.success(spots, "Парковочные места получены успешно");
    }

    @GetMapping("/{id}")
    public ApiResponse<ParkingSpotDTO> getParkingSpotById(@PathVariable Long id) {
        ParkingSpotDTO spot = parkingSpotService.getParkingSpotById(id);
        return ApiResponse.success(spot, "Парковочное место найдено");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParkingSpotDTO>> createParkingSpot(
            @Valid @RequestBody CreateParkingSpotRequest createRequest) {
        ParkingSpotDTO spot = parkingSpotService.createParkingSpot(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(spot, "Парковочное место создано успешно"));
    }

    @PutMapping("/{id}")
    public ApiResponse<ParkingSpotDTO> updateParkingSpot(
            @PathVariable Long id,
            @Valid @RequestBody UpdateParkingSpotRequest updateRequest) {
        ParkingSpotDTO spot = parkingSpotService.updateParkingSpot(id, updateRequest);
        return ApiResponse.success(spot, "Парковочное место обновлено успешно");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteParkingSpot(@PathVariable Long id) {
        parkingSpotService.deleteParkingSpot(id);
        return ApiResponse.success(null, "Парковочное место удалено успешно");
    }

    @GetMapping("/available")
    public ApiResponse<List<ParkingSpotDTO>> getAvailableParkingSpots() {
        List<ParkingSpotDTO> spots = parkingSpotService.getAvailableParkingSpots();
        return ApiResponse.success(spots, "Доступные парковочные места получены");
    }

    @PutMapping("/{id}/availability")
    public ApiResponse<ParkingSpotDTO> updateAvailability(
            @PathVariable Long id,
            @RequestBody Boolean isAvailable) {
        ParkingSpotDTO spot = parkingSpotService.updateAvailability(id, isAvailable);
        return ApiResponse.success(spot, "Статус доступности обновлен");
    }

    @GetMapping("/search")
    public ApiResponse<ParkingSpotDTO> getParkingSpotByNumber(@RequestParam String spotNumber) {
        ParkingSpotDTO spot = parkingSpotService.findBySpotNumber(spotNumber);
        return ApiResponse.success(spot, "Парковочное место найдено");
    }
}
