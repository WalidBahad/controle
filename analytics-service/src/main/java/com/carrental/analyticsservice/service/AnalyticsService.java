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
 * Service de calcul des analyses d'occupation des voitures.
 * Calcule les taux d'occupation en fonction des données de location du service de location et des données de voiture du service de voiture.
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
     * Calculer les taux d'occupation pour toutes les voitures sur une période spécifiée.
     * La valeur par défaut est les 30 derniers jours si aucune date n'est fournie.
     * 
     * @param startDate Start date of the period (optional)
     * @param endDate End date of the period (optional)
     * @return List of occupancy rates for all cars
     */
    public List<OccupancyRate> calculateOccupancyRates(LocalDate startDate, LocalDate endDate) {
        // Définir la période par défaut aux 30 derniers jours si non fournie
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

        // Récupérer toutes les voitures
        ResponseEntity<List<Car>> carsResponse = carServiceClient.getAllCars();
        if (carsResponse.getStatusCode() != HttpStatus.OK || carsResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                "Unable to fetch cars from car-service");
        }

        List<Car> cars = carsResponse.getBody();

        // Récupérer toutes les locations
        ResponseEntity<List<Rental>> rentalsResponse = rentalServiceClient.getAllRentals();
        if (rentalsResponse.getStatusCode() != HttpStatus.OK || rentalsResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                "Unable to fetch rentals from rental-service");
        }

        List<Rental> allRentals = rentalsResponse.getBody();

        // Calculer l'occupation pour chaque voiture
        List<OccupancyRate> occupancyRates = new ArrayList<>();

        for (Car car : cars) {
            // Filtrer les locations pour cette voiture qui sont actives ou terminées
            List<Rental> carRentals = allRentals.stream()
                .filter(r -> r.getCarId().equals(car.getId()))
                .filter(r -> "ACTIVE".equalsIgnoreCase(r.getStatus()) || 
                            "COMPLETED".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());

            // Calculer les jours loués dans la période
            long rentedDays = calculateRentedDays(carRentals, startDate, endDate);

            // Calculer le pourcentage d'occupation
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
     * Calculer le taux d'occupation pour une voiture spécifique.
     * 
     * @param carId Car ID
     * @param startDate Start date of the period (optional)
     * @param endDate End date of the period (optional)
     * @return Occupancy rate for the specified car
     */
    public OccupancyRate calculateOccupancyRateForCar(Long carId, LocalDate startDate, LocalDate endDate) {
        // Définir la période par défaut aux 30 derniers jours si non fournie
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

        // Récupérer la voiture
        ResponseEntity<Car> carResponse = carServiceClient.getCarById(carId);
        if (carResponse.getStatusCode() != HttpStatus.OK || carResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Car not found with ID: " + carId);
        }

        Car car = carResponse.getBody();

        // Récupérer les locations pour cette voiture
        ResponseEntity<List<Rental>> rentalsResponse = rentalServiceClient.getRentalsByCarId(carId);
        if (rentalsResponse.getStatusCode() != HttpStatus.OK || rentalsResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                "Unable to fetch rentals from rental-service");
        }

        List<Rental> carRentals = rentalsResponse.getBody().stream()
            .filter(r -> "ACTIVE".equalsIgnoreCase(r.getStatus()) || 
                        "COMPLETED".equalsIgnoreCase(r.getStatus()))
            .collect(Collectors.toList());

        // Calculer les jours loués dans la période
        long rentedDays = calculateRentedDays(carRentals, startDate, endDate);

        // Calculer le pourcentage d'occupation
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
     * Calculer le nombre total de jours loués pour une liste de locations dans une plage de dates.
     */
    private long calculateRentedDays(List<Rental> rentals, LocalDate startDate, LocalDate endDate) {
        long totalRentedDays = 0;

        for (Rental rental : rentals) {
            LocalDate rentalStart = rental.getStartDate().isBefore(startDate) ? startDate : rental.getStartDate();
            LocalDate rentalEnd = rental.getEndDate().isAfter(endDate) ? endDate : rental.getEndDate();

            // Compter les jours uniquement si la location chevauche la période
            if (!rentalStart.isAfter(rentalEnd)) {
                long days = ChronoUnit.DAYS.between(rentalStart, rentalEnd) + 1;
                totalRentedDays += days;
            }
        }

        // Plafonner au nombre total de jours dans la période (gère les locations chevauchantes)
        long totalDaysInPeriod = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return Math.min(totalRentedDays, totalDaysInPeriod);
    }
}

