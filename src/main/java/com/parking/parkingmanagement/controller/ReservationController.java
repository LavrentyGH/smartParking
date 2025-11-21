package com.parking.parkingmanagement.controller;

import com.parking.parkingmanagement.entity.Reservation;
import com.parking.parkingmanagement.service.ReservationService;
import jakarta.validation.Valid;
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

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping
    public Reservation createReservation(@Valid @RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    @PutMapping("/{id}/pay")
    public Reservation markAsPaid(@PathVariable Long id) {
        return reservationService.markAsPaid(id);
    }

    @PutMapping("/{id}/free")
    public Reservation freeSpot(@PathVariable Long id) {
        return reservationService.freeSpot(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<Reservation> searchReservations(
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String ownerName) {

        if (licensePlate != null && !licensePlate.trim().isEmpty()) {
            return reservationService.searchByCarLicensePlate(licensePlate);
        } else if (ownerName != null && !ownerName.trim().isEmpty()) {
            return reservationService.searchByOwnerFullName(ownerName);
        } else {
            return reservationService.getAllReservations();
        }
    }

    @GetMapping("/active")
    public List<Reservation> getActiveReservations() {
        return reservationService.getActiveReservations();
    }
}
