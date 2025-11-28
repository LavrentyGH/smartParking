package com.parking.parkingmanagement.exception;

public class ParkingSpotNotFoundException extends RuntimeException {
    public ParkingSpotNotFoundException(String message) {
        super(message);
    }
    public ParkingSpotNotFoundException(Long spotId) {
        super("Парковочное место с ID " + spotId + " не найдено");
    }
}
