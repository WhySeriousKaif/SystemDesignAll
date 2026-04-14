# HR Management System: Learning SOLID Principles

This guide explains the **SOLID Principles** step-by-step using an HR Management System example. We will focus on the first two principles: **Single Responsibility Principle (SRP)** and **Open-Closed Principle (OCP)**.

---

## v0: The Monolith (Breaking SRP)

In the initial version, our `Employee` class tries to do everything: hold data AND handle how it's saved.

```java
class Employee {
    String name;
    String id;
    // ... other fields

    void save() {
        // 1. Serialize data to string
        // 2. Open file
        // 3. Write to file
        // 4. Close file
    }
}

public class Main {
    public static void main(String[] args) {
        Employee emp = new Employee();
        emp.name = "John Doe";
        emp.id = "E001";
        emp.save(); // Violates SRP!
    }
}
```

### Why is this bad?
This violates the **Single Responsibility Principle (SRP)**. 
> "A class should have one, and only one, reason to change."

If we change the file format (e.g., JSON to CSV) or the database type, we have to modify the `Employee` class. This makes the code fragile.

---

## v1: Single Responsibility Principle (SRP)

To fix this, we delegate responsibilities to specialized classes.

1.  **Employee**: Holds only employee data.
2.  **Serializer**: Responsible only for formatting the data.
3.  **FileHandler**: Responsible only for interacting with the storage.

```java
class Serializer {
    String serialize(Employee e) { /* Convert to String */ return ""; }
}

class FileHandler {
    void writeFile(String data) { /* Real I/O logic */ }
}

class EmployeeRepository {
    void save(Employee e) {
        Serializer s = new Serializer();
        FileHandler f = new FileHandler();
        String data = s.serialize(e);
        f.writeFile(data);
    }
}

public class Main {
    public static void main(String[] args) {
        Employee emp = new Employee();
        EmployeeRepository repo = new EmployeeRepository();
        repo.save(emp); // Achievement: Responsibilities are separate!
    }
}
```

**Now:**
- To change format $\rightarrow$ Change `Serializer`.
- To change database $\rightarrow$ Change `FileHandler`.
- `Employee` remains untouched!

---

## v2: The If-Else Trap (Breaking OCP)

Next, we added tax calculation. Different employees have different tax rates:
- **Full Time (FT)**: 30% + 2% Professional Tax.
- **Intern**: 15%.

A naive approach uses `if-else`:

```java
class IncomeTaxCalculator {
    double calculate(Employee e) {
        if (e.type.equals("FT")) {
            return e.getIncome() * 0.3 + 2;
        } else if (e.type.equals("Intern")) {
            return e.getIncome() * 0.15;
        }
        return 0;
    }
}

public class Main {
    public static void main(String[] args) {
        IncomeTaxCalculator calc = new IncomeTaxCalculator();
        Employee intern = new Employee("Intern", 1000);
        System.out.println("Tax: " + calc.calculate(intern)); // Hard to extend!
    }
}
```

### Why is this bad?
This violates the **Open-Closed Principle (OCP)**.
> "Software entities should be open for extension, but closed for modification."

Every time we add a new employee type (e.g., "Contractor"), we must **modify** this `if-else` block.

---

## v3: Open-Closed Principle (OCP)

We solve this using **Interfaces** and **Composition**.

### 1. Define an Interface
```java
interface TaxCalculator {
    double calculateTax(Employee e);
}
```

### 2. Create Concrete Implementations
```java
class FTHTaxCalculator implements TaxCalculator {
    public double calculateTax(Employee e) { return e.getIncome() * 0.3 + 2; }
}

class InternTaxCalculator implements TaxCalculator {
    public double calculateTax(Employee e) { return e.getIncome() * 0.15; }
}
```

### 3. Use Composition in Employee
Instead of checking types, the `Employee` just tells its `taxCalculator` to do its job.

