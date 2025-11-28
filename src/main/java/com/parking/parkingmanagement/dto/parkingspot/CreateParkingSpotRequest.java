package com.parking.parkingmanagement.dto.parkingspot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateParkingSpotRequest {
    @NotBlank(message = "Номер места обязателен")
    private String spotNumber;

    @NotNull(message = "Статус доступности обязателен")
    private Boolean isAvailable;

    public CreateParkingSpotRequest() {}

    public CreateParkingSpotRequest(String spotNumber, Boolean isAvailable) {
        this.spotNumber = spotNumber;
        this.isAvailable = isAvailable;
    }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }
}
