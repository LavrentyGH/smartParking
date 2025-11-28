package com.parking.parkingmanagement.dto.owner;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class OwnerDTO {
    private Long id;

    @JsonProperty("fullName")
    private String fullName;

    private LocalDateTime createdAt;

    public OwnerDTO() {}

    public OwnerDTO(Long id, String fullName, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}