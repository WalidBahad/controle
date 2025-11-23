package com.carrental.rentalservice.client;

import com.carrental.rentalservice.dto.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * FeignClient for communicating with car-service.
 */
@FeignClient(name = "car-service", url = "${car.service.url:http://localhost:8081}")
public interface CarServiceClient {

    /**
     * Get car by ID from car-service.
     */
    @GetMapping("/api/cars/{id}")
    ResponseEntity<Car> getCarById(@PathVariable Long id);

    /**
     * Update car status in car-service.
     */
    @PutMapping("/api/cars/{id}")
    ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car);
}

