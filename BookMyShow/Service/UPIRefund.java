package BookMyShow.Service;

public class UPIRefund implements RefundService {
    @Override
    public boolean processRefund(String id, double amount) {
        System.out.println("💵 Refund processed for " + id + ": ₹" + amount);
        return true;
    }
}
