package com.parkinglot.strategies;

import com.parkinglot.models.Ticket;
import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculateAmount(Ticket t, LocalDateTime exit);
}
