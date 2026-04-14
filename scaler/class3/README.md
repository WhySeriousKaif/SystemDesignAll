# Class 3: Design Patterns & SOLID Principles

This session covers the practical application of SOLID principles, foundational Java concepts, and explores the evolution of the **Singleton Design Pattern** to achieve the best balance between performance and thread safety.

---

## ☕ Java Fundamentals: Static vs. Instance

Understanding the difference between static and instance members is crucial for memory management and object-oriented design.

- **Instance Methods**: To call a method inside a class, you must first create an **object** (instance) of that class. You then invoke the method using that object reference.
- **Static Methods**: By marking a method as `static`, you can invoke it directly using the **class reference** without creating an object.
- **The Golden Rule**: A static method or variable **cannot** directly access or return non-static (instance) variables/methods. If a static method needs to return or use such data, those members must also be made `static`.

---

## 🏗️ SOLID Principles Recap (HR Management System)

In `Main.java`, we refactored the HR Management System to adhere to SOLID principles:

1.  **SRP (Single Responsibility Principle)**: We moved `save()` logic out of the `Employee` class into `EmployeeRepository` and handled database/file logic separately.
2.  **OCP (Open-Closed Principle)**: By using the `IDataBaseSaervice` interface, we can add new storage types (MongoDB, SQL) without modifying existing repository code.
3.  **DIP (Dependency Inversion Principle)**: `EmployeeRepository` no longer depends on concrete classes like `FileStorageService`; it depends on the `IDataBaseSaervice` abstraction.
4.  **ISP (Interface Segregation Principle)**: Interfaces should be specific. Instead of one giant interface, we split them based on specific behaviors (e.g., `Flyable` and `Flappable`).

---

## 🛠️ Singleton Pattern Evolution

The goal of a Singleton is to ensure that a class has only **one instance** and provides a global point of access to it.

**Intent:** Ensure exactly one instance of a class exists and provide a global access point.
**Use cases:** Logging service, configuration manager, feature flag provider, connection pool manager, metrics reporter.

### Step 0: Eager Initialization (The Initial Approach)
The instance is created at the time of class loading.

```java
public class FileStorageService {
    private static FileStorageService instance = new FileStorageService();

    private FileStorageService() {} // Private constructor

    public static FileStorageService getInstance() {
        return instance;
    }
}
```
- **The Issue**: It uses memory even if the application never uses the object (**Eager Loading**). This can slow down system startup if the object is heavy.

---

### Step 1: Lazy Initialization (Adding "If-Needed")
We only create the instance when `getInstance()` is called for the first time.

```java
public class FileStorageService {
    private static FileStorageService instance = null;

    private FileStorageService() {}

    public static FileStorageService getInstance() {
        if (instance == null) {
            instance = new FileStorageService();
        }
        return instance;
    }
}
```
- **The Issue**: **Not Thread-Safe**. In a multi-threaded environment, two threads might both check `instance == null` at the same time and create two different objects (Race Condition).

---

### Step 2: Synchronized Method (Solving Thread Safety)
We make the method `synchronized` so only one thread can enter at a time.

