package com.parking.parkingmanagement.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ReservationDTO {
    private Long id;
    private Long carId;
    private Long spotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isPaid;
    private String status;
    private LocalDateTime createdAt;

    @JsonProperty("carLicensePlate")
    private String carLicensePlate;

    @JsonProperty("ownerFullName")
    private String ownerFullName;

    @JsonProperty("spotNumber")
    private String spotNumber;
    public ReservationDTO() {}

    public ReservationDTO(Long id, Long carId, Long spotId, LocalDateTime startTime, LocalDateTime endTime,
                          Boolean isPaid, String status, LocalDateTime createdAt, String carLicensePlate,
                          String ownerFullName, String spotNumber) {
        this.id = id;
        this.carId = carId;
        this.spotId = spotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isPaid = isPaid;
        this.status = status;
        this.createdAt = createdAt;
        this.carLicensePlate = carLicensePlate;
        this.ownerFullName = ownerFullName;
        this.spotNumber = spotNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean paid) {
        isPaid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCarLicensePlate() {
        return carLicensePlate;
    }

    public void setCarLicensePlate(String carLicensePlate) {
        this.carLicensePlate = carLicensePlate;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }
}
