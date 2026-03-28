package com.parkinglot.services;

public class PaymentService {
    public void processPayment(double amount) {
        System.out.println("Processing Payment: $" + String.format("%.2f", amount));
    }
}
