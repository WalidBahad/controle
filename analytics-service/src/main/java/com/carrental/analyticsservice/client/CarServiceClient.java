package com.carrental.analyticsservice.client;

import com.carrental.analyticsservice.dto.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * FeignClient for communicating with car-service.
 */
@FeignClient(name = "car-service", url = "${car.service.url:http://localhost:8081}")
public interface CarServiceClient {

    /**
     * Get all cars from car-service.
     */
    @GetMapping("/api/cars")
    ResponseEntity<List<Car>> getAllCars();

    /**
     * Get car by ID from car-service.
     */
    @GetMapping("/api/cars/{id}")
    ResponseEntity<Car> getCarById(@PathVariable Long id);
}

