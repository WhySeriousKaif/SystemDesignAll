package com.ratelimiter.examples;

// Subject Interface
interface Account {
    void withdraw(double amount);
    String getAccountNumber();
}

// RealSubject
class BankAccount implements Account {
    private String accountNumber;
    
    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    @Override
    public void withdraw(double amount) {
        System.out.println("💰 [Bank] Withdrawing $" + amount + " from account " + accountNumber);
    }
    
    @Override
    public String getAccountNumber() {
        return accountNumber;
    }
}

// Proxy
class ATM implements Account {
    private BankAccount bankAccount;
    private String accountNumber;
    private String pin;
    
    public ATM(String accountNumber, String pin) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        // Lazy initialization is handled in the methods
    }
    
    private void ensureAccountInitialized() {
        if (bankAccount == null) {
            bankAccount = new BankAccount(accountNumber);
        }
    }

    @Override
    public void withdraw(double amount) {
        if (validatePin()) {
            System.out.println("✅ [ATM] PIN validated successfully");
            ensureAccountInitialized(); // Lazy Init
            bankAccount.withdraw(amount);  // Delegate
        } else {
            System.out.println("❌ [ATM] Invalid PIN! Transaction denied.");
        }
    }
    
    @Override
    public String getAccountNumber() {
        if (validatePin()) {
            ensureAccountInitialized();
            return bankAccount.getAccountNumber();
        }
        return "Access denied - Invalid PIN";
    }
    
    private boolean validatePin() {
        return "1234".equals(pin);
    }
}

public class ProxyExampleMain {
    public static void main(String[] args) {
        System.out.println("--- Starting ATM Proxy Example ---");
        Account atm = new ATM("987654321", "1234");
        atm.withdraw(500);
        
        System.out.println("\n--- Testing Invalid PIN ---");
        Account wrongAtm = new ATM("987654321", "0000");
        wrongAtm.withdraw(100);
    }
}
