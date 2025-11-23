package com.carrental.rentalservice.client;

import com.carrental.rentalservice.dto.PaymentRequest;
import com.carrental.rentalservice.dto.PaymentResponse;
import reactor.core.publisher.Mono;

/**
 * WebClient-based service for communicating with payment-service.
 * Using reactive WebClient for non-blocking communication.
 */
public interface PaymentServiceClient {
    
    /**
     * Process payment through payment-service.
     */
    Mono<PaymentResponse> processPayment(PaymentRequest request);
}

