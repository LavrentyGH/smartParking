package com.parking.parkingmanagement.dto.reservation;

import jakarta.validation.constraints.NotNull;

public class CreateReservationRequest {
    @NotNull(message = "ID автомобиля обязателен")
    private Long carId;

    @NotNull(message = "ID парковочного места обязательно")
    private Long spotId;

    public CreateReservationRequest() {}

    public CreateReservationRequest(Long carId, Long spotId) {
        this.carId = carId;
        this.spotId = spotId;
    }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public Long getSpotId() { return spotId; }
    public void setSpotId(Long spotId) { this.spotId = spotId; }
}
