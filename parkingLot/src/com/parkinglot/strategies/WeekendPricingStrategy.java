package com.parkinglot.strategies;

import com.parkinglot.models.Ticket;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class WeekendPricingStrategy implements PricingStrategy {
    private PricingStrategy base;

    public WeekendPricingStrategy(PricingStrategy base) {
        this.base = base;
    }

    @Override
    public double calculateAmount(Ticket t, LocalDateTime exit) {
        double amt = base.calculateAmount(t, exit);
        DayOfWeek d = exit.getDayOfWeek();
        if (d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY) {
            return amt * 1.5;
        }
        return amt;
    }
}
