package BookMyShow.Service;

import BookMyShow.controllers.MovieController;
import BookMyShow.controllers.TheaterController;
import BookMyShow.enums.BookingStatus;
import BookMyShow.enums.SeatStatus;
import BookMyShow.theaters.Show;
import BookMyShow.theaters.ShowSeat;
import BookMyShow.users.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingService {
    private static BookingService instance;

    private MovieController movieController = new MovieController();
    private TheaterController theaterController = new TheaterController();

    private PaymentService paymentService;
    private RefundService refundService;
    private PricingStrategy pricingStrategy;

    private Map<ShowSeat, SeatLock> locks = new ConcurrentHashMap<>();

    private BookingService(PaymentService p, RefundService r, PricingStrategy pr) {
        this.paymentService = p;
        this.refundService = r;
        this.pricingStrategy = pr;
    }

    public static BookingService getInstance(PaymentService p, RefundService r, PricingStrategy pr) {
        if (instance == null) instance = new BookingService(p, r, pr);
        return instance;
    }

    public MovieController getMovieController() { return movieController; }
    public TheaterController getTheaterController() { return theaterController; }

    // 🔥 LOCK SEATS
    public synchronized List<ShowSeat> lockSeats(Show show, List<String> seatNumbers, Customer user) {
        List<ShowSeat> lockedSeats = new ArrayList<>();

        for (String num : seatNumbers) {
            ShowSeat seat = show.getSeatMap().get(num);

            if (seat == null || seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException("❌ Seat not available: " + num);
            }

            seat.setStatus(SeatStatus.LOCKED);
            locks.put(seat, new SeatLock(seat, user, 300)); // 5 min TTL
            lockedSeats.add(seat);
        }
        return lockedSeats;
    }

    // 🔥 CONFIRM BOOKING
    public Booking confirmBooking(Customer user, Show show, List<ShowSeat> seats) {
        double total = 0;

        for (ShowSeat seat : seats) {
            total += pricingStrategy.calculatePrice(seat, show);
        }

        if (!paymentService.processPayment(total)) {
            releaseSeats(seats);
            throw new RuntimeException("❌ Payment failed");
        }

        for (ShowSeat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
            locks.remove(seat);
        }

        Booking booking = new Booking(show, seats);
        booking.setStatus(BookingStatus.CONFIRMED);
        return booking;
    }

    // 🔥 RELEASE LOCK
    public void releaseSeats(List<ShowSeat> seats) {
        for (ShowSeat seat : seats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            locks.remove(seat);
        }
    }

    // 🔥 AUTO CLEANUP
    public void cleanExpiredLocks() {
        locks.values().removeIf(lock -> {
            if (lock.isExpired()) {
                lock.getSeat().setStatus(SeatStatus.AVAILABLE);
                return true;
            }
            return false;
        });
    }
}
