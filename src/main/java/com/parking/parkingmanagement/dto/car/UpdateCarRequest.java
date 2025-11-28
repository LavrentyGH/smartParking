package com.parking.parkingmanagement.dto.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateCarRequest
{
    @NotBlank
    private String licensePlate;
    @NotNull
    private Long ownerId;

    public UpdateCarRequest(String licensePlate, Long ownerId) {
        this.licensePlate = licensePlate;
        this.ownerId = ownerId;
    }

    public @NotBlank String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(@NotBlank String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public @NotNull Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(@NotNull Long ownerId) {
        this.ownerId = ownerId;
    }
}
