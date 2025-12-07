package com.carrental.rentalservice.dto;

import com.carrental.rentalservice.validation.ValidDateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

/**
 * DTO for creating a new rental request.
 */
@ValidDateRange(startDate = "startDate", endDate = "endDate")
public class RentalRequest {
    
    @NotNull(message = "Car ID is required")
    private Long carId;

    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-M-d")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-M-d")
    private LocalDate endDate;

    public RentalRequest() {
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

