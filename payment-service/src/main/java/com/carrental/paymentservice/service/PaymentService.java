package com.carrental.paymentservice.service;

import com.carrental.paymentservice.dto.PaymentRequest;
import com.carrental.paymentservice.dto.PaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.Random;

/**
 * Service de traitement des paiements.
 * Simule le traitement des paiements à l'aide de Stripe ou du bac à sable PayPal.
 * Il s'agit d'une implémentation fictive à des fins de démonstration.
 */
@Service
public class PaymentService {

    private final Random random = new Random();

    /**
     * Traiter une demande de paiement.
     * Simule le traitement des paiements avec Stripe ou PayPal.
     * 
     * @param request Demande de paiement contenant la méthode, le montant, l'ID client et la description
     * @return PaymentResponse avec ID de paiement et statut
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        // Valider la méthode de paiement
        String paymentMethod = request.getPaymentMethod().toLowerCase();
        if (!paymentMethod.equals("stripe") && !paymentMethod.equals("paypal")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Unsupported payment method. Use 'stripe' or 'paypal'");
        }

        // Simuler le délai de traitement du paiement
        try {
            Thread.sleep(500); // Simuler un délai réseau
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simuler des échecs de paiement aléatoires (taux d'échec de 10% pour la démonstration)
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

        // Générer un ID de paiement fictif
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
     * Générer un ID de paiement fictif basé sur la méthode de paiement.
     */
    private String generatePaymentId(String paymentMethod) {
        String prefix = paymentMethod.equals("stripe") ? "ch_" : "PP-";
        return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }
}

