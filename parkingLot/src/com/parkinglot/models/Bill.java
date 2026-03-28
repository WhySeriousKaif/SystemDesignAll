package com.parkinglot.models;

import java.time.LocalDateTime;

public class Bill {
    private double amount;
    private LocalDateTime exitTime;

    public Bill(double amount) {
        this.amount = amount;
        this.exitTime = LocalDateTime.now();
    }

    public double getAmount() { return amount; }
    public LocalDateTime getExitTime() { return exitTime; }
}