```java
public class FileStorageService {
    private static FileStorageService instance = null;

    private FileStorageService() {}
// the method is synchronized so it will only allow one thread to enter at a time(sequential method not parallel by multiple threads)
    public static synchronized FileStorageService getInstance() {
        if (instance == null) {
            instance = new FileStorageService();
        }
        return instance;
    }
}
```
- **The Issue**: **Performance Bottleneck**. Making the whole method `synchronized` is **cost-heavy**. Every time a thread wants the instance (even after it's already created), it has to wait for the lock. This causes unnecessary **Thread Blocking** and slows down the system.

---

### Step 3: Double-Checked Locking (DCL) - The Optimization
To avoid blocking the whole method, we only synchronize the block that creates the object. We perform a second null check *inside* the synchronized block to prevent multiple instantiation.

```java
public class FileStorageService {
    private static FileStorageService instance = null;

    private FileStorageService() {}

    public static FileStorageService getInstance() {
        if (instance == null) { // First Check (No locking)
            synchronized (FileStorageService.class) {
                if (instance == null) { // Second Check (With locking)
                    instance = new FileStorageService();
                }
            }
        }
        return instance;
    }
}
```

---

### ⚠️ The "Silent Killer": JVM Instruction Reordering

While DCL seems perfect, it can still fail due to how the JVM optimizes code. Object creation (`new FileStorageService()`) is **not one atomic step**. Internally, it involves three steps:

1.  **Reserve Memory**: Space is allocated for the object in the heap.
2.  **Initialization**: The object's attributes are initialized and the constructor is run.
3.  **Linking the Reference**: The `instance` variable is made to point to the allocated memory address.

#### The Problem: Optimization Reordering
For performance, the JVM might reorder these steps to **1 -> 3 -> 2**.
- If **Thread 1** performs Step 1 and Step 3 (linking the reference) but hasn't finished Step 2 (initialization)...
- **Thread 2** comes in, checks `if (instance == null)`, and sees that it is **not null** (because it's already linked).
- **Thread 2** returns the object and tries to use it.
- **Result**: The application crashes because the object is "half-baked" or uninitialized.

---

### 🛡️ The Final Solution: `volatile`

By marking the instance as `volatile`, we tell the JVM:
1.  **Visibility**: Any change to this variable is immediately visible to all threads.
2.  **No Reordering**: The JVM is strictly prohibited from reordering the instructions around this variable.

```java
public class FileStorageService {
    // Volatile ensures visibility and prevents instruction reordering
    private static volatile FileStorageService instance = null;

    private FileStorageService() {}

    public static FileStorageService getInstance() {
        if (instance == null) {
            synchronized (FileStorageService.class) {
                if (instance == null) {
                    instance = new FileStorageService();
                }
            }
        }
        return instance;
    }
}
```
> [!IMPORTANT]
> This is the industry-standard way to implement a thread-safe, high-performance Singleton in Java using DCL.

#### The "Visibility Guarantee"
With `volatile`, other threads effectively "wait" (not by being paused by a lock, but by their `if (instance == null)` check returning `true`) until **all three steps** are finished. Because the JVM cannot put Step 3 before Step 2, a thread will only see a non-null `instance` when it is **100% initialized**.

---

### 🕵️ The Security Breach: Java Reflection API

Even after implementing DCL and `volatile`, a 100% Singleton is not guaranteed. A developer can use the **Java Reflection API** to introspect your class and bypass your `private` constructor.

#### The Attack: `setAccessible(true)`
Reflection allows you to change the accessibility of any constructor at runtime.

```java
import java.lang.reflect.Constructor;

public class Attack {
    public static void main(String[] args) throws Exception {
        Singleton instance1 = Singleton.getInstance();

        // 1. Get the private constructor
        Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
        // 2. Force it to be accessible
        constructor.setAccessible(true);
        // 3. Create a second instance!
        Singleton instance2 = constructor.newInstance();

        // Now instance1 and instance2 are DIFFERENT objects!
    }
}
```

#### The Defense: The Constructor Guard
To prevent a Reflection attack, you should add a check inside your private constructor to throw an exception if an instance has already been created.

```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
        // Guard against Reflection attack
        if (instance != null) {
            throw new RuntimeException("Singleton instance already exists. Use getInstance().");
        }
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

> [!IMPORTANT]
> This is a crucial detail often discussed in **Senior/LLD Interviews**. It demonstrates that you understand the deep internals of the JVM and the potential loopholes in standard design patterns.

---

### 📦 The Serialization Breach

When a Singleton class implements `Serializable`, it can be serialized and later deserialized. However, **deserialization creates a new instance** without calling the constructor, violating the Singleton property.

#### The Attack: Deserialization
```java
// ... instance1 is already created ...
// Serialize
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("singleton.ser"));
out.writeObject(instance1);

// Deserialize
ObjectInputStream in = new ObjectInputStream(new FileInputStream("singleton.ser"));
Singleton instance2 = (Singleton) in.readObject();

// instance1 != instance2 !!
```

#### The Defense: `readResolve()`
To protect against this, implement the `readResolve()` method. This method is called during deserialization and allows you to replace the deserialized object with your existing instance.

```java
import java.io.Serializable;

public class Singleton implements Serializable {
    // ... DCL and volatile implementation ...

    protected Object readResolve() {
        // Return the existing instance instead of a new one
        return getInstance();
    }
}
```

---

---

### 🎙️ The Interviewer's Favorite: Singleton Stories

When explaining Singleton in an interview, use these structured "stories" to demonstrate deep understanding of concurrency.

#### Story 1: The T1 & T2 Race Condition (Why V2.5 Fails)
"Imagine two threads, **T1 and T2**, entering the `getInstance()` method simultaneously. 
1. **T1** checks `instance == null`, it's true, so it enters the block. 
2. Before **T1** can synchronized and create the object, **T2** also checks `instance == null`. It's still true because T1 hasn't finished.
3. Now both are inside the 'if' block. **T1** gets the lock, creates the object, and leaves. 
4. **T2** then gets the lock and **creates a second object** because there was no second null check. This is why Double-Checked Locking is mandatory."

#### Story 2: The Half-Baked Object (Why `volatile` is Mandatory)
"Even with DCL, a thread can crash. Object creation isn't one step; it's three: 
1. **Allocate memory**. 
2. **Link the reference** (point the variable to that memory). 
3. **Initialize the object** (run the constructor). 
The JVM can reorder these to **1 -> 2 -> 3**. If **T1** links the reference (Step 2) but hasn't initialized yet (Step 3), **T2** will see a non-null instance, try to use it, and crash because it's **half-baked**. `volatile` prevents this reordering."

---

## 🚀 Advanced Singleton Patterns

While DCL is the classic answer, modern Java offers cleaner alternatives.

### 1. Enum Singleton (The Most Robust)

Recommended by Joshua Bloch (Effective Java), this is widely considered the **most secure and simplest** way to implement a Singleton in Java.

#### 💻 Refactored Implementation
```java
public enum ConnectionPool {
    INSTANCE; // This is the single instance

    private String databaseUrl;

    // Enum constructors are private by default
    ConnectionPool() {
        this.databaseUrl = "jdbc:mysql://localhost:3306/mydb";
    }

    public void getConnection() {
        System.out.println("Connecting to: " + databaseUrl);
    }
}

// Usage:
// ConnectionPool.INSTANCE.getConnection();
```

---

#### 🛡️ Why it works best
1.  **Absolute Thread Safety**: The JVM guarantees that enum constants are instantiated only once in a thread-safe manner during class loading. You don't need `synchronized` or `volatile`.
2.  **Native Serialization Support**: Unlike normal classes, enums handle serialization automatically. The JVM ensures that during deserialization, the same `INSTANCE` is returned, preventing the creation of "duplicate" singletons. No need for `readResolve()`.
3.  **Reflection Proof**: This is the only method that is **immune to Reflection attacks**. If you try to instantiate an enum via Reflection, the JVM throws an `IllegalArgumentException`, specifically stating that enums cannot be reflectively created.

---

#### ❌ Why other approaches can "break"
*   **Lazy Initialization (V1)**: Fails in multi-threaded environments (Race Condition).
*   **Synchronized Method (V2)**: Extremely slow due to locking overhead.
*   **Double-Checked Locking (V3)**: Requires `volatile` to prevent "half-baked" objects due to instruction reordering. Even then, it is **vulnerable to Reflection and Serialization** unless you manually add guards and `readResolve()`.

---

#### ⚠️ Setbacks of Enum Singleton
While robust, the Enum Singleton has a few limitations:
1.  **No Lazy Loading**: Like Eager initialization, the enum instance is created as soon as the class is loaded. If the object is very "heavy" and rarely used, this might waste memory.
2.  **No Inheritance**: Enums in Java cannot extend another class (because they implicitly extend `java.lang.Enum`). If your Singleton needs to be part of a class hierarchy, you cannot use this approach.
3.  **Limited Flexibility**: It's harder to refactor an Enum Singleton into a "Multiton" (e.g., exactly 3 instances) or change it to a non-singleton later compared to the Builder or Factory patterns.

### 2. Bill Pugh Singleton (Static Inner Class)
Uses the "Lazy Initialization Holder" idiom. It's lazy and thread-safe without explicit synchronization!

```java
public class FileStorageService {
    private FileStorageService() {}

    private static class SingletonHelper {
        private static final FileStorageService INSTANCE = new FileStorageService();
    }

    public static FileStorageService getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
```
- **How it works**: The `SingletonHelper` class is not loaded into memory until `getInstance()` is called. The JVM handles thread safety during class loading.

---

### 3. Testing Singletons (The Hidden Cost)
- **Problem**: Singletons are difficult to mock in unit tests because they provide a global state.
- **Solution**: If testability is a high priority, consider using Dependency Injection (like Spring) to manage singletons rather than hardcoding the Singleton pattern yourself.

---

### 🎙️ Singleton Interview Questions

#### Q1: How do you make a class Singleton?
- By making the constructor `private` to restrict external instantiation.
- Holding a `static` reference to the single instance within the class.
- Providing a `public static` method (like `getInstance()`) to allow global access to that instance.

#### Q2: What problem does eager initialization cause?
- Eager initialization creates the instance at the time of class loading, even if the application never uses it. This can consume unnecessary memory and slow down system startup if the object is resource-heavy.

#### Q3: How does lazy initialization solve this, and what problem does it introduce?
- **Solution**: It creates the instance only when `getInstance()` is called for the first time, saving memory.
- **Problem**: It introduces a **Race Condition** in a multi-threaded environment. Two threads could simultaneously check `instance == null` and end up creating two separate objects.

#### Q4: What is Double-Checked Locking (DCL)? How do you achieve it?
- DCL is an optimization to reduce the overhead of a fully synchronized block. It checks if the instance is null (first check, no lock). If true, it enters a `synchronized` block and checks if the instance is null again (second check, with lock) before creating the object.
- **How to achieve it**:
  ```java
  if (instance == null) {
      synchronized (Logger.class) {
          if (instance == null) {
              instance = new Logger();
          }
      }
  }
  ```

#### Q5: What two problems arise without `volatile` in Double-Checked Locking?
1. **Instruction Reordering**: Object creation is not atomic (allocate memory, initialize, assign reference). The JVM might assign the reference before initialization is complete. Another thread could then see a non-null, but fully "half-baked" object.
2. **Caching and Visibility**: Without `volatile`, one thread might update its CPU cache with the new object reference, but other threads might still see `null` in their own caches.

#### Q6: Which Singleton approach is the safest overall, and why?
- **Enum Singleton** is the safest.
- **Why**: It relies on the JVM to automatically handle thread safety, serialization, and absolute protection against Java Reflection API attacks (which can break private constructors in other approaches).

---

## 🔒 Immutability in Java

An **Immutable Class** is one whose state cannot be changed after it is constructed. Applications like `String` and `Integer` are immutable by design.

**Intent:** Objects whose state cannot change after construction.
**Use cases:** Payment receipts, order snapshots, currency/money values, configuration objects, DTOs shared across threads.

---

### ❓ Why do we need Immutability?
1. **Thread Safety**: Immutable objects are inherently thread-safe. Multiple threads can access them simultaneously without any risk of data corruption or race conditions, as their state never changes. No `synchronized` blocks or locks are needed.
2. **Security**: Sensitive information (like usernames, passwords, or network configurations) is often stored in immutable objects. If `String` were mutable, an attacker could potentially change the connection string *after* it's been validated but *before* it's used.
3. **Caching & Reusability**: Because they never change, they are perfect for caching. This is why Java has a **String Pool** and caches small `Integer` values (-128 to 127).
4. **Consistency in Hash-based Collections**: If an object is used as a key in a `HashMap` or `HashSet`, its `hashCode` must remain constant. If the object were mutable and its state changed, its hash code would change, making it impossible to retrieve the object from the collection.
5. **Predictability**: It's much easier to reason about code when you know that an object passed into a method won't be silently modified by that method.

---

### 📜 The 5 Rules of Immutability
1. **No Setters**: Don't provide methods that modify the object's state.
2. **Make the Class `final`**: Prevents subclasses from overriding methods and compromising immutability
3. **Make all fields `private` and `final`**: Ensures fields are not accessible and not changed outside like in main method and are initialized only once and because of final inside the class we can't change the reference of the field aslo value asigned cannot be changed !
4. **Initialize through Constructor**: Pass all values during object creation.
5. **Reference Protection (Deep Copy)**: Never return or accept direct references to mutable objects (like `List`, `Map`, etc.).

### 📂 Reference Protection & The Collection Trap

Even if a field is `private final`, if it points to an `ArrayList`, someone can still call `.add()` on it. This is because **`final` only protects the reference, not the content**.

#### 1. The Constructor Leak (Input Protection)
If you simply assign `this.list = list;`, both your object and the caller (e.g., `main()`) point to the **same list** in memory. If `main()` modifies the list later, your "immutable" object changes.
- **Solution**: Create a `new ArrayList<>(list)` in the constructor so your object has its own private copy.

#### 2. The Getter Leak (Output Protection)
If your getter returns `return this.list;`, you are handing over the keys to your internal data. The caller can now call `.add()` or `.clear()` on your private list.
- **Solution**: Return a `new ArrayList<>(this.list)`. The user will modify their own copy, not your internal reference.

#### 💻 Simple Scalar Example (`PaymentReceipt`)
If all fields are primitive or themselves immutable, simple assignment via constructor is enough.
```java
import java.time.Instant;

public final class PaymentReceipt {
    private final String id;
    private final double amount;
    private final Instant timestamp;

    public PaymentReceipt(String id, double amount, Instant timestamp) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }
    public Instant getTimestamp() { return timestamp; }
}
```

#### 🛡️ Defensive Copying Example (`Order`)
If you have a mutable collection, you must make defensive copies to prevent external mutation of internal state.

```java
import java.util.*;

public final class Order {
    private final List<String> items;
    
    public Order(List<String> items) {
        // Creates an unmodifiable copy at the time of construction
        this.items = List.copyOf(items); 
    }

    public List<String> getItems() {
        // Returns a safe view so caller cannot call .add()
        return Collections.unmodifiableList(items); 
    }
}
```
*Note: If we just did `this.items = items;`, a caller could modify their original list and silently alter the `Order` object!*

#### 🎮 Complete Real-World Example (`GameConfig`)
Combining standard types and collections securely:

```java
import java.util.*;

public final class GameConfig {
    private final String name;
    private final List<String> rules;

    public GameConfig(String name, List<String> rules) {
        this.name = Objects.requireNonNull(name);
        this.rules = List.copyOf(rules); // defensive copy
    }

    public String getName() { return name; }
    public List<String> getRules() { return Collections.unmodifiableList(rules); }

    @Override
    public String toString() { return name + " with rules " + rules; }
}
```

---

### 👥 How to "Clone" an Immutable Object?

In standard Java, cloning is done using the `clone()` method. However, for an **Immutable Class**, traditional cloning is redundant.

1. **Returning `this`**: Since the object's state can never change, any number of references can safely point to the same instance. Therefore, a `clone()` implementation for an immutable class should simply **return `this`**.
    ```java
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // No need to create a new object. 'this' is perfectly safe to share.
        return this;
    }
    ```
2. **The "Wither" Pattern (Functional Cloning)**: If you need a copy of the object but with one field modified (e.g., changing a user's email), you use what's called a **"Wither" method**. It returns a **new instance** with the updated value while copying everything else.
    ```java
    public final class User {
        private final String name;
        private final String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        // Instead of setName(), we have withEmail()
        public User withEmail(String newEmail) {
            return new User(this.name, newEmail);
        }
    }
    ```

---

### 🎙️ Interview Follow-up / Practice Questions

#### Q1: Why should immutable classes make defensive copies of mutable fields?
- If an immutable class holds a reference to a mutable object (like a `List`), external code could modify that object and silently alter the "immutable" state. Defensive copying ensures the object owns completely isolated data. 

#### Q2: What is the difference between returning a defensive copy and an unmodifiable view?
- **Defensive Copy (`new ArrayList<>(list)`)**: Creates a completely new list containing the same elements. It takes more memory but is completely detached.
- **Unmodifiable View (`Collections.unmodifiableList(list)`)**: Doesn't create a new list; it creates a "read-only" wrapper around the existing list. It's more memory efficient but relies on the underlying list not changing.

#### Q3: How does immutability improve thread-safety?
- Because the state of an immutable object never changes after construction, multiple threads can read it simultaneously without any risk of Race Conditions or data corruption. **No synchronization or locks are required**.

#### Q4: Why is marking the class `final` recommended when creating an immutable class?
- It prevents **Inheritance Risks**. A subclass could override methods to return different values, add setters, or expose mutable state, completely breaking the immutability guarantee.

#### Q5: Give two real-world use cases where immutability is essential.
1. **Security/Authentication**: Passing around a `UserCredentials` object. You don't want another part of the system unexpectedly changing the username after it's been validated.
2. **High-Concurrency Systems**: Sharing Configuration objects (like `GameConfig`) or DTOs across hundreds of threads without locking bottlenecks.

#### Q6: Why is `String` immutable in Java?
- **String Pool**: To save memory by sharing common strings.
- **Security**: To prevent modification of credentials/paths after validation.
- **Hashing**: `String` caches its hash code because its value never changes, making it extremely fast and reliable as a key in `HashMap`.

#### Q7: If a class has a `final ArrayList`, is it immutable?
- **No.** `final` only makes the **reference** immutable (you can't point it to a different list). You can still add/remove elements. You must use defensive copying to ensure true immutability.

---

## 🏗️ Builder Design Pattern

**Intent:** Simplify the creation of complex immutable objects by separating construction from representation.
**Use cases:** Classes with many parameters (some optional), configuration objects, constructing immutable aggregates.

---

## 🛑 The Problem: Telescoping Constructors
What happens if a class has many fields (e.g., 8–10)? We either need one huge constructor, or many overloaded constructors (known as "Telescoping Constructors").

```java
public class Order {
    private final String customerId;
    private final int priority;
    private final boolean giftWrap;
    private final boolean expressDelivery;

