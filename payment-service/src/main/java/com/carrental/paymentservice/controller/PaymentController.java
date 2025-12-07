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
 * Contrôleur REST pour les opérations de paiement.
 * Fournit des points de terminaison pour traiter les paiements via Stripe ou PayPal (implémentation fictive).
 */
@RestController
@RequestMapping("/api/payments")
@Tag(name = "Contrôleur de Paiement", description = "Points de terminaison API pour le traitement des paiements")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    @Operation(summary = "Traiter le paiement", 
               description = "Traite un paiement en utilisant Stripe ou le sandbox PayPal (implémentation fictive)")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Vérification de santé", description = "Vérifie si le service de paiement est en cours d'exécution")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment service is running");
    }
}

