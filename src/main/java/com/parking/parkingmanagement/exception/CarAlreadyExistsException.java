package com.parking.parkingmanagement.exception;

public class CarAlreadyExistsException extends RuntimeException {
    public CarAlreadyExistsException(String licensePlate) {
        super("Автомобиль с госномером " + licensePlate + " уже существует");
    }
}
