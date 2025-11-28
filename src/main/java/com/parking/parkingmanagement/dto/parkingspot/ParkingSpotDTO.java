package com.parking.parkingmanagement.dto.parkingspot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ParkingSpotDTO {
    private Long id;

    @JsonProperty("spotNumber")
    private String spotNumber;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    private LocalDateTime createdAt;

    public ParkingSpotDTO() {}

    public ParkingSpotDTO(Long id, String spotNumber, Boolean isAvailable, LocalDateTime createdAt) {
        this.id = id;
        this.spotNumber = spotNumber;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}