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
 * REST controller for analytics operations.
 */
@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics Controller", description = "API endpoints for car occupancy analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/occupancy")
    @Operation(summary = "Get occupancy rates for all cars", 
               description = "Calculates and returns occupancy rates (percentage of days rented) for all cars over a specified period. Defaults to last 30 days if no dates provided.")
    public ResponseEntity<List<OccupancyRate>> getOccupancyRates(
            @Parameter(description = "Start date (format: yyyy-MM-dd). Defaults to 30 days ago if not provided.")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (format: yyyy-MM-dd). Defaults to today if not provided.")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<OccupancyRate> occupancyRates = analyticsService.calculateOccupancyRates(startDate, endDate);
        return ResponseEntity.ok(occupancyRates);
    }

    @GetMapping("/occupancy/car/{carId}")
    @Operation(summary = "Get occupancy rate for a specific car", 
               description = "Calculates and returns occupancy rate (percentage of days rented) for a specific car over a specified period. Defaults to last 30 days if no dates provided.")
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

