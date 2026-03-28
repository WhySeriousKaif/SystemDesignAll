package com.parkinglot.strategies;

import com.parkinglot.models.Ticket;
import java.time.Duration;
import java.time.LocalDateTime;

public class HourlyPricingStrategy implements PricingStrategy {
    @Override
    public double calculateAmount(Ticket t, LocalDateTime exit) {
        long hours = Duration.between(t.getEntryTime(), exit).toHours();
        if (hours == 0) hours = 1;
        return 10 + hours * 5;
    }
}
