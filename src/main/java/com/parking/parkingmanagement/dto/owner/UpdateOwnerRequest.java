package com.parking.parkingmanagement.dto.owner;

import jakarta.validation.constraints.NotBlank;

public class UpdateOwnerRequest {
    @NotBlank(message = "ФИО обязательно")
    private String fullName;

    public UpdateOwnerRequest() {}

    public UpdateOwnerRequest(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
