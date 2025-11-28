package com.parking.parkingmanagement.service;

import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.parkingspot.CreateParkingSpotRequest;
import com.parking.parkingmanagement.dto.parkingspot.ParkingSpotDTO;
import com.parking.parkingmanagement.dto.parkingspot.UpdateParkingSpotRequest;

import java.util.List;

public interface ParkingSpotService {
    PagedResponse<ParkingSpotDTO> findAllPaged(int page, int size);
    List<ParkingSpotDTO> getAllParkingSpots();
    ParkingSpotDTO getParkingSpotById(Long id);
    ParkingSpotDTO createParkingSpot(CreateParkingSpotRequest createRequest);
    ParkingSpotDTO updateParkingSpot(Long id, UpdateParkingSpotRequest updateRequest);
    void deleteParkingSpot(Long id);
    List<ParkingSpotDTO> getAvailableParkingSpots();
    ParkingSpotDTO updateAvailability(Long id, Boolean isAvailable);
    ParkingSpotDTO findBySpotNumber(String spotNumber);
}
