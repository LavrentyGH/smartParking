package com.parking.parkingmanagement.service.impl;


import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.parkingspot.CreateParkingSpotRequest;
import com.parking.parkingmanagement.dto.parkingspot.ParkingSpotDTO;
import com.parking.parkingmanagement.dto.parkingspot.UpdateParkingSpotRequest;
import com.parking.parkingmanagement.entity.ParkingSpot;
import com.parking.parkingmanagement.exception.ParkingSpotAlreadyExistsException;
import com.parking.parkingmanagement.exception.ParkingSpotNotFoundException;
import com.parking.parkingmanagement.mapper.ParkingSpotMapper;
import com.parking.parkingmanagement.repository.ParkingSpotRepository;
import com.parking.parkingmanagement.service.ParkingSpotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSpotMapper parkingSpotMapper;

    public ParkingSpotServiceImpl(ParkingSpotRepository parkingSpotRepository,
                                  ParkingSpotMapper parkingSpotMapper) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingSpotMapper = parkingSpotMapper;
    }

    @Override
    public PagedResponse<ParkingSpotDTO> findAllPaged(int page, int size) {
        PagedResponse<ParkingSpot> spotPage = parkingSpotRepository.findAllPaged(page, size);
        List<ParkingSpotDTO> spotDTOs = spotPage.getContent().stream()
                .map(parkingSpotMapper::toParkingSpotDto)
                .toList();

        return new PagedResponse<>(spotDTOs,
                spotPage.getCurrentPage(),
                spotPage.getTotalPages(),
                spotPage.getTotalItems(),
                spotPage.getPageSize());
    }

    @Override
    public List<ParkingSpotDTO> getAllParkingSpots() {
        List<ParkingSpot> spots = parkingSpotRepository.findAll();
        if (spots.isEmpty()) {
            throw new ParkingSpotNotFoundException("Парковочные места не найдены");
        }
        return spots.stream().map(parkingSpotMapper::toParkingSpotDto).toList();
    }

    @Override
    public ParkingSpotDTO getParkingSpotById(Long id) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ParkingSpotNotFoundException(id));
        return parkingSpotMapper.toParkingSpotDto(spot);
    }

    @Override
    public ParkingSpotDTO createParkingSpot(CreateParkingSpotRequest createRequest) {
        if (parkingSpotRepository.existsBySpotNumber(createRequest.getSpotNumber())) {
            throw new ParkingSpotAlreadyExistsException(createRequest.getSpotNumber());
        }

        ParkingSpot spot = new ParkingSpot();
        spot.setSpotNumber(createRequest.getSpotNumber());
        spot.setIsAvailable(createRequest.getIsAvailable());
        spot.setCreatedAt(LocalDateTime.now());

        ParkingSpot savedSpot = parkingSpotRepository.save(spot);
        return parkingSpotMapper.toParkingSpotDto(savedSpot);
    }

    @Override
    public ParkingSpotDTO updateParkingSpot(Long id, UpdateParkingSpotRequest updateRequest) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ParkingSpotNotFoundException(id));

        if (!spot.getSpotNumber().equals(updateRequest.getSpotNumber()) &&
            parkingSpotRepository.existsBySpotNumber(updateRequest.getSpotNumber())) {
            throw new ParkingSpotAlreadyExistsException(updateRequest.getSpotNumber());
        }

        spot.setSpotNumber(updateRequest.getSpotNumber());
        spot.setIsAvailable(updateRequest.getIsAvailable());

        ParkingSpot updatedSpot = parkingSpotRepository.save(spot);
        return parkingSpotMapper.toParkingSpotDto(updatedSpot);
    }

    @Override
    public void deleteParkingSpot(Long id) {
        if (!parkingSpotRepository.findById(id).isPresent()) {
            throw new ParkingSpotNotFoundException(id);
        }
        parkingSpotRepository.deleteById(id);
    }

    @Override
    public List<ParkingSpotDTO> getAvailableParkingSpots() {
        List<ParkingSpot> spots = parkingSpotRepository.findAvailableSpots();
        if (spots.isEmpty()) {
            throw new ParkingSpotNotFoundException("Доступные парковочные места не найдены");
        }
        return spots.stream().map(parkingSpotMapper::toParkingSpotDto).toList();
    }

    @Override
    public ParkingSpotDTO updateAvailability(Long id, Boolean isAvailable) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ParkingSpotNotFoundException(id));

        spot.setIsAvailable(isAvailable);
        ParkingSpot updatedSpot = parkingSpotRepository.save(spot);
        return parkingSpotMapper.toParkingSpotDto(updatedSpot);
    }

    @Override
    public ParkingSpotDTO findBySpotNumber(String spotNumber) {
        ParkingSpot spot = parkingSpotRepository.findBySpotNumber(spotNumber)
                .orElseThrow(() -> new ParkingSpotNotFoundException(
                        "Парковочное место с номером " + spotNumber + " не найдено"));
        return parkingSpotMapper.toParkingSpotDto(spot);
    }
}