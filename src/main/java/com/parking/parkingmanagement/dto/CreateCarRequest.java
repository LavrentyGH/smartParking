package com.parking.parkingmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCarRequest {
    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    // Конструкторы
    public CreateCarRequest() {}

    public CreateCarRequest(String licensePlate, Long ownerId) {
        this.licensePlate = licensePlate;
        this.ownerId = ownerId;
    }

    // Геттеры и сеттеры
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
