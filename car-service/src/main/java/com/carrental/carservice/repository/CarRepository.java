package com.carrental.carservice.repository;

import com.carrental.carservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Spring Data REST repository for Car entities.
 * Automatically exposes CRUD operations via REST API.
 */
@RepositoryRestResource(collectionResourceRel = "cars", path = "cars")
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Find cars by status (available/rented).
     * Exposed at: GET /cars/search/findByStatus?status=AVAILABLE
     */
    @RestResource(path = "status", rel = "status")
    List<Car> findByStatus(@Param("status") Car.CarStatus status);

    /**
     * Find cars by brand.
     * Exposed at: GET /cars/search/findByBrand?brand=Toyota
     */
    @RestResource(path = "brand", rel = "brand")
    List<Car> findByBrand(@Param("brand") String brand);
}

