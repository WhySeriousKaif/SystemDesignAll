package parking_lot_assignment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class HourlyPricingStrategy implements PricingStrategy {
    private static final double BASE_PRICE = 10.0;
    private static final double HOURLY_RATE = 5.0;

    @Override
    public double calculateAmount(Receipt ticket, LocalDateTime exitTime) {
        long durationInHours = ChronoUnit.HOURS.between(ticket.getEntryTime(), exitTime);
        // Ensuring minimum 1 hour is billed
        if (durationInHours == 0) {
            durationInHours = 1;
        }
        
        return BASE_PRICE + (durationInHours * HOURLY_RATE);
    }
}
