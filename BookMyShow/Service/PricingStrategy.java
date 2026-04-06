package BookMyShow.Service;

import BookMyShow.theaters.Show;
import BookMyShow.theaters.ShowSeat;

public interface PricingStrategy {
    double calculatePrice(ShowSeat seat, Show show);
}
