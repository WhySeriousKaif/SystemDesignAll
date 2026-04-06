package BookMyShow.Service;

public interface RefundService {
    boolean processRefund(String bookingId, double amount);
}
