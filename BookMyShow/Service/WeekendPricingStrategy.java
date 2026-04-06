package BookMyShow.Service;

import BookMyShow.theaters.Show;
import BookMyShow.theaters.ShowSeat;
import java.time.DayOfWeek;

public class WeekendPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(ShowSeat seat, Show show) {
        if (show.getStartTime().getDayOfWeek() == DayOfWeek.SATURDAY ||
            show.getStartTime().getDayOfWeek() == DayOfWeek.SUNDAY) {
            return seat.getPrice() * 1.2;
        }
        return seat.getPrice();
    }
}
