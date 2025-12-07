package com.carrental.analyticsservice.controller;

import com.carrental.analyticsservice.dto.OccupancyRate;
import com.carrental.analyticsservice.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST pour les opérations d'analyse.
 */
@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Contrôleur d'Analyse", description = "Points de terminaison API pour l'analyse de l'occupation des voitures")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/occupancy")
    @Operation(summary = "Obtenir les taux d'occupation pour toutes les voitures", 
               description = "Calcule et renvoie les taux d'occupation (pourcentage de jours loués) pour toutes les voitures sur une période spécifiée. La valeur par défaut est les 30 derniers jours si aucune date n'est fournie.")
    public ResponseEntity<List<OccupancyRate>> getOccupancyRates(
            @Parameter(description = "Start date (format: yyyy-MM-dd). Defaults to 30 days ago if not provided.")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (format: yyyy-MM-dd). Defaults to today if not provided.")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<OccupancyRate> occupancyRates = analyticsService.calculateOccupancyRates(startDate, endDate);
        return ResponseEntity.ok(occupancyRates);
    }

    @GetMapping("/occupancy/car/{carId}")
    @Operation(summary = "Obtenir le taux d'occupation pour une voiture spécifique", 
               description = "Calcule et renvoie le taux d'occupation (pourcentage de jours loués) pour une voiture spécifique sur une période spécifiée. La valeur par défaut est les 30 derniers jours si aucune date n'est fournie.")
    public ResponseEntity<OccupancyRate> getOccupancyRateForCar(
            @PathVariable Long carId,
            @Parameter(description = "Start date (format: yyyy-MM-dd). Defaults to 30 days ago if not provided.")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (format: yyyy-MM-dd). Defaults to today if not provided.")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        OccupancyRate occupancyRate = analyticsService.calculateOccupancyRateForCar(carId, startDate, endDate);
        return ResponseEntity.ok(occupancyRate);
    }
}

