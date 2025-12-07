package com.carrental.analyticsservice.dto;

/**
 * DTO représentant les statistiques du taux d'occupation des voitures.
 */
public class OccupancyRate {
    private Long carId;
    private String brand;
    private String model;
    private Integer year;
    private Long totalDaysInPeriod;
    private Long rentedDays;
    private Double occupancyPercentage;
    private Integer numberOfRentals;

    public OccupancyRate() {
    }

    public OccupancyRate(Long carId, String brand, String model, Integer year, 
                        Long totalDaysInPeriod, Long rentedDays, 
                        Double occupancyPercentage, Integer numberOfRentals) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.totalDaysInPeriod = totalDaysInPeriod;
        this.rentedDays = rentedDays;
        this.occupancyPercentage = occupancyPercentage;
        this.numberOfRentals = numberOfRentals;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getTotalDaysInPeriod() {
        return totalDaysInPeriod;
    }

    public void setTotalDaysInPeriod(Long totalDaysInPeriod) {
        this.totalDaysInPeriod = totalDaysInPeriod;
    }

    public Long getRentedDays() {
        return rentedDays;
    }

    public void setRentedDays(Long rentedDays) {
        this.rentedDays = rentedDays;
    }

    public Double getOccupancyPercentage() {
        return occupancyPercentage;
    }

    public void setOccupancyPercentage(Double occupancyPercentage) {
        this.occupancyPercentage = occupancyPercentage;
    }

    public Integer getNumberOfRentals() {
        return numberOfRentals;
    }

    public void setNumberOfRentals(Integer numberOfRentals) {
        this.numberOfRentals = numberOfRentals;
    }
}

