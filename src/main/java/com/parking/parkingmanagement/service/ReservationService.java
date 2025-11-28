package com.parking.parkingmanagement.service;

import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.reservation.CreateReservationRequest;
import com.parking.parkingmanagement.dto.reservation.ReservationDTO;

import java.util.List;

public interface ReservationService {
    PagedResponse<ReservationDTO> findAllPaged(int page, int size);
    List<ReservationDTO> getAllReservations();
    ReservationDTO getReservationById(Long id);
    ReservationDTO createReservation(CreateReservationRequest request);
    ReservationDTO markAsPaid(Long id);
    ReservationDTO freeSpot(Long id);
    List<ReservationDTO> searchByCarLicensePlate(String licensePlate);
    List<ReservationDTO> searchByOwnerFullName(String ownerName);
    List<ReservationDTO> getActiveReservations();
    void cancelReservation(Long id);
}
