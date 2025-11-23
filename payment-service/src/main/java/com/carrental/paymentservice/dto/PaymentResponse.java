package com.carrental.paymentservice.dto;

/**
 * DTO for payment processing response.
 */
public class PaymentResponse {
    private String paymentId;
    private String status;
    private String message;
    private Double amount;
    private String paymentMethod;

    public PaymentResponse() {
    }

    public PaymentResponse(String paymentId, String status, String message, Double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.status = status;
        this.message = message;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

