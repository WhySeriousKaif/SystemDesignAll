package parking_lot.java.payments;

public class Payment {
    private double amount;
    private PaymentStrategy paymentStrategy;

    public Payment(double amount,PaymentStrategy paymentStrategy){
        this.amount=amount;
        this.paymentStrategy=paymentStrategy;
    }
    public void processPayment(){
        if(amount>0){
            paymentStrategy.processPayment(amount); //Delegating the task to the strategy object
        }
        else System.out.println("Invalid Payment Amount");
    }
    

    
    
}
