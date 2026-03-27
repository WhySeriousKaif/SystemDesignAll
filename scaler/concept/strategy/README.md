# Strategy Design Pattern - Revision Guide (viva-Ready)

The **Strategy Pattern** is a behavioral design pattern that allows an object to **change its behavior at runtime** by swapping encapsulated algorithms.

---

## 🍴 1. The Restaurant Analogy
Think of a Restaurant:
- **Strategy Interface**: The **Menu** (defines what can be ordered).
- **Concrete Strategies**: **Pizza**, **Pasta**, **Salad** (specific implementations of "Food").
- **Context**: The **Waiter** (holds your choice and executes the order).
- **Client**: **You (The Customer)** (choose what to eat at runtime).

**Key Insight**: The Waiter doesn't decide the food; they just execute whatever "strategy" the Customer chooses.

---

## 🏗️ 2. The Four Core Components

| Component | Role | Description |
| :--- | :--- | :--- |
| **Strategy Interface** | **Contract** | Defines the common method for all algorithms. |
| **Concrete Strategy** | **Implementation** | The actual logic for a specific version of the algorithm. |
| **Context Class** | **Delegator** | Holds a reference to a Strategy and calls its method. |
| **Client** | **Decision Maker** | Instantiates the specific strategy and gives it to the Context. |

---

## 🏢 3. Scaler Class Examples

The Strategy Pattern was used extensively in our LLD classes to solve the **"Inheritance Explosion"** problem.

### A. MyList Sorting & Printing (`class6`)
- **Context**: `MyListImpl`
- **Strategies**: `ISortAlgorithm` (QuickSort, BubbleSort), `IPrintAlgorithm` (Vertical, Horizontal)
- **Benefit**: We can create any combination of (Sorting + Printing) without creating millions of subclasses.

### B. Pen Design System (`class9`)
- **Context**: `Pen`
- **Strategies**: `RefillStrategy` (Cartridge, InkBottle), `StartStrategy` (Cap, Click)
- **Benefit**: A pen's refill and opening mechanism can be swapped independently.

### C. Snake & Ladder (`class9`)
- **Context**: `Game`
- **Strategy**: `MoveStrategy` (EasyMove, HardMove)
- **Benefit**: Change game rules (e.g., whether a '6' gives another turn) at runtime.

---

## 💻 4. Code Implementation (Payment Example)

### 4.1 Strategy Interface
```java
public interface PaymentStrategy {
    void pay(double amount);
}
```

### 4.2 Concrete Strategies
```java
public class CreditCardPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("Paid ₹" + amount + " via Card"); }
}

public class UPIPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("Paid ₹" + amount + " via UPI"); }
}
```

### 4.3 Context Class
```java
public class PaymentContext {
    private PaymentStrategy strategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy; // Interchangeable at runtime!
    }

    public void checkout(double amount) {
        strategy.pay(amount); // Delegation
    }
}
```

---

## 🎯 5. Summary for Interviews
- **When to use?** When you have many variations of an algorithm and don't want to use huge `if-else` or `switch` blocks.
- **Why use?** Adheres to **OCP** (add new strategies without changing context) and **SRP** (logic is isolated).
- **Core Principle**: **Composition over Inheritance**.

---
*Created for viva preparation using Scaler LLD session notes.*
