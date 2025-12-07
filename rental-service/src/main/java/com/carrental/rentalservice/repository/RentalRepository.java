package com.carrental.rentalservice.repository;

import com.carrental.rentalservice.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour les entités de location.
 */
public interface RentalRepository extends JpaRepository<Rental, Long> {

    /**
     * Trouver les locations par ID client.
     */
    List<Rental> findByClientId(String clientId);

    /**
     * Trouver les locations par ID voiture.
     */
    List<Rental> findByCarId(Long carId);

    /**
     * Trouver les locations actives pour une voiture spécifique dans une plage de dates.
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

