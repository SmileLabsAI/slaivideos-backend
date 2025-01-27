package com.slaivideos.model;

import java.util.List;

public class PaymentRequest {

    private List<Item> items;

    public PaymentRequest(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public static class Item {
        private String title;
        private double unit_price;
        private int quantity;

        public Item(String title, double unit_price, int quantity) {
            this.title = title;
            this.unit_price = unit_price;
            this.quantity = quantity;
        }

        public String getTitle() {
            return title;
        }

        public double getUnit_price() {
            return unit_price;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
