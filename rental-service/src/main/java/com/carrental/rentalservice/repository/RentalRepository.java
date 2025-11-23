package com.carrental.rentalservice.repository;

import com.carrental.rentalservice.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Rental entities.
 */
public interface RentalRepository extends JpaRepository<Rental, Long> {

    /**
     * Find rentals by client ID.
     */
    List<Rental> findByClientId(String clientId);

    /**
     * Find rentals by car ID.
     */
    List<Rental> findByCarId(Long carId);

    /**
     * Find active rentals for a specific car within a date range.
     */
    @Query("SELECT r FROM Rental r WHERE r.carId = :carId " +
           "AND r.status = 'ACTIVE' " +
           "AND ((r.startDate <= :endDate AND r.endDate >= :startDate))")
    List<Rental> findActiveRentalsForCarInDateRange(
            @Param("carId") Long carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

