package com.parking.parkingmanagement.entity;

import java.time.LocalDateTime;

public class Car {
    private Long id;
    private String licensePlate;
    private Long ownerId;
    private LocalDateTime createdAt;

    // Для JOIN запросов
    private String ownerFullName;

    // Конструкторы
    public Car() {}

    public Car(Long id, String licensePlate, Long ownerId, LocalDateTime createdAt) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getOwnerFullName() { return ownerFullName; }
    public void setOwnerFullName(String ownerFullName) { this.ownerFullName = ownerFullName; }
}
