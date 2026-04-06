package BookMyShow.Service;

import BookMyShow.enums.BookingStatus;
import BookMyShow.theaters.Show;
import BookMyShow.theaters.ShowSeat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Booking {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private String bookingId;
    private List<ShowSeat> seats;
    private Show show;
    private BookingStatus status;

    public Booking(Show show, List<ShowSeat> seats) {
        this.bookingId = "B-" + counter.incrementAndGet();
        this.show = show;
        this.seats = seats;
        this.status = BookingStatus.CREATED;
    }

    public String getBookingId() { return bookingId; }
    public List<ShowSeat> getSeats() { return seats; }
    public Show getShow() { return show; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
