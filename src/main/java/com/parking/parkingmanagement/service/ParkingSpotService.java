package com.parking.parkingmanagement.service;

import com.parking.parkingmanagement.entity.ParkingSpot;

import java.util.List;

public interface ParkingSpotService {
    List<ParkingSpot> getAllParkingSpots();

    ParkingSpot getParkingSpotById(Long id);

    ParkingSpot createParkingSpot(ParkingSpot parkingSpot);

    ParkingSpot updateParkingSpot(Long id, ParkingSpot parkingSpotDetails);

    void deleteParkingSpot(Long id);

    List<ParkingSpot> getAvailableParkingSpots();

    ParkingSpot updateAvailability(Long id, Boolean isAvailable);

    ParkingSpot findBySpotNumber(String spotNumber);
}
