# SOLID Principles - Revision Guide (viva-Ready)

This guide summarizes the **5 key design principles** discussed in the Scaler LLD classes. These principles are essential for building maintainable, flexible, and reliable systems like the **HR Management System**, **Pen Design System**, and **Snake & Ladder**.

---

## 🤔 Why SOLID?
- **Aim of LLD**: To write code that is **easier to understand**, **easier to extend**, and **easier to maintain**.
- **SOLID** is a set of 5 design principles that, when followed, organically give us these desired structural qualities.

---

## 🏗️ 1. Single Responsibility Principle (SRP)
> **"A class should have only one reason to change."**

In simple terms: A class should do **one thing** and do it well.

### 🏢 Scaler Example: HR Management System (`class3`)
Initially, the `Employee` class had a `save()` method that handled serialization and file writing.
- **Problem**: Changing the database or the file format would require modifying the `Employee` class.
- **Fix**: Move `save()` logic to `EmployeeRepository`.

```java
// ✅ SRP: Employee handles only data
class Employee {
    private int id;
    private String name;
    // Getters and Setters...
}

// ✅ SRP: Repository handles storage only
class EmployeeRepository {
    public void save(Employee e) {
        // Database/File logic here
    }
}
```

### 🚨 Quick Checks for SRP Violations
1. **Multiple `if/else` statements**: Usually indicates a method handling multiple behaviors.
2. **"God" or Monster Methods**: Methods doing too many things.
    ```java
    int getIncome() {
        // 1. Generate payslip
        // 2. Convert PS to JSON
        // 3. Mail PS to the employee
        return this.income;
    }
    ```
3. **Unspecified `Util` or `Helper` classes**: Classes that become dump-grounds for unrelated methods.
    ```java
    class Util {
        void rupeeToDollar(int amount) {}
        int calculateIncomeTax(Employee e) {}
        String toString(Object obj) {}
    }
    ```

---

## ⚡ 2. Open/Closed Principle (OCP)
> **"Software entities should be open for extension but closed for modification."**

In simple terms: You should be able to add new features **without changing existing code**.

### 🖋️ Scaler Example: Pen Design (`class9`)
Adding a new Pen type (e.g., "Fountain Pen") shouldn't require changing the `Pen` class or its `write()` method.
- **Fix**: Use an abstract class or interface and extend it.

```java
// ✅ OCP: Base class is closed for modification
abstract class Pen {
    public abstract void write(String text);
}

// ✅ OCP: Open for extension
class GelPen extends Pen {
    @Override public void write(String text) { /* Gel logic */ }
}

class FountainPen extends Pen {
    @Override public void write(String text) { /* Fountain logic */ }
}
```

### 💼 Tax Calculation Example (HR System)
- **Requirement**: Calculate income tax for Full-Time Employees (FTEs) differently than for Interns.
- **Violation (Bad approach)**: Having an `if (e instanceof Intern)` ... `else if (e instanceof FTE)` inside a calculate method violates OCP and SRP.
- **Fix**: Create an abstract `TaxCalculationUtil` and let concrete subclasses implement specific rules.

```java
// ✅ OCP: Open for extension
abstract class TaxCalculationUtil {
    public abstract double calculate(Employee e);
}

class FTETaxCalculationUtil extends TaxCalculationUtil {
    @Override public double calculate(Employee e) { return (e.income * 0.30) + (e.income * 0.02); }
}

class InternTaxCalculationUtil extends TaxCalculationUtil {
    @Override public double calculate(Employee e) { return e.income * 0.15; }
}
```
*Pros: Adding a new employee type means just adding a new TaxCalculation class without touching existing ones.*

---

## 🔄 3. Liskov Substitution Principle (LSP)
> **"Subclasses should be substitutable for their base classes without breaking the program."**

In simple terms: A child class should be able to replace its parent without causing errors or unexpected behavior.

### 🦢 The Classic Bird Problem
- **Problem**: If `Bird` has a `fly()` method, and `Penguin` inherits from `Bird`, substituting a `Penguin` where a `Bird` is expected will cause a crash (or an `UnsupportedOperationException`).
- **Fix**: Split the hierarchy.

