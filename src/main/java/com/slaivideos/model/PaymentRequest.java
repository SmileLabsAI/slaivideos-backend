package com.slaivideos.model;

public class PaymentRequest {
    private final String title;
    private final double amount;

    // Construtor para inicializar os valores (evita setters desnecessários)
    public PaymentRequest(String title, double amount) {
        this.title = title;
        this.amount = amount;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }
}
