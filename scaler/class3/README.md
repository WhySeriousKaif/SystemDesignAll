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
Recommended by Joshua Bloch (Effective Java), this is the simplest and most secure way.

```java
public enum FileStorageService {
    INSTANCE;
    
    public void log(String msg) {
        System.out.println("Logging: " + msg);
    }
}
```
- **Why it's great**: 
    - Inherently thread-safe by the JVM.
    - Protected against Reflection attacks.
    - Handles Serialization automatically.
- **Interview Tip**: "It's the only approach that provides a concrete guarantee against multiple instantiation, even in complex scenarios like serialization."

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

## 🔒 Immutability in Java

An **Immutable Class** is one whose state cannot be changed after it is constructed. Applications like `String` and `Integer` are immutable by design.

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

#### 💻 Comprehensive Example
```java
public final class ImmutableDemo {
    private final int a;
    private final ArrayList<Integer> list;

    public ImmutableDemo(int a, ArrayList<Integer> list) {
        this.a = a;
        // DEEP COPY in Constructor: 
        // Prevents main() from changing our list after passing it
        this.list = new ArrayList<>(list); 
    }

    public ArrayList<Integer> getList() {
        // DEEP COPY in Getter: 
        // User modifies the deep copy, not our internal list
        return new ArrayList<>(this.list);
    }
}

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        ImmutableDemo obj = new ImmutableDemo(10, list);

        // This modification only affects the copy returned by getList()
        obj.getList().add(10);
        obj.getList().add(90);

        // The internal list remains EMPTY!
        System.out.println("Object List Size: " + obj.getList().size()); // Prints 0
    }
}
```

---

## 🏗️ Builder Design Pattern: An Evolutionary Story
->providing flexibility when it comes to object creation of Immutable classes

The Builder pattern is used to construct complex objects. Instead of one giant constructor, we use a separate "workspace" to collect data. The design evolved through three distinct stages to reach the standard "Static Inner Class" pattern we use today.

---

### Stage 1: Separate Classes (`MasterClass` & `X`)
Initially, we might create two separate classes: one for the final object (`MasterClass`) and one for the configuration (`X`).

```java
// Logic: MasterClass is immutable
class MasterClass {
    private final int a;
    private final int b;
    private final String c;

    public MasterClass(X config) {
        this.a = config.a;
        this.b = config.b;
        this.c = config.c;
    }

    public void display() { System.out.println(a + " " + b + " " + c); }
}

// X is a separate mutable helper class
class X {
    public int a;
    public int b;
    public String c;
}

public class Main {
    public static void main(String[] args) {
        X config = new X();
        config.a = 10;
        config.b = 20;
        config.c = "Stage 1";

        MasterClass obj = new MasterClass(config);
        obj.display();
    }
}
```
- **The Problem**: 
    - **Reduced Abstraction**: The client has to know about and manage two completely separate classes.
    - **Maintainability**: If you add a variable to `MasterClass`, you must manually remember to add it to `X`. They are disconnected.

---

### Stage 2: Non-Static Inner Class
To improve maintainability, we move `X` inside `MasterClass`. This keeps the code together.

```java
class MasterClass {
    private int a;
    // ... other fields

    public MasterClass(X config) {
        this.a = config.a;
    }

    // Non-static inner class
    class X {
        public int a;
        public X setA(int a) { this.a = a; return this; }
    }
}

public class Main {
    public static void main(String[] args) {
        // ERROR: To create X, you need an instance of MasterClass!
        // X helper = new MasterClass.X(); 
        
        // But to create MasterClass, you need an instance of X!
        // MasterClass obj = new MasterClass(helper);
    }
}
```
- **The Problem**: **Circular Dependency Paradox**. Since `X` is a non-static inner class, it belongs to an *instance* of `MasterClass`. You can't create the helper without the object, but you can't create the object without the helper.

---

### Stage 3: The Standard Builder (Static Inner Class)
By making `X` (now called `Builder`) a **static inner class**, it no longer belongs to an instance. It belongs to the class itself.

```java
public class MasterClass {
    private final int a;
    private final int b;
    private final String c;

    private MasterClass(Builder builder) {
        this.a = builder.a;
        this.b = builder.b;
        this.c = builder.c;
    }

    // Static Inner Class: The perfect workspace
    public static class Builder {
        private int a;
        private int b;
        private String c;

        public Builder setA(int a) { this.a = a; return this; }
        public Builder setB(int b) { this.b = b; return this; }
        public Builder setC(String c) { this.c = c; return this; }

        public MasterClass build() {
            return new MasterClass(this);
        }
    }

    public void display() { System.out.println(a + " " + b + " " + c); }
}

public class Main {
    public static void main(String[] args) {
        // --- Approach 1: Verbose (The 3-Step Way) ---
        // 1. Create the builder object
        MasterClass.Builder helper = new MasterClass.Builder();
        // 2. Set values
        helper.setA(100);
        helper.setB(200);
        helper.setC("Verbose");
        // 3. Pass to constructor (or call build)
        MasterClass obj1 = helper.build();

        // --- Approach 2: Concise (The Improved Way) ---
        // Using method chaining and the F() method (build)
        MasterClass obj2 = new MasterClass.Builder()
                            .setA(100)
                            .setB(200)
                            .setC("Concise")
                            .build(); // Instructor referred to this as F()
        
        obj1.display();
        obj2.display();
    }
}
```
- **The Solution**: 
    - **Self-Contained**: The logic is all in one file.
    - **Static Access**: We can create the `Builder` without needing a `MasterClass` object first.
    - **Method Chaining**: The client code becomes much more readable and concise.
    - **Atomicity**: The client doesn't need to manage the helper object reference (`helper`) explicitly.

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
 