```java
// Strategy Interface
interface TaxCalculator {
    double calculateTax(Employee employee);
}

// Full-Time Tax Logic
class FTHTaxCalculator implements TaxCalculator {
    @Override
    public double calculateTax(Employee employee) {
        return employee.getSalary() * 0.20;
    }
}

// Part-Time Tax Logic
class PTHTaxCalculator implements TaxCalculator {
    @Override
    public double calculateTax(Employee employee) {
        return employee.getSalary() * 0.10;
    }
}

// Base Employee Class
class Employee {

    protected int id;
    protected String name;
    protected String email;
    protected String phone;
    protected double salary;

    protected TaxCalculator taxCalculator;

    public Employee(int id, String name, String email, String phone, double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public double calculateIncomeTax() {
        return taxCalculator.calculateTax(this);
        //this refers to the current Employee object
        //if FullTimeEmployee extends Employee then this is FullTimeEmployee object
        //if PartTimeEmployee extends Employee then this is PartTimeEmployee object
    }

    public void displayDetails() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Salary: " + salary);
    }
}

// Full-Time Employee
class FullTimeEmployee extends Employee {

    public FullTimeEmployee(int id, String name, String email, String phone, double salary) {
        super(id, name, email, phone, salary);
        this.taxCalculator = new FTHTaxCalculator();
    }
}

// Part-Time Employee
class PartTimeEmployee extends Employee {

    public PartTimeEmployee(int id, String name, String email, String phone, double salary) {
        super(id, name, email, phone, salary);
        this.taxCalculator = new PTHTaxCalculator();
    }
}

// Main
public class Main {
    public static void main(String[] args) {

        Employee emp1 = new FullTimeEmployee(
                101,
                "Kaif Molla",
                "kaif@email.com",
                "9876543210",
                50000
        );

        emp1.displayDetails();
        System.out.println("Tax: " + emp1.calculateIncomeTax());
    }
}
```
### Architecture Diagram
```text
                +----------------------+
                |    TaxCalculator     |
                |----------------------|
                | + calculateTax()     |
                +----------▲-----------+
                           |
            -------------------------------
            |                             |
+-----------------------+     +-----------------------+
|   FTHTaxCalculator    |     |   PTHTaxCalculator    |
|-----------------------|     |-----------------------|
| calculateTax() 20%    |     | calculateTax() 10%    |
+-----------------------+     +-----------------------+


              (Strategy used here)
                       ▲
                       |
                +----------------------+
                |       Employee       |
                |----------------------|
                | id                   |
                | name                 |
                | email                |
                | phone                |
                | salary               |
                | TaxCalculator        |
                |----------------------|
                | calculateIncomeTax() |
                | displayDetails()     |
                +----------▲-----------+
                           |
              -------------------------
              |                       |
+---------------------------+  +---------------------------+
|     FullTimeEmployee      |  |     PartTimeEmployee      |
|---------------------------|  |---------------------------|
| taxCalculator = FTH       |  | taxCalculator = PTH       |
+---------------------------+  +---------------------------+
```

---

## v4: Dependency Inversion Principle (DIP)

The Dependency Inversion Principle states that **high-level modules should not depend on low-level modules; both should depend on abstractions.**

Here is the step-by-step evolution of how we decoupled our data layer.

### Stage 1: Tightly Coupled (The Problem)
Initially, our `EmployeeRepo` was directly creating a `FileService` inside its methods.

```java
class EmployeeRepo {
    void save(Employee e) {
        // ERROR: Tightly Coupled!
        FileService fs = new FileService();
        fs.write(e.toString());
    }
}
```
**Loopholes:**
- **Cannot Test**: You can't replace `FileService` with a "mock" or "fake" for testing.
- **Fragile**: If `FileService` changes its constructor (e.g., requires a path), you have to change `EmployeeRepo` too.
- **Hard to Switch**: If you want to use SQL instead of a File, you must rewrite the `EmployeeRepo`.

---

### Stage 2: Using Interfaces (The Attempt)
We introduced an interface `IDatabaseService`, but we still instantiated it inside the class.

```java
interface IDatabaseService {
    void write(String data);
}

class EmployeeRepo {
    void save(Employee e) {
        // STILL COUPLED to FileStorageService!
        IDatabaseService dbs = new FileStorageService(); 
        dbs.write(e.toString());
    }
}
```
**Loopholes:**
- Even though we use an interface, the **dependency on the concrete class** (`new FileStorageService()`) still exists. The high-level module (`EmployeeRepo`) is still "creating" its own dependency.

---

### Stage 3: Dependency Injection (The Solution)
Instead of creating the dependency, we **ask for it** via the constructor. This is called **Constructor Injection**.

```java
class EmployeeRepo {
    private IDatabaseService dbs;

    // Dependency is "Injected" from outside
    public EmployeeRepo(IDatabaseService dbs) {
        this.dbs = dbs;
    }

    void save(Employee e) {
        this.dbs.write(e.toString());
    }
}
```

### Complete Code (DIP)

