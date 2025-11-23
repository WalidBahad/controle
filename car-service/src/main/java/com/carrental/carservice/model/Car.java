package com.carrental.carservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Car entity representing a rental car in the system.
 * Each car has attributes: id, brand, model, year, status, and price per day.
 */
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Brand is required")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be at least 1900")
    @Max(value = 2100, message = "Year must be at most 2100")
    @Column(name = "car_year",nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status = CarStatus.AVAILABLE;

    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(name = "price_per_day", nullable = false)
    private Double pricePerDay;

    public Car() {
    }

    public Car(String brand, String model, Integer year, CarStatus status, Double pricePerDay) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.status = status;
        this.pricePerDay = pricePerDay;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    /**
     * Enumeration for car availability status.
     */
    public enum CarStatus {
        AVAILABLE,
        RENTED
    }
}

