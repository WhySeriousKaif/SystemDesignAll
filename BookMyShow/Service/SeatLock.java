package BookMyShow.Service;

import BookMyShow.theaters.ShowSeat;
import BookMyShow.users.Customer;
import java.time.Duration;
import java.time.LocalDateTime;

public class SeatLock {
    private ShowSeat seat;
    private Customer user;
    private LocalDateTime lockTime;
    private int timeoutSec;

    public SeatLock(ShowSeat seat, Customer user, int timeoutSec) {
        this.seat = seat;
        this.user = user;
        this.lockTime = LocalDateTime.now();
        this.timeoutSec = timeoutSec;
    }

    public boolean isExpired() {
        return Duration.between(lockTime, LocalDateTime.now()).getSeconds() > timeoutSec;
    }

    public ShowSeat getSeat() { return seat; }
    public Customer getUser() { return user; }
}
