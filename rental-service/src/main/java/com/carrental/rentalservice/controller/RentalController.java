package com.carrental.rentalservice.controller;

import com.carrental.rentalservice.dto.RentalRequest;
import com.carrental.rentalservice.model.Rental;
import com.carrental.rentalservice.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for rental operations.
 */
@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rental Controller", description = "API endpoints for managing car rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @Operation(summary = "Create a new rental", 
               description = "Creates a new rental/reservation. Requires car availability check and payment processing.")
    public ResponseEntity<Rental> createRental(@Valid @RequestBody RentalRequest request) {
        Rental rental = rentalService.createRental(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @GetMapping
    @Operation(summary = "Get all rentals", description = "Retrieves all rentals in the system")
    public ResponseEntity<List<Rental>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rental by ID", description = "Retrieves a specific rental by its ID")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get rentals by client ID", description = "Retrieves all rentals for a specific client")
    public ResponseEntity<List<Rental>> getRentalsByClientId(@PathVariable String clientId) {
        List<Rental> rentals = rentalService.getRentalsByClientId(clientId);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/car/{carId}")
    @Operation(summary = "Get rentals by car ID", description = "Retrieves all rentals for a specific car")
    public ResponseEntity<List<Rental>> getRentalsByCarId(@PathVariable Long carId) {
        List<Rental> rentals = rentalService.getRentalsByCarId(carId);
        return ResponseEntity.ok(rentals);
    }
}