    // Telescoping constructor
    public Order(String customerId, int priority, boolean giftWrap, boolean expressDelivery) {
        this.customerId = customerId;
        this.priority = priority;
        this.giftWrap = giftWrap;
        this.expressDelivery = expressDelivery;
    }
}
```
**Problem:** Hard to read, hard to maintain, confusing for callers (imagine seeing `new Order("C101", 1, true, false)` and trying to remember what those booleans mean without looking at the constructor).

---

## 💡 The Idea of Builder
How can we make object creation clearer? We use a separate `Builder` that sets fields step by step conceptually, and then calls `build()`.

```java
public class Order {
    private final String customerId;
    private final int priority;
    private final boolean giftWrap;
    private final boolean expressDelivery;

    // Private constructor taking the Builder
    private Order(Builder b) {
        this.customerId = b.customerId;
        this.priority = b.priority;
        this.giftWrap = b.giftWrap;
        this.expressDelivery = b.expressDelivery;
    }

    // Static Inner Class Builder
    public static class Builder {
        private String customerId;
        private int priority = 1; // default
        private boolean giftWrap = false;
        private boolean expressDelivery = false;

        // Fluent Setters
        public Builder customerId(String id) { this.customerId = id; return this; }
        public Builder priority(int p) { this.priority = p; return this; }
        public Builder giftWrap(boolean g) { this.giftWrap = g; return this; }
        public Builder expressDelivery(boolean e) { this.expressDelivery = e; return this; }

