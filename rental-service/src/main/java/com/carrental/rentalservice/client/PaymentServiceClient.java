package com.carrental.rentalservice.client;

import com.carrental.rentalservice.dto.PaymentRequest;
import com.carrental.rentalservice.dto.PaymentResponse;
import reactor.core.publisher.Mono;

/**
 * Service basé sur WebClient pour communiquer avec payment-service.
 * Utilise WebClient réactif pour une communication non bloquante.
 */
public interface PaymentServiceClient {
    
    /**
     * Traiter le paiement via payment-service.
     */
    Mono<PaymentResponse> processPayment(PaymentRequest request);
}

