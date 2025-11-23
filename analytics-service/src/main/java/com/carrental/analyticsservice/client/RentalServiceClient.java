package com.carrental.analyticsservice.client;

import com.carrental.analyticsservice.dto.Rental;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * FeignClient for communicating with rental-service.
 */
@FeignClient(name = "rental-service", url = "${rental.service.url:http://localhost:8082}")
public interface RentalServiceClient {

    /**
     * Get all rentals from rental-service.
     */
    @GetMapping("/api/rentals")
    ResponseEntity<List<Rental>> getAllRentals();

    /**
     * Get rentals by car ID from rental-service.
     */
    @GetMapping("/api/rentals/car/{carId}")
    ResponseEntity<List<Rental>> getRentalsByCarId(@PathVariable Long carId);
}

