package com.carrental.carservice.config;

import com.carrental.carservice.model.Car;
import com.carrental.carservice.repository.CarRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Configuration class to initialize example data for testing.
 * Uses ApplicationReadyEvent which fires after all initialization is complete.
 */
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final CarRepository carRepository;
    private final PlatformTransactionManager transactionManager;
    private boolean initialized = false;

    public DataInitializer(CarRepository carRepository, PlatformTransactionManager transactionManager) {
        this.carRepository = carRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized) {
            return;
        }
        
        // Use manual transaction management to have more control
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // Check if data already exists
            long count = carRepository.count();
            if (count == 0) {
                // Initialize sample cars
                carRepository.save(new Car("Toyota", "Camry", 2023, Car.CarStatus.AVAILABLE, 50.00));
                carRepository.save(new Car("Honda", "Accord", 2022, Car.CarStatus.AVAILABLE, 45.00));
                carRepository.save(new Car("Ford", "Mustang", 2023, Car.CarStatus.RENTED, 75.00));
                carRepository.save(new Car("BMW", "X5", 2023, Car.CarStatus.AVAILABLE, 120.00));
                carRepository.save(new Car("Mercedes-Benz", "C-Class", 2022, Car.CarStatus.AVAILABLE, 110.00));
                carRepository.save(new Car("Tesla", "Model 3", 2023, Car.CarStatus.AVAILABLE, 95.00));
                
                transactionManager.commit(status);
                System.out.println("Sample cars initialized in the database.");
            } else {
                transactionManager.commit(status);
                System.out.println("Database already contains " + count + " cars.");
            }
            initialized = true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            System.err.println("Could not initialize sample data: " + e.getMessage());
            // Don't fail startup - data can be added manually via REST API
            initialized = true; // Mark as initialized to prevent retries
        }
    }
}

