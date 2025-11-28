package com.parking.parkingmanagement.exception;

public class OwnerAlreadyExistsException extends RuntimeException {
    public OwnerAlreadyExistsException(String fullName) {
        super("Владелец с ФИО " + fullName + " уже существует");
    }
}
