package parking_lot_assignment;

import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculateAmount(Receipt ticket, LocalDateTime exitTime);
}
