package parking_lot_assignment;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class WeekendPricingStrategy implements PricingStrategy {
    private static final double WEEKEND_MULTIPLIER = 1.5;
    private PricingStrategy baseStrategy;

    public WeekendPricingStrategy(PricingStrategy baseStrategy) {
        this.baseStrategy = baseStrategy;
    }

    @Override
    public double calculateAmount(Receipt ticket, LocalDateTime exitTime) {
        double baseAmount = baseStrategy.calculateAmount(ticket, exitTime);
        if (isWeekend(exitTime)) {
            return baseAmount * WEEKEND_MULTIPLIER;
        }
        return baseAmount;
    }

    private boolean isWeekend(LocalDateTime time) {
        DayOfWeek day = time.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
