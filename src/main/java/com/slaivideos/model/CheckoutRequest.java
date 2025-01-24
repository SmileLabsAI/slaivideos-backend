package com.slaivideos.model;

public class  CheckoutRequest {
    private String title;
    private double price;

    // Construtor vazio (necessário para serialização)
    public CheckoutRequest() {}

    // Getters e Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}