```java
interface IDatabaseService {
    void write(String data);
}

class FileStorageService implements IDatabaseService {
    public void write(String data) { System.out.println("Saving to File: " + data); }
}

class SqlStorageService implements IDatabaseService {
    public void write(String data) { System.out.println("Saving to SQL Database: " + data); }
}

class EmployeeRepo {
    private IDatabaseService dbs;

    public EmployeeRepo(IDatabaseService dbs) {
        this.dbs = dbs;
    }

    void save(Employee e) {
        // Logic to format employee...
        String data = e.name + " (" + e.id + ")";
        this.dbs.write(data);
    }
}

public class Main {
    public static void main(String[] args) {
        // We decide the storage at runtime!
        IDatabaseService storage = new SqlStorageService(); 
        
        EmployeeRepo repo = new EmployeeRepo(storage);
        Employee emp = new Employee(101, "Kaif Molla", "kaif@email.com", "987...", 50000);
        
        repo.save(emp);
    }
}
```

**Benefits of DIP:**
1.  **Flexibility**: You can swap `FileStorageService` for `SqlStorageService` without touching a single line of code in `EmployeeRepo`.
2.  **Testability**: In unit tests, you can inject a `MockDatabaseService` that doesn't actually write to disk.
3.  **Decoupling**: Classes only care about "what" (the interface), not "how" (the implementation).

---

### Deep Dive: DIP vs. DI vs. IoC

1.  **Dependency Inversion Principle (DIP)**: The **design principle** which says "Depend on abstractions, not concretions."
2.  **Dependency Injection (DI)**: The **action or way** of executing this principle (e.g., passing the dependency through the constructor).
3.  **Inversion of Control (IoC)**: A **broader concept** where you give up control of object creation and lifecycle to a framework.

> [!NOTE]
> **Spring Boot & IoC**: In modern Java frameworks like Spring, you don't even call `new EmployeeRepo(storage)`. The framework (the "IoC Container") manages the entire lifecycle of your objects. It decides which object depends on which, creates them, and even deletes them when they are no longer needed.

---

### Why Database Methods Should NOT be Static

A common beginner question is: *"Why not just make `write()` and `searchById()` static? Then I don't need to create an object!"*

**The Answer: Static Breaks Polymorphism.**

- **Static Methods**: Belong to the class itself. They are "resolved" at compile-time, not runtime.
- **Instance Methods**: Support **Runtime Polymorphism**. They are resolved at runtime based on the actual object type.

| Feature | Static Method | Instance Method |
| :--- | :--- | :--- |
| **Context** | Belongs to Class | Belongs to Object (`this`) |
| **Override?** | ❌ Cannot be overridden | ✅ Can be overridden |
| **Polymorphism?**| ❌ No Runtime Polymorphism | ✅ Supports Runtime Polymorphism |
| **Usage** | Global Utility (e.g., `Math.max()`) | Behavior that varies by implementation |

Since different database services (MySQL, MongoDB, File) must implement `write()` differently, we **must** use instance methods. If you make them static, you can't use an interface properly, and you lose the ability to swap implementations at runtime.

#### The "Math Class" Exception
You might ask: *"But `Math.max()` and `Math.min()` are static! Why?"*
- **Fixed Logic**: The logic for finding the maximum of two numbers will **never change**. It is universal.
- **No Variations**: You don't have "MySQL Math" or "MongoDB Math". 
- **SOLID Compliance**: Since the behavior is fixed, making it static doesn't violate OCP or DIP. However, for database operations, behavior **does** change, so using `static` would violate major SOLID principles.

---

### Understanding `static` (Memory & Scope)

- **Static Variable**: Only **one copy** exists in memory (Heap), shared across all instances of the class.
- **Static Method**: Can be called directly (e.g., `ClassName.method()`) without creating an object.
- **The Limit**: Inside a `static` method, you **cannot** call non-static variables or methods because there is no `this` reference.

#### Example: Static vs. Non-Static Scope
```java
class Demo {
    int var = 10; // Instance variable (belongs to object)

    static boolean compare(int a) {
        // ERROR: Non-static field 'var' cannot be referenced from a static context
        // return a > this.var; 
        
        return a > 0; // Only static variables or parameters can be accessed here
    }

    public static void main(String[] args) {
        // We can call static methods without creating an object
        System.out.println(Demo.compare(100));
    }
}
```

---

## Summary for Beginners

| Principle | Simple Rule | How we achieved it |
| :--- | :--- | :--- |
| **SRP** | Do one thing well. | Moved file saving and formatting out of the `Employee` class. |
| **OCP** | Don't touch working code to add features. | Used interfaces so we can add "ContractorTax" by creating a new class, not by editing old ones. |

### Key Takeaway
Good system design is like **LEGO bricks**. Each brick has a specific shape (SRP) and you can add new bricks to build bigger things without melting down the bricks you already have (OCP).
