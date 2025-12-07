package com.carrental.carservice.repository;

import com.carrental.carservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Repository Spring Data REST pour les entités de voiture.
 * Expose automatiquement les opérations CRUD via l'API REST.
 */
@RepositoryRestResource(collectionResourceRel = "cars", path = "cars")
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Trouver les voitures par statut (disponible/loué).
     * Exposé à : GET /cars/search/findByStatus?status=AVAILABLE
     */
    @RestResource(path = "status", rel = "status")
    List<Car> findByStatus(@Param("status") Car.CarStatus status);

    /**
     * Trouver les voitures par marque.
     * Exposé à : GET /cars/search/findByBrand?brand=Toyota
     */
    @RestResource(path = "brand", rel = "brand")
    List<Car> findByBrand(@Param("brand") String brand);
}

