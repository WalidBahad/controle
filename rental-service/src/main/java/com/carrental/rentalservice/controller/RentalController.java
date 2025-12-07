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
 * Contrôleur REST pour les opérations de location.
 */
@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Contrôleur de Location", description = "Points de terminaison API pour la gestion des locations de voitures")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle location", 
               description = "Crée une nouvelle location/réservation. Nécessite une vérification de la disponibilité de la voiture et un traitement du paiement.")
    public ResponseEntity<Rental> createRental(@Valid @RequestBody RentalRequest request) {
        Rental rental = rentalService.createRental(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @GetMapping
    @Operation(summary = "Obtenir toutes les locations", description = "Récupère toutes les locations dans le système")
    public ResponseEntity<List<Rental>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une location par ID", description = "Récupère une location spécifique par son ID")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Obtenir les locations par ID client", description = "Récupère toutes les locations pour un client spécifique")
    public ResponseEntity<List<Rental>> getRentalsByClientId(@PathVariable String clientId) {
        List<Rental> rentals = rentalService.getRentalsByClientId(clientId);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/car/{carId}")
    @Operation(summary = "Obtenir les locations par ID voiture", description = "Récupère toutes les locations pour une voiture spécifique")
    public ResponseEntity<List<Rental>> getRentalsByCarId(@PathVariable Long carId) {
        List<Rental> rentals = rentalService.getRentalsByCarId(carId);
        return ResponseEntity.ok(rentals);
    }
}

