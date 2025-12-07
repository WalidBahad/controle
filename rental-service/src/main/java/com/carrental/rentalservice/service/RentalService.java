package com.carrental.rentalservice.service;

import com.carrental.rentalservice.client.CarServiceClient;
import com.carrental.rentalservice.client.PaymentServiceClient;
import com.carrental.rentalservice.dto.*;
import com.carrental.rentalservice.model.Rental;
import com.carrental.rentalservice.repository.RentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service de gestion des locations de voitures.
 * Gère la logique métier, y compris les vérifications de disponibilité des voitures et le traitement des paiements.
 */
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarServiceClient carServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    public RentalService(RentalRepository rentalRepository,
                        CarServiceClient carServiceClient,
                        PaymentServiceClient paymentServiceClient) {
        this.rentalRepository = rentalRepository;
        this.carServiceClient = carServiceClient;
        this.paymentServiceClient = paymentServiceClient;
    }

    /**
     * Créer une nouvelle location/réservation.
     * Vérifie la disponibilité de la voiture et traite le paiement avant de confirmer la réservation.
     */
    @Transactional
    public Rental createRental(RentalRequest request) {
        // Les validations de dates sont maintenant gérées par les annotations @ValidDateRange et @FutureOrPresent

        // Vérifier la disponibilité de la voiture via FeignClient
        ResponseEntity<Car> carResponse = carServiceClient.getCarById(request.getCarId());
        
        if (carResponse.getStatusCode() != HttpStatus.OK || carResponse.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Car not found with ID: " + request.getCarId());
        }

        Car car = carResponse.getBody();

        // Vérifier si la voiture est disponible
        if (!"AVAILABLE".equalsIgnoreCase(car.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Car is not available for rental");
        }

        // Vérifier les locations chevauchantes
        List<Rental> overlappingRentals = rentalRepository
            .findActiveRentalsForCarInDateRange(
                request.getCarId(), 
                request.getStartDate(), 
                request.getEndDate()
            );

        if (!overlappingRentals.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Car is already rented for the requested dates");
        }

        // Calculer le montant total
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        Double totalAmount = car.getPricePerDay() * days;

        // Traiter le paiement via WebClient
        PaymentRequest paymentRequest = new PaymentRequest(
            "stripe", // Méthode de paiement par défaut
            totalAmount,
            request.getClientId(),
            String.format("Rental for car %d (%s %s)", car.getId(), car.getBrand(), car.getModel())
        );

        try {
            PaymentResponse paymentResponse = paymentServiceClient.processPayment(paymentRequest)
                .block(); // Appel bloquant pour traitement synchrone

            if (paymentResponse == null || !"SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
                throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, 
                    "Payment processing failed: " + (paymentResponse != null ? paymentResponse.getMessage() : "Unknown error"));
            }

            // Créer la location
            Rental rental = new Rental();
            rental.setCarId(request.getCarId());
            rental.setClientId(request.getClientId());
            rental.setStartDate(request.getStartDate());
            rental.setEndDate(request.getEndDate());
            rental.setStatus(Rental.RentalStatus.ACTIVE);
            rental.setPaymentId(paymentResponse.getPaymentId());
            rental.setTotalAmount(totalAmount);

            Rental savedRental = rentalRepository.save(rental);

            // Mettre à jour le statut de la voiture à LOUE
            car.setStatus("RENTED");
            // Définir l'ID explicitement car il peut être nul dans la réponse de Spring Data REST
            car.setId(request.getCarId());
            carServiceClient.updateCar(request.getCarId(), car);

            return savedRental;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error processing payment: " + e.getMessage(), e);
        }
    }

    /**
     * Obtenir toutes les locations.
     */
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Obtenir une location par ID.
     */
    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Rental not found with ID: " + id));
    }

    /**
     * Obtenir les locations par ID client.
     */
    public List<Rental> getRentalsByClientId(String clientId) {
        return rentalRepository.findByClientId(clientId);
    }

    /**
     * Obtenir les locations par ID voiture.
     */
    public List<Rental> getRentalsByCarId(Long carId) {
        return rentalRepository.findByCarId(carId);
    }
}

