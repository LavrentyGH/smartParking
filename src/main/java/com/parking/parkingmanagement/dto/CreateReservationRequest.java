package com.parking.parkingmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class CreateReservationRequest {
    @NotNull(message = "carId обязателен")
    @JsonProperty("carId")
    private Long carId;

    @NotNull(message = "spotId обязателен")
    @JsonProperty("spotId")
    private Long spotId;

    // Обязательно должны быть геттеры и сеттеры с правильными именами
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    // Добавьте конструкторы для надежности
    public CreateReservationRequest() {}

    public CreateReservationRequest(Long carId, Long spotId) {
        this.carId = carId;
        this.spotId = spotId;
    }
}