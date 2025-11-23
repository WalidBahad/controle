package com.carrental.rentalservice.client;

import com.carrental.rentalservice.dto.PaymentRequest;
import com.carrental.rentalservice.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Implementation of PaymentServiceClient using WebClient.
 */
@Component
public class PaymentServiceClientImpl implements PaymentServiceClient {

    private final WebClient webClient;

    public PaymentServiceClientImpl(
            WebClient.Builder webClientBuilder,
            @Value("${payment.service.url:http://localhost:8083}") String paymentServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(paymentServiceUrl)
                .build();
    }

    @Override
    public Mono<PaymentResponse> processPayment(PaymentRequest request) {
        return webClient.post()
                .uri("/api/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .onErrorMap(Exception.class, ex -> new RuntimeException("Payment service error: " + ex.getMessage(), ex));
    }
}

