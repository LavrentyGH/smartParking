package com.parking.parkingmanagement.mapper;

import com.parking.parkingmanagement.dto.parkingspot.ParkingSpotDTO;
import com.parking.parkingmanagement.entity.ParkingSpot;
import org.springframework.stereotype.Component;

@Component
public class ParkingSpotMapper {
    public ParkingSpotDTO toParkingSpotDto(ParkingSpot parkingSpot) {
        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setId(parkingSpot.getId());
        parkingSpotDTO.setSpotNumber(parkingSpot.getSpotNumber());
        parkingSpotDTO.setIsAvailable(parkingSpot.getIsAvailable());
        parkingSpotDTO.setCreatedAt(parkingSpot.getCreatedAt());
        return parkingSpotDTO;
    }
}
