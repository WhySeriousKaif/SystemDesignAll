package BookMyShow.theaters;

import BookMyShow.enums.SeatType;

public class Seat {
    private String seatNumber;
    private SeatType type;

    public Seat(String seatNumber, SeatType type) {
        this.seatNumber = seatNumber;
        this.type = type;
    }

    public String getSeatNumber() { return seatNumber; }
    public SeatType getType() { return type; }
}
