package com.parking.parkingmanagement.mapper;

import com.parking.parkingmanagement.dto.reservation.ReservationDTO;
import com.parking.parkingmanagement.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public ReservationDTO toDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setCarId(reservation.getCarId());
        dto.setSpotId(reservation.getSpotId());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setPaid(reservation.getIsPaid());
        dto.setStatus(reservation.getStatus());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setCarLicensePlate(reservation.getCarLicensePlate());
        dto.setOwnerFullName(reservation.getOwnerFullName());
        dto.setSpotNumber(reservation.getSpotNumber());
        return dto;
    }
}
