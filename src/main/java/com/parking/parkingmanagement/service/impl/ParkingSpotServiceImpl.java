package com.parking.parkingmanagement.service.impl;


import com.parking.parkingmanagement.entity.ParkingSpot;
import com.parking.parkingmanagement.repository.ParkingSpotRepository;
import com.parking.parkingmanagement.service.ParkingSpotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotServiceImpl(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Override
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }

    @Override
    public ParkingSpot getParkingSpotById(Long id) {
        return parkingSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Парковочное место не найдено с id: " + id));
    }

    @Override
    public ParkingSpot createParkingSpot(ParkingSpot parkingSpot) {
        if (parkingSpotRepository.existsBySpotNumber(parkingSpot.getSpotNumber())) {
            throw new RuntimeException("Парковочное место с таким номером уже существует");
        }
        return parkingSpotRepository.save(parkingSpot);
    }

    @Override
    public ParkingSpot updateParkingSpot(Long id, ParkingSpot parkingSpotDetails) {
        ParkingSpot parkingSpot = getParkingSpotById(id);

        if (!parkingSpot.getSpotNumber().equals(parkingSpotDetails.getSpotNumber()) &&
                parkingSpotRepository.existsBySpotNumber(parkingSpotDetails.getSpotNumber())) {
            throw new RuntimeException("Парковочное место с таким номером уже существует");
        }

        parkingSpot.setSpotNumber(parkingSpotDetails.getSpotNumber());
        parkingSpot.setIsAvailable(parkingSpotDetails.getIsAvailable());

        return parkingSpotRepository.save(parkingSpot);
    }

    @Override
    public void deleteParkingSpot(Long id) {
        parkingSpotRepository.deleteById(id);
    }

    @Override
    public List<ParkingSpot> getAvailableParkingSpots() {
        return parkingSpotRepository.findAvailableSpots();
    }

    @Override
    public ParkingSpot updateAvailability(Long id, Boolean isAvailable) {
        ParkingSpot parkingSpot = getParkingSpotById(id);
        parkingSpot.setIsAvailable(isAvailable);
        return parkingSpotRepository.save(parkingSpot);
    }

    @Override
    public ParkingSpot findBySpotNumber(String spotNumber) {
        return parkingSpotRepository.findBySpotNumber(spotNumber)
                .orElseThrow(() -> new RuntimeException("Парковочное место не найдено: " + spotNumber));
    }
}
