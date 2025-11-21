package com.parking.parkingmanagement.controller;

import com.parking.parkingmanagement.entity.ParkingSpot;
import com.parking.parkingmanagement.service.OwnerService;
import com.parking.parkingmanagement.service.ParkingSpotService;
import jakarta.validation.Valid;
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

    @GetMapping
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotService.getAllParkingSpots();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(@PathVariable Long id) {
        ParkingSpot parkingSpot = parkingSpotService.getParkingSpotById(id);
        return ResponseEntity.ok(parkingSpot);
    }

    @PostMapping
    public ParkingSpot createParkingSpot(@Valid @RequestBody ParkingSpot parkingSpot) {
        return parkingSpotService.createParkingSpot(parkingSpot);
    }

    @PutMapping("/{id}")
    public ParkingSpot updateParkingSpot(@PathVariable Long id, @Valid @RequestBody ParkingSpot parkingSpotDetails) {
        return parkingSpotService.updateParkingSpot(id, parkingSpotDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParkingSpot(@PathVariable Long id) {
        parkingSpotService.deleteParkingSpot(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public List<ParkingSpot> getAvailableParkingSpots() {
        return parkingSpotService.getAvailableParkingSpots();
    }

    @PutMapping("/{id}/availability")
    public ParkingSpot updateAvailability(@PathVariable Long id, @RequestBody Boolean isAvailable) {
        return parkingSpotService.updateAvailability(id, isAvailable);
    }

    @GetMapping("/search")
    public ResponseEntity<ParkingSpot> getParkingSpotByNumber(@RequestParam String spotNumber) {
        ParkingSpot parkingSpot = parkingSpotService.findBySpotNumber(spotNumber);
        return ResponseEntity.ok(parkingSpot);
    }
}
