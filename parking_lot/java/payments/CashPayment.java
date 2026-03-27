package parking_lot.java.payments;

public class CashPayment implements PaymentStrategy{
    
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing cash payment of amount: " + amount);
    }
}
