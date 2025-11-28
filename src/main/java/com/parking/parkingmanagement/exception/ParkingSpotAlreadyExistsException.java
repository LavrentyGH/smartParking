package com.parking.parkingmanagement.exception;

public class ParkingSpotAlreadyExistsException extends RuntimeException {
    public ParkingSpotAlreadyExistsException(String spotNumber) {
        super("Парковочное место с номером " + spotNumber + " уже существует");
    }
}
