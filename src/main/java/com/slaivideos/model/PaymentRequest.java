package com.slaivideos.model;

public class PaymentRequest {
    private double amount;
    private String currency;
    private String paymentMethod;

    public PaymentRequest() {}  // Construtor vazio para serialização

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
