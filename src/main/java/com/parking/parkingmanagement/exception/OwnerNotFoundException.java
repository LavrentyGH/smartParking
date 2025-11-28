package com.parking.parkingmanagement.exception;

public class OwnerNotFoundException extends RuntimeException {
    public OwnerNotFoundException(String message) {
        super(message);
    }

    public OwnerNotFoundException(Long ownerId) {
        super("Владелец с ID " + ownerId + " не найден");
    }
}