package com.parking.parkingmanagement.entity;

import java.time.LocalDateTime;

public class ParkingSpot {
    private Long id;
    private String spotNumber;
    private Boolean isAvailable;
    private LocalDateTime createdAt;

    // Конструкторы
    public ParkingSpot() {}

    public ParkingSpot(Long id, String spotNumber, Boolean isAvailable, LocalDateTime createdAt) {
        this.id = id;
        this.spotNumber = spotNumber;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