        public Order build() {
            if (customerId == null) throw new IllegalStateException("customerId required");
            return new Order(this);
        }
    }
}
```

### 💻 Usage:
```java
Order order = new Order.Builder()
    .customerId("C101")
    .priority(2)
    .giftWrap(true)
    .build();
```
*Notice how readable the client code is now!*

---

## 🔒 Enforcing Required Fields
How do we ensure mandatory fields are set so that we do not build an invalid state?
- **Option 1**: Validate in the `build()` method (as seen above).
- **Option 2**: Require them in the Builder's constructor!

```java
public static class Builder {
    private final String customerId; // Required!
    private int priority = 1;        // Optional
    private boolean giftWrap = false;// Optional

    public Builder(String customerId) {
        this.customerId = Objects.requireNonNull(customerId);
    }
    // ... setters and build() ...
}
```

---

## 🎮 Complex Example: GameConfig
What if we have a collection of rules and want to add them step by step? The Builder can accumulate them and build a highly secure immutable object.

```java
import java.util.*;

public final class GameConfig {
    private final String name;
    private final List<String> rules;

    private GameConfig(Builder b) {
        this.name = b.name;
        // Defensive copy to ensure immutability
        this.rules = List.copyOf(b.rules); 
    }

    public static class Builder {
        private String name;
        private List<String> rules = new ArrayList<>();

