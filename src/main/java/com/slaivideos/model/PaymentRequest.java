package com.slaivideos.model;

public class PaymentRequest {
    private double amount;
    private String currency;
    private String paymentMethod;

    // Construtor vazio para serialização
    public PaymentRequest() {}

    // Construtor com parâmetros
    public PaymentRequest(double amount, String currency, String paymentMethod) {
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
