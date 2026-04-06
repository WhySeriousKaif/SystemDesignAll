package BookMyShow.Service;

public class UPIPayment implements PaymentService {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("💳 UPI Payment Success: ₹" + amount);
        return true;
    }
}