        public Builder name(String n) { this.name = n; return this; }
        public Builder addRule(String r) { this.rules.add(r); return this; }
        public Builder addAllRules(List<String> rs) { this.rules.addAll(rs); return this; }

        public GameConfig build() {
            if (name == null) throw new IllegalStateException("Name required");
            return new GameConfig(this);
        }
    }
}
```

### 💻 Usage:
```java
GameConfig config = new GameConfig.Builder()
    .name("Battle Royale")
    .addRule("No cheating")
    .addRule("Time limit: 10 min")
    .build();
```

---

## 🛠️ Practical Considerations & Quick Recap

- **Telescoping constructors are unreadable.**
- **Builder separates construction from representation.**
- **Builders work best for immutable classes.**
- **Defaults** can be beautifully set right inside the Builder class.
- **Fluent setters** (`return this;`) vastly improve readability.
- **Enforce required fields** via the Builder's constructor or throwing exceptions in `build()`.
- **Use defensive copies** for collections inside the core object constructor to preserve immutability.
- Libraries like **Lombok** can auto-generate builders (`@Builder`), but they may hide complex or custom validation.

---

## 🎙️ Frequently Asked Interview Questions (Viva)

#### Q1: What problem does the Builder pattern solve?
**Answer**: It solves the "Telescoping Constructor" anti-pattern where a class has too many constructor parameters, many of which might be optional. It provides a readable, fluent, step-by-step approach to constructing complex objects without confusing callers.

#### Q2: How can required fields be enforced in a Builder?
**Answer**: There are two primary ways:
1. Pass the highly mandatory fields directly into the `Builder`'s constructor. 
2. Adding checks/validations inside the final `.build()` method and throwing an `IllegalStateException` or `IllegalArgumentException` if criteria aren't met.

#### Q3: Why is Builder often used with immutable objects?
**Answer**: Because immutable classes require all their state to be assigned exactly once at creation (via a constructor). If an object has many fields, an immutable constructor becomes massive. The Builder gathers all the data cleanly over multiple steps, and then passes it atomically into that massive `private` constructor via the `build()` method, giving us the best of both worlds (readability + strict immutability).

#### Q4: Compare Builder with telescoping constructors in readability.
**Answer**: A telescoping constructor forces the caller to write code like `new Order("C1", 2, true, false, true)`, forcing developers to memorize the parameter index/types. A Builder uses method chaining (Fluent API), resulting in `new Order.Builder("C1").priority(2).giftWrap(true).build()`, effectively self-documenting the code.

---

---

### 🌍 Real-world Analogy: StringBuilder

A perfect example of the Builder pattern in the Java Standard Library is **`StringBuilder`**. It allows you to build a complex string piece-by-piece and only produces the final, immutable `String` when you are done.

```java
public class Main {
    public static void main(String[] args) {
        // Step-by-step construction (The Builder Phase)
        String result = new StringBuilder()
                            .append("Hello")
                            .append(" ")
                            .append("World")
                            .append("!")
                            .toString(); // The Build Phase (Creates Immutable String)

        System.out.println(result);
    }
}
```

#### The Parallel:
| Builder Pattern Stage | `StringBuilder` Equivalent |
| :--- | :--- |
| **Builder Object** | `new StringBuilder()` |
| **Chained Setters** | `.append("...")` |
| **build() / F() Method** | `.toString()` |
| **Final Immutable Object** | `String` |

---

## 🎯 Summary
| Version | Approach | Pros | Cons |
| :--- | :--- | :--- | :--- |
| **V0** | Eager | Simple, Thread-safe | High memory/startup cost |
| **V1** | Lazy | Saves memory | Not Thread-safe |
| **V2** | Synchronized | Thread-safe | **Cost-heavy** (Blocking) |
| **V2.5** | Half-Baked | Faster | **Race Condition** |
| **V3** | **DCL + Volatile** | **Fast & Safe** | Complex theory |
| **V4** | **Enum** | **Bulletproof** | No lazy loading (mostly) |
| **V5** | **Bill Pugh** | **Elegant & Lazy** | Slightly obscure |
 