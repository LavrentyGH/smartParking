package com.parking.parkingmanagement.service;

import com.parking.parkingmanagement.entity.Reservation;

import java.util.List;

public interface ReservationService {
    List<Reservation> getAllReservations();

    Reservation getReservationById(Long id);

    Reservation createReservation(Reservation reservation);

    Reservation markAsPaid(Long id);

    Reservation freeSpot(Long id);

    List<Reservation> searchByCarLicensePlate(String licensePlate);

    List<Reservation> searchByOwnerFullName(String ownerName);

    List<Reservation> getActiveReservations();

    void cancelReservation(Long id);
}
