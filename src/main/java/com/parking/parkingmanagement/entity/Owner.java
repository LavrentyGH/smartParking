package com.parking.parkingmanagement.entity;

import java.time.LocalDateTime;

public class Owner {
    private Long id;
    private String fullName;
    private LocalDateTime createdAt;

    // Конструкторы
    public Owner() {}

    public Owner(Long id, String fullName, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
