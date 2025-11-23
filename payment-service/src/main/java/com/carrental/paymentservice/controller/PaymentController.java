package com.carrental.paymentservice.controller;

import com.carrental.paymentservice.dto.PaymentRequest;
import com.carrental.paymentservice.dto.PaymentResponse;
import com.carrental.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for payment operations.
 * Provides endpoints for processing payments via Stripe or PayPal (mock implementation).
 */
@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Controller", description = "API endpoints for processing payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    @Operation(summary = "Process payment", 
               description = "Processes a payment using Stripe or PayPal sandbox (mock implementation)")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if payment service is running")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment service is running");
    }
}

