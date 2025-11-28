package com.parking.parkingmanagement.dto.car;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CarDTO {
    private Long id;

    @JsonProperty("licensePlate")
    private String licensePlate;

    @JsonProperty("ownerId")
    private Long ownerId;
    private LocalDateTime createdAt;

    @JsonProperty("ownerFullName")
    private String ownerFullName;

    public CarDTO() {}
    public CarDTO(Long id, String licensePlate, Long ownerId, LocalDateTime createdAt, String ownerFullName) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.ownerFullName = ownerFullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }
}
