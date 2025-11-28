package com.parking.parkingmanagement.controller;

import com.parking.parkingmanagement.dto.ApiResponse;
import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.reservation.CreateReservationRequest;
import com.parking.parkingmanagement.dto.reservation.ReservationDTO;
import com.parking.parkingmanagement.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/paged")
    public ApiResponse<PagedResponse<ReservationDTO>> getReservationsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<ReservationDTO> reservations = reservationService.findAllPaged(page, size);
        return ApiResponse.success(reservations, "Бронирования получены успешно");
    }

    @GetMapping
    public ApiResponse<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ApiResponse.success(reservations, "Бронирования получены успешно");
    }

    @GetMapping("/{id}")
    public ApiResponse<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.getReservationById(id);
        return ApiResponse.success(reservation, "Бронирование найдено");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationDTO>> createReservation(
            @Valid @RequestBody CreateReservationRequest request) {
        ReservationDTO reservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(reservation, "Бронирование успешно создано"));
    }

    @PutMapping("/{id}/pay")
    public ApiResponse<ReservationDTO> markAsPaid(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.markAsPaid(id);
        return ApiResponse.success(reservation, "Бронирование оплачено");
    }

    @PutMapping("/{id}/free")
    public ApiResponse<ReservationDTO> freeSpot(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.freeSpot(id);
        return ApiResponse.success(reservation, "Парковочное место освобождено");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ApiResponse.success(null, "Бронирование отменено");
    }

    @GetMapping("/search")
    public ApiResponse<List<ReservationDTO>> searchReservations(
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String ownerName) {

        List<ReservationDTO> reservations;
        if (licensePlate != null && !licensePlate.trim().isEmpty()) {
            reservations = reservationService.searchByCarLicensePlate(licensePlate);
        } else if (ownerName != null && !ownerName.trim().isEmpty()) {
            reservations = reservationService.searchByOwnerFullName(ownerName);
        } else {
            reservations = reservationService.getAllReservations();
        }
        return ApiResponse.success(reservations, "Поиск завершен");
    }

    @GetMapping("/active")
    public ApiResponse<List<ReservationDTO>> getActiveReservations() {
        List<ReservationDTO> reservations = reservationService.getActiveReservations();
        return ApiResponse.success(reservations, "Активные бронирования получены");
    }
}
