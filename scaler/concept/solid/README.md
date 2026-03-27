# SOLID Principles - Revision Guide (viva-Ready)

This guide summarizes the **5 key design principles** discussed in the Scaler LLD classes. These principles are essential for building maintainable, flexible, and reliable systems like the **HR Management System**, **Pen Design System**, and **Snake & Ladder**.

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
*Created for viva preparation using notes from Scaler LLD sessions.*
