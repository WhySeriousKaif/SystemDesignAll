package scaler.concept.strategy;

/**
 * Strategy Pattern Demonstration: Payment System
 */
public class StrategyPatternDemo {

    public static void main(String[] args) {
        PaymentContext cart = new PaymentContext();

        // 1. Client chooses Credit Card at runtime
        System.out.println("Scenario 1: User chooses Credit Card");
        cart.setPaymentStrategy(new CreditCardPayment());
        cart.checkout(1500.0);

        System.out.println("\n-----------------------------------\n");

        // 2. Client switches to UPI at runtime (Interchangeable!)
        System.out.println("Scenario 2: User switches to UPI");
        cart.setPaymentStrategy(new UPIPayment());
        cart.checkout(2500.0);
    }

    // --- 1. THE STRATEGY INTERFACE (The Menu) ---
    interface PaymentStrategy {
        void pay(double amount);
    }

    // --- 2. CONCRETE STRATEGIES (The Food Items) ---
    static class CreditCardPayment implements PaymentStrategy {
        @Override
        public void pay(double amount) {
            System.out.println("Processing ₹" + amount + " via Secure Credit Card Gateway...");
        }
    }

    static class UPIPayment implements PaymentStrategy {
        @Override
        public void pay(double amount) {
            System.out.println("Processing ₹" + amount + " via BHIM UPI (Scanning QR)...");
        }
    }

    static class PayPalPayment implements PaymentStrategy {
        @Override
        public void pay(double amount) {
            System.out.println("Processing ₹" + amount + " via PayPal Wallet...");
        }
    }

    // --- 3. THE CONTEXT (The Waiter) ---
    static class PaymentContext {
        private PaymentStrategy strategy;

        // Allows changing behavior at runtime!
        public void setPaymentStrategy(PaymentStrategy strategy) {
            this.strategy = strategy;
        }

        public void checkout(double amount) {
            if (strategy == null) {
                System.out.println("Error: Please select a payment method first!");
                return;
            }
            strategy.pay(amount); // Delegation to the selected strategy
        }
    }
}
