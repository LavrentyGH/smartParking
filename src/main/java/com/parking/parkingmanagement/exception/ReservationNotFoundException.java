package com.parking.parkingmanagement.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(Long reservationId) {
        super("Бронирование с ID " + reservationId + " не найдено");
    }
}