```java
// ✅ LSP: Separate interface for flying
interface Flyable { void fly(); }

class Bird { /* general bird traits */ }

class Eagle extends Bird implements Flyable {
    public void fly() { System.out.println("Eagle flying"); }
}

class Penguin extends Bird {
    // No fly() method here, perfectly safe!
}
```

---

## 🧩 4. Interface Segregation Principle (ISP)
> **"Clients should not be forced to depend on interfaces they don't use."**

In simple terms: Keep interfaces **lean and specific**. Don't create "fat" interfaces with methods that some implementations don't need.

### 🤖 Example: Robot vs. Human
- **Problem**: A `Worker` interface with `work()` and `eat()`. A `Robot` must implement `eat()`, which makes no sense.

```java
// ✅ ISP: Split into specific interfaces
interface Workable { void work(); }
interface Eatable { void eat(); }

class Human implements Workable, Eatable {
    public void work() { /* ... */ }
    public void eat() { /* ... */ }
}

class Robot implements Workable {
    public void work() { /* ... */ }
    // No need to implement eat()
}
```

### 🧑‍💼 The HR System Example: Unpaid Interns
- **Problem**: If we have an `Employee` interface with `getSalary()` and `processPayment()`, what happens when we introduce an `UnpaidIntern`?
- **Violation**: The `UnpaidIntern` is forced to implement `processPayment()`, even though they aren't paid.
- **Fix**: Break down the "fat" `Employee` interface. Keep it thin, moving payment logic to a separate `Payable` interface that only paid employees implement.

---

## 🔌 5. Dependency Inversion Principle (DIP)
> **"High-level modules should not depend on low-level modules. Both should depend on abstractions."**

In simple terms: Depend on **Interfaces**, not **Concrete Classes**. Use **Dependency Injection**.

### 🛠️ Scaler Example: Seller Search Service (`class6`)
The `SellerRankingService` shouldn't depend on a specific `SnapdealSearchService`. It should depend on an `ISellerSearchService` interface.

```java
// ✅ DIP: High-level module depends on Interface
class SellerRankingService {
    private ISellerSearchService searchService; // Dependency on Abstraction

    public SellerRankingService(ISellerSearchService searchService) {
        this.searchService = searchService; // Constructor Injection
    }

    public void rank() {
        searchService.getSellers();
    }
}
```

### 🛒 The E-Commerce Example: PaymentProcessor
- **Problem**: A `PaymentProcessor` class directly instantiating a `SqlProductRepo` (a low-level class). If we switch to MongoDB, `PaymentProcessor` breaks.
- **Fix**: Instead of creating concrete objects inside other classes, inject an abstraction through the constructor (`ProductRepo`).

```java
// ✅ Abstraction
interface ProductRepo {
    Product getProductById(String productId);
}

// ✅ High-level module depends on abstraction
class PaymentProcessor {
    private ProductRepo repo;
    
    public PaymentProcessor(ProductRepo repo) {
        this.repo = repo; // Constructor Injection
    }
    
    void pay(String productId) {
        Product p = repo.getProductById(productId);
        // Process payment
    }
}
```
*Now, to use MongoDB, just pass `new MongoProductRepo()` into the PaymentProcessor constructor!*

---

## 💼 Case Study: HR Management System (Before vs After)

This comparison clearly shows how the **SRP**, **OCP**, and **DIP** principles transform a rigid system into a flexible one.

### 🔴 BEFORE (Violating Principles)
- **SRP Violation**: `Employee` handles its own persistence (`saveToFile`).
- **OCP Violation**: To add MongoDB support, we *must* change the `Employee` class’s `saveToFile` method.
- **Tight Coupling**: The code is "hardcoded" to only use file storage.

```java
class Employee {
    private int id;
    private String name;

    public void saveToFile() {
        System.out.println("Saving employee " + name + " to file system...");
    }
}

// Client Code
Employee e = new Employee();
e.saveToFile(); // No flexibility!
```

