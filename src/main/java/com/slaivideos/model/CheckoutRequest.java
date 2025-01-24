package com.slaivideos.model;

public class CheckoutRequest {
    private String title;
    private double price;

    // Construtor vazio necessário para serialização JSON
    public CheckoutRequest() {
    }

    // Construtor com parâmetros
    public CheckoutRequest(String title, double price) {
        this.title = title;
        this.price = price;
    }

    // Métodos Getter e Setter
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
