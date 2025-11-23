package com.carrental.analyticsservice.service;

import com.carrental.analyticsservice.client.CarServiceClient;
import com.carrental.analyticsservice.client.RentalServiceClient;
import com.carrental.analyticsservice.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for calculating car occupancy analytics.
 * Calculates occupancy rates based on rental data from rental-service and car data from car-service.
 */
@Service
public class AnalyticsService {

    private final CarServiceClient carServiceClient;
    private final RentalServiceClient rentalServiceClient;

    public AnalyticsService(CarServiceClient carServiceClient, RentalServiceClient rentalServiceClient) {
        this.carServiceClient = carServiceClient;
        this.rentalServiceClient = rentalServiceClient;
    }

    /**
     * Calculate occupancy rates for all cars over a specified period.
     * Defaults to the last 30 days if no dates are provided.
     * 
     * @param startDate Start date of the period (optional)
     * @param endDate End date of the period (optional)
     * @return List of occupancy rates for all cars
     */
    public List<OccupancyRate> calculateOccupancyRates(LocalDate startDate, LocalDate endDate) {
        // Set default period to last 30 days if not provided
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate == null) {
            startDate = endDate.minusDays(30);
        }

        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Start date must be before or equal to end date");
        }

        long totalDaysInPeriod = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Fetch all cars
        ResponseEntity<List<Car>> carsResponse = carServiceClient.getAllCars();
        if (carsResponse.getStatusCode() != HttpStatus.OK || carsResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                "Unable to fetch cars from car-service");
        }

        List<Car> cars = carsResponse.getBody();

        // Fetch all rentals
        ResponseEntity<List<Rental>> rentalsResponse = rentalServiceClient.getAllRentals();
        if (rentalsResponse.getStatusCode() != HttpStatus.OK || rentalsResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                "Unable to fetch rentals from rental-service");
        }

        List<Rental> allRentals = rentalsResponse.getBody();

        // Calculate occupancy for each car
        List<OccupancyRate> occupancyRates = new ArrayList<>();

        for (Car car : cars) {
            // Filter rentals for this car that are active or completed
            List<Rental> carRentals = allRentals.stream()
                .filter(r -> r.getCarId().equals(car.getId()))
                .filter(r -> "ACTIVE".equalsIgnoreCase(r.getStatus()) || 
                            "COMPLETED".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());

            // Calculate rented days within the period
            long rentedDays = calculateRentedDays(carRentals, startDate, endDate);

            // Calculate occupancy percentage
            double occupancyPercentage = totalDaysInPeriod > 0 
                ? (double) rentedDays / totalDaysInPeriod * 100.0 
                : 0.0;

            OccupancyRate occupancyRate = new OccupancyRate(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                totalDaysInPeriod,
                rentedDays,
                Math.round(occupancyPercentage * 100.0) / 100.0, // Round to 2 decimal places
                carRentals.size()
            );

            occupancyRates.add(occupancyRate);
        }

        return occupancyRates;
    }

    /**
     * Calculate occupancy rate for a specific car.
     * 
     * @param carId Car ID
     * @param startDate Start date of the period (optional)
     * @param endDate End date of the period (optional)
     * @return Occupancy rate for the specified car
     */
    public OccupancyRate calculateOccupancyRateForCar(Long carId, LocalDate startDate, LocalDate endDate) {
        // Set default period to last 30 days if not provided
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate == null) {
            startDate = endDate.minusDays(30);
        }

        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Start date must be before or equal to end date");
        }

        long totalDaysInPeriod = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Fetch car
        ResponseEntity<Car> carResponse = carServiceClient.getCarById(carId);
        if (carResponse.getStatusCode() != HttpStatus.OK || carResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Car not found with ID: " + carId);
        }

        Car car = carResponse.getBody();

        // Fetch rentals for this car
        ResponseEntity<List<Rental>> rentalsResponse = rentalServiceClient.getRentalsByCarId(carId);
        if (rentalsResponse.getStatusCode() != HttpStatus.OK || rentalsResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                "Unable to fetch rentals from rental-service");
        }

        List<Rental> carRentals = rentalsResponse.getBody().stream()
            .filter(r -> "ACTIVE".equalsIgnoreCase(r.getStatus()) || 
                        "COMPLETED".equalsIgnoreCase(r.getStatus()))
            .collect(Collectors.toList());

        // Calculate rented days within the period
        long rentedDays = calculateRentedDays(carRentals, startDate, endDate);

        // Calculate occupancy percentage
        double occupancyPercentage = totalDaysInPeriod > 0 
            ? (double) rentedDays / totalDaysInPeriod * 100.0 
            : 0.0;

        return new OccupancyRate(
            car.getId(),
            car.getBrand(),
            car.getModel(),
            car.getYear(),
            totalDaysInPeriod,
            rentedDays,
            Math.round(occupancyPercentage * 100.0) / 100.0, // Round to 2 decimal places
            carRentals.size()
        );
    }

    /**
     * Calculate the total number of rented days for a list of rentals within a date range.
     */
    private long calculateRentedDays(List<Rental> rentals, LocalDate startDate, LocalDate endDate) {
        long totalRentedDays = 0;

        for (Rental rental : rentals) {
            LocalDate rentalStart = rental.getStartDate().isBefore(startDate) ? startDate : rental.getStartDate();
            LocalDate rentalEnd = rental.getEndDate().isAfter(endDate) ? endDate : rental.getEndDate();

            // Only count days if the rental overlaps with the period
            if (!rentalStart.isAfter(rentalEnd)) {
                long days = ChronoUnit.DAYS.between(rentalStart, rentalEnd) + 1;
                totalRentedDays += days;
            }
        }

        // Cap at total days in period (handles overlapping rentals)
        long totalDaysInPeriod = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return Math.min(totalRentedDays, totalDaysInPeriod);
    }
}

