package com.carrental.paymentservice.service;

import com.carrental.paymentservice.dto.PaymentRequest;
import com.carrental.paymentservice.dto.PaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.Random;

/**
 * Service for processing payments.
 * Simulates payment processing using Stripe or PayPal sandbox.
 * This is a mock implementation for demonstration purposes.
 */
@Service
public class PaymentService {

    private final Random random = new Random();

    /**
     * Process a payment request.
     * Simulates payment processing with Stripe or PayPal.
     * 
     * @param request Payment request containing method, amount, client ID, and description
     * @return PaymentResponse with payment ID and status
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        // Validate payment method
        String paymentMethod = request.getPaymentMethod().toLowerCase();
        if (!paymentMethod.equals("stripe") && !paymentMethod.equals("paypal")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Unsupported payment method. Use 'stripe' or 'paypal'");
        }

        // Simulate payment processing delay
        try {
            Thread.sleep(500); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate random payment failures (10% failure rate for demonstration)
        boolean paymentSuccessful = random.nextDouble() > 0.1;

        if (!paymentSuccessful) {
            return new PaymentResponse(
                null,
                "FAILED",
                "Payment processing failed. Please try again or use a different payment method.",
                request.getAmount(),
                paymentMethod
            );
        }

        // Generate mock payment ID
        String paymentId = generatePaymentId(paymentMethod);

        return new PaymentResponse(
            paymentId,
            "SUCCESS",
            String.format("Payment processed successfully via %s", paymentMethod.toUpperCase()),
            request.getAmount(),
            paymentMethod
        );
    }

    /**
     * Generate a mock payment ID based on the payment method.
     */
    private String generatePaymentId(String paymentMethod) {
        String prefix = paymentMethod.equals("stripe") ? "ch_" : "PP-";
        return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }
}