### 🟢 AFTER (Applying SOLID)
- **SRP**: `Employee` is a simple data object. `EmployeeRepository` handles storage.
- **OCP**: We can add `EmailService` or `MongoDbStorage` by extending the interfaces.
- **DIP**: `EmployeeRepository` depends on the `IDataBaseService` interface, not a concrete class.

```java
// 1. Data Object (SRP)
class Employee {
    private int id;
    private String name;
    // ...
}

// 2. Abstraction (DIP)
interface IDataBaseService {
    void write(Employee e);
}

// 3. Concrete Implementations (OCP - Open for extension)
class FileStorageService implements IDataBaseService {
    public void write(Employee e) { System.out.println("Writing to File"); }
}

class MongoDbStorageService implements IDataBaseService {
    public void write(Employee e) { System.out.println("Writing to MongoDB"); }
}

// 4. Repository (DIP & SRP)
class EmployeeRepository {
    private IDataBaseService dbService; // Dependency on Interface

    public EmployeeRepository(IDataBaseService dbService) {
        this.dbService = dbService; // Dependency Injection
    }

    public void save(Employee e) {
        dbService.write(e);
    }
}
```

---

## 🎯 Summary for Interviews
| Principle | Key Word | Main Benefit |
| :--- | :--- | :--- |
| **SRP** | **Single Reason** | Easy maintenance, loose coupling. |
| **OCP** | **Extension** | Adding features without breaking old ones. |
| **LSP** | **Substitution** | Reliable inheritance, safe polymorphism. |
| **ISP** | **Specific** | Clean contracts, no "dumb" method implementations. |
| **DIP** | **Abstraction** | Plug-and-play architecture, easy testing. |

---

## 🎙️ Frequently Asked Interview Questions (Viva)

#### Q1: Why do we need the SOLID principles?
**Answer**: SOLID principles are essential for writing low-level code that is easy to understand, extend, and maintain. They help prevent rigid architecture, reduce the chance of code breaking during updates, and allow developer teams to work independently on different features.

#### Q2: What are some quick "smells" or checks that indicate a Single Responsibility Principle (SRP) violation?
**Answer**: 
- **Multiple `if/else` ladders**: Often point to a single method handling multiple behaviors.
- **God/Monster Methods**: A method doing multiple distinct tasks (e.g., parsing, saving, and emailing).
- **Unspecified Util/Helper Classes**: Classes like `StringUtil` that become massive dumping grounds for completely unrelated functions.

#### Q3: How does the Open/Closed Principle (OCP) prevent breaking existing features?
**Answer**: OCP dictates that classes should be closed to modification but open for extension. By using abstract classes or interfaces, we can add new capabilities (like a new tax rule or a new type of Pen) by creating new derived classes, completely avoiding the need to edit (and potentially break) existing, tested code.

#### Q4: Why shouldn't a Penguin class simply implement a fly() method by throwing an Exception or doing nothing?
**Answer**: This violates the **Liskov Substitution Principle (LSP)**. If the rest of the code expects a base type (`Bird`) to be able to successfully `fly()`, swapping that base type with the given derived type (`Penguin`) will alter the correctness of the program and cause crashes.

#### Q5: What is a "Fat Interface," and how does it relate to ISP?
**Answer**: A "fat interface" is a broad interface with many responsibilities. The **Interface Segregation Principle (ISP)** states that clients shouldn't be forced to depend on methods they don't use. For example, forcing an `UnpaidIntern` class to implement a `processPayment()` method from a fat Employee interface. The solution is splitting it into thinner, specific interfaces like `Payable`.

#### Q6: Dependency Inversion Principle says "depend upon abstractions, not concretions." How is this practically achieved in code?
**Answer**: It is achieved by avoiding the instantiation of concrete classes inside other classes (`new SqlProductRepo()`). Instead, the higher-level class expects an interface (`ProductRepo`), and the concrete object is provided via **Dependency Injection** (typically passed through the class's constructor).

---
*Created for viva preparation using notes from Scaler LLD sessions.*
