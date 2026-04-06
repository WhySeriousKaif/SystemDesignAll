package BookMyShow.theaters;

import BookMyShow.enums.SeatStatus;

public class ShowSeat {
    private Seat seat;
    private volatile SeatStatus status;
    private double price;

    public ShowSeat(Seat seat, double price) {
        this.seat = seat;
        this.price = price;
        this.status = SeatStatus.AVAILABLE;
    }

    public Seat getSeat() { return seat; }
    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
