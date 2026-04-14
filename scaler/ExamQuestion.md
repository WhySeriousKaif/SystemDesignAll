# 🚀 Core Java & LLD Concepts — Viva Ready Revision

Master the fundamental traps and concepts of Low-Level Design and Java. This document is structured into MCQ-style breakdowns, execution traces, and interview-ready summaries.

---

## 📑 Table of Contents
1. [🧠 Concept Summaries (At-a-glance)](#-concept-summaries-at-a-glance)
2. [📝 Complete Question Bank (Q1–Q20)](#-complete-question-bank-q1q20)
3. [🔥 Final Master Summary](#-final-master-summary)

---

## 🧠 Concept Summaries (At-a-glance)

Note: Detailed reasoning for these topics is provided in the Question Bank below.

---

## 🧠 Singleton Design Pattern

### MCQ Concepts — Truth Table

| Statement | Evaluation | Key Reasoning |
| :--- | :--- | :--- |
| Singleton object’s attributes cannot be changed after creation | ❌ **False** | Singleton ≠ Immutable. Fields can be modified unless marked `final`. |
| Eager loading is thread-safe without double-check locking | ✅ **True** | JVM guarantees thread safety during class initialization. |
| Enum is by default singleton in Java | ✅ **True** | JVM handles serialization, reflection attacks, and thread safety. |
| Singleton class must be declared as `static` | ❌ **False** | Top-level classes cannot be static; depends on `private constructor` + `static instance`. |
| All methods in a singleton must be declared `static` | ❌ **False** | Only `getInstance()` is static; business methods are instance methods. |

#### 💡 Eager Loading Example
```java
class Singleton {
    private static final Singleton instance = new Singleton();
    private Singleton() {}

    public static Singleton getInstance() {
        return instance;
    }
}
```

> [!TIP]
> **Enum Singleton (Best Practice):** Use `enum Singleton { INSTANCE; }`. It's the most robust against reflection and serialization attacks.

---

## 🏗️ Immutability & Class Design

### Designing Immutable Classes
Rules for ensuring an object's state cannot change:

1.  **No Setters:** Do not provide mutator methods. ✅
2.  **Encapsulation:** All fields should be `private` and ideally `final`. ✅
3.  **Defensive Copying:** If the class contains collections or mutable objects, return copies, not original references. 

#### ❌ Common Traps
*   **"No collections allowed":** False. You can have collections, just use unmodifiable views or defensive copies.
*   **"No getters for objects":** False. You can have getters, but return a copy.

```java
// Defensive Copy Example
public List<String> getList() {
    return new ArrayList<>(list); 
}
```

---

## ⚡ The `final` Keyword

### Deep Dive Traps

| Statement | Evaluation | Why? |
| :--- | :--- | :--- |
| A `final` class ensures all instances are immutable. | ❌ **False** | `final` class only prevents inheritance. Fields can still be mutable. |
| `final` variables can be assigned exactly once, but not necessarily at declaration. | ✅ **True** | Can be initialized in constructors or initializer blocks. |
| Declaring a reference as `final` makes the object immutable. | ❌ **False** | Only the reference is fixed; internal state of the object can change. |
| A `final` variable must be initialized at declaration. | ❌ **False** | Can be delayed to constructor or initializer block. |
| A `final` method cannot be overridden. | ✅ **True** | Core definition of the keyword for methods. |

#### 🔗 Reference vs Object Immutability
```java
final List<String> list = new ArrayList<>();
list.add("Hello");   // ✅ Allowed (state changed)
// list = new ArrayList<>(); // ❌ Error (reference cannot change)
```

---

## 🧬 Static Nested Classes

### Key Rules
*   **Instantiation:** Requires outer class name: `Outer.Inner obj = new Outer.Inner();`.
*   **Access:** Can access **private** members of the outer class. ✅
*   **Top-level:** Top-level classes cannot be `static`.
*   **Non-static members:** Static nested classes **can** have non-static fields/methods. 

> [!IMPORTANT]
> A static nested class behaves like a normal top-level class that is simply scoped inside another for packaging convenience. It is **NOT** a singleton by default.

---

## 🔒 Final Class & Methods

### Final Class Traps
*   **Subclassing:** Impossible. ✅
*   **Methods:** NOT implicitly `final` (though they can't be overridden anyway). ❌
*   **Abstract:** A `final` class cannot have abstract methods (conflict). ✅
*   **Variables:** NOT required to be `final`. ❌

### Final Method Traps
*   **Delayed Init:** Final instance variables **must** be initialized before the constructor completes. ✅
*   **Access:** Final methods can access any attribute (not just final ones). ✅
*   **Overloading:** `final` does NOT prevent overloading. ✅
*   **Abstract Class:** Can exist inside an abstract class. ✅

---

## 🌗 Abstract Classes vs Interfaces

| Feature | Abstract Class | Interface |
| :--- | :--- | :--- |
| **Constructor** | ✅ Allowed | ❌ Not Allowed |
| **Variables** | Normal instance fields | Only `public static final` (constants) |
| **Inheritance** | Single inheritance only | Multiple inheritance allowed |
| **Default Methods** | N/A | Must resolve conflicts if duplicate |

> [!NOTE]
> An abstract class can implement an interface **without** providing implementations for its methods. The responsibility is passed to the concrete subclass.

---

## 🎭 Polymorphism & Constructors

### Rules of Engagement
*   **Runtime Polymorphism:** Achieved via method **overriding**. (Dynamic Dispatch)
*   **Compile-time Polymorphism:** Achieved via method **overloading**.
*   **Private Methods:** Cannot be overridden (not inherited).
*   **Access Modifiers:** You can **increase** visibility but never **decrease** it during overriding (`protected` → `public` ✅, `public` → `protected` ❌).
*   **Constructor Order:** Parent constructor executes **first**, then the child.

---

## 🛰️ Observer Design Pattern

The pattern defines a one-to-many dependency so that when one object changes state, all its dependents are notified automatically.

### Key Characteristics
| Concept | Observer Pattern |
| :--- | :--- |
| **Communication** | Interface-based updates ✅ |
| **Mechanism** | **Push** mechanism (Subject notifies observers) |
| **Loose Coupling** | Subject depends on `Observer` interface, not concrete classes |
| **Thread Safety** | **NOT** guaranteed by default ❌ |
| **Storage** | Subject maintains a list of observers via Collection framework |

#### ❌ The "Polling" Trap
Observers **do not** check the subject's state at regular intervals. That is polling. The Observer pattern is **push-based**.

---

## 📝 Complete Question Bank (Q1–Q20)

### 🧠 Q1 — Singleton Concepts
**❓ Question:** Which of the following statements about Singleton pattern in Java are correct?
1. Singleton object’s attributes’ values cannot be changed after creation
2. **Eager loading singleton solution is thread-safe without double-check locking** ✅
3. **Enum is by default singleton in Java** ✅
4. Singleton class must be declared as static
5. All methods in a singleton class should be declared static

**🔍 Reasoning:**
- **2 (True):** JVM guarantees thread safety for class initialization.
- **3 (True):** Enum is the most robust singleton implementation (handles reflection/serialization).
- **1 (False):** Singleton only ensures one instance; it doesn't enforce immutability.
- **5 (False):** Only `getInstance()` is static; business methods are instance methods.

---

### 🧠 Q2 — Immutable Class Design
**❓ Question:** Which of the following statements are correct for designing an immutable class in Java?
1. The class must not contain any collections or arrays
2. The constructor must be declared private as the object state is not supposed to change
3. **No setter methods or mutator methods should be provided** ✅
4. No getter methods should be provided for non-primitive datatypes
5. **All fields must be properly encapsulated** ✅

**🔍 Reasoning:**
- **3 & 5 (True):** Core rules of immutability.
- **1 (False):** You can have collections, provided you use defensive copies/unmodifiable views.
- **4 (False):** Getters are allowed if they don't leak mutable references.

---

### 🧠 Q3 — Final Keyword Basics
**❓ Question:** Which of the following statements about the final keyword in Java are correct?
1. A final class ensures that all its instances are immutable
2. **Final variables are assigned exactly once, but not necessarily at declaration** ✅
3. Declaring a reference as final makes the object immutable
4. A final variable must always be initialized at declaration
5. **A final method cannot be overridden in a subclass** ✅

**🔍 Reasoning:**
- **2 (True):** Can be initialized in constructor/initializer blocks.
- **5 (True):** Definition of `final` method.
- **3 (False):** Final reference means the reference is fixed, not the object's content.

---

### 🧠 Q4 — Static Nested Class
**❓ Question:** Which of the following statements about static nested classes in Java are correct?
1. Static nested class can be instantiated without using the outer class name
2. **Static nested class can access private members of the outer class** ✅
3. Top-level classes can only be declared static if they contain static nested class
4. Static nested class cannot have non-static members
5. Static nested classes are by-default singleton

**🔍 Reasoning:**
- **2 (True):** Inner/Nested classes always have access to outer class private members.
- **1 (False):** Must use `Outer.Inner obj = new Outer.Inner()`.

---

### 🧠 Q5 — Static Keyword
**❓ Question:** Which of the following statements about static in Java are correct?
1. **Static variables are initialized at class loading time** ✅
2. Static methods can access non-static instance variables using the class name
3. Static methods can call non-static methods using this keyword
4. **Static variables are shared among all instances of a class** ✅
5. Static keyword makes attributes thread-safe

**🔍 Reasoning:**
- **1 & 4 (True):** Standard JVM behavior for static members.
- **5 (False):** `static` does not provide concurrency control; needs `synchronized` or `volatile`.

---

### 🧠 Q6 — Final Class Behavior
**❓ Question:** Which of the following statements about final classes in Java are correct?
1. **Final classes cannot be subclassed** ✅
2. All methods in a final class are implicitly final
3. **Final classes cannot have abstract methods** ✅
4. A final class cannot extend another class
5. All variables in a final class must be final

**🔍 Reasoning:**
- **1 (True):** Core rule.
- **3 (True):** Abstract methods require overriding; final class prevents it.
- **2 (False):** Methods are not marked final, but inheritance is blocked anyway.

---

### 🧠 Q7 — Final Variables & Methods
**❓ Question:** Which of the following statements about final variables and methods are correct?
1. **Final instance variables must be initialized before constructor completes** ✅
2. Final methods can only access final attributes
3. **Final methods cannot be overridden in subclasses** ✅
4. Final methods cannot be overloaded
5. Final methods cannot be present in an abstract class

**🔍 Reasoning:**
- **1 (True):** Delayed init is allowed but must be finished by constructor's end.
- **4 (False):** Overloading is allowed; only overriding is blocked.

---

### 🧠 Q8 — Abstract Class vs Interface
**❓ Question:** Which of the following statements are correct regarding abstract classes and interfaces?
1. **Abstract classes can have constructors, interfaces cannot** ✅
2. **Abstract classes can have instance variables, interfaces can only have constants** ✅
3. A class can extend multiple abstract classes if no conflict exists
4. Compiler resolves conflicts in default methods automatically
5. **An abstract class can implement an interface without implementing its methods** ✅

**🔍 Reasoning:**
- **1, 2, 5 (True):** Fundamental differences/capabilities.
- **3 (False):** Java doesn't support multiple inheritance for classes.

---

### 🧠 Q9 — Polymorphism & OOP Rules
**❓ Question:** Which of the following statements about polymorphism and inheritance are correct?
1. **Runtime polymorphism is achieved through method overriding** ✅
2. **Compile-time polymorphism is achieved through method overloading** ✅
3. Private methods can be overridden in subclasses
4. Constructor chaining starts from the most derived class
5. A subclass can override a method with a more restrictive access modifier

**🔍 Reasoning:**
- **1 & 2 (True):** Definitions of polymorphism types.
- **4 (False):** Parent constructor always executes *before* child logic.
- **5 (False):** Visibility can only be expanded, not restricted.

---

### 🧠 Q10 — Assertion Reason (Singleton + volatile)
**❓ Question:**
**Assertion:** Instance variable must be declared volatile in a lazy Singleton implemented using double-checked locking.
**Reason:** volatile ensures that changes are written to disk and are visible to all threads.

**✅ Final Answer:**
- ✔️ **Assertion:** True (Prevents instruction reordering)
- ❌ **Reason:** False (Reason says "written to disk", but volatile is about **visibility in RAM/Caches**)

---

### 🧩 Advanced Execution Traces (Q11–Q17)

### Q11: Static Method Dispatch
```java
Parent p = new Child();
p.display(); // static void display() in both
```
*   **Output:** `Parent`
*   **Reason:** Static methods use **compile-time binding**. Binding is based on the reference type (`Parent`), not the object type (`Child`).

### Q13: Interface + Abstract + Override
```java
Vehicle v = new Sedan();
v.start(); // Sedan extends Car (overrides start) extends Vehicle (default start)
```
*   **Output:** `Sedan starting`
*   **Reason:** Runtime polymorphism executes the implementation of the actual object type.

### Q14: Design Pattern Mapping
| Requirement | Ideal Pattern |
| :--- | :--- |
| One cart per user session | **Singleton** |
| Swappable discount logic | **Strategy** |
| Optional add-ons (gift wrap, etc.) | **Decorator** |
| Multiple systems react to order placement | **Observer** |
| External payment SDK interface mismatch | **Adapter** |

### Q15: Constructor Chaining Output
```java
new Dog(); // Dog() { this("Unknown"); } -> Dog(String name) { super(name); }
```
*   **Output:** `Animal: Unknown | Dog: Unknown | Dog default |`
*   **Logic:** `this()` and `super()` redirections. Parent constructor always finishes before Child logic executes.

### Q16: Field vs Method Binding
```java
Parent p = new Child();
System.out.println(p.x); // x=10 in Parent, x=20 in Child
p.display(); // display() overridden in Child
```
*   **Output:** `10`, `Child: 20`
*   **Key Insight:** **Fields** use compile-time binding (Reference type). **Methods** use runtime polymorphism (Object type).

---

## 🔁 Hardcore Execution Traces (Q18–Q20)

### 🧠 Q18 — Observer Pattern (Multiple Observers Logic)

**❓ Complete Question**
```java
interface Observer {
    void update(int value);
}

class Subject {
    private List<Observer> observers = new ArrayList<>();
    private int state;

    void addObserver(Observer o) {
        observers.add(o);
    }

    void setState(int value) {
        state = value;
        notifyObservers();
    }

    int getState() {
        return state;
    }

    void notifyObservers() {
        for (Observer o : observers) {
            o.update(state);
        }
    }
}

// Observers
class DoubleObserver implements Observer {
    public void update(int value) {
        if (value % 2 == 0)
            System.out.print(value * 2 + " ");
        else
            System.out.print(value + " ");
    }
}

class SquareObserver implements Observer {
    public void update(int value) {
        if (value > 3)
            System.out.print(value * value + " ");
        else
            System.out.print(value + 1 + " ");
    }
}

class SkipObserver implements Observer {
    public void update(int value) {
        if (value == 4)
            return;
        System.out.print(value - 1 + " ");
    }
}

// Main
Subject s = new Subject();
s.addObserver(new DoubleObserver());
s.addObserver(new SquareObserver());
s.addObserver(new SkipObserver());

s.setState(2);
s.setState(4);
```

**✅ Final Output**
`4 3 1 8 16`

**🔍 Step-by-step execution**
*   **🔹 Call 1 → s.setState(2)**
    *   `state = 2`
    *   `Observer 1 (Double)`: 2 % 2 == 0 → YES → print `4`
    *   `Observer 2 (Square)`: 2 > 3 → NO → print 2 + 1 = `3`
    *   `Observer 3 (Skip)`: 2 == 4 → NO → print 2 - 1 = `1`
*   **🔹 Call 2 → s.setState(4)**
    *   `state = 4`
    *   `Observer 1 (Double)`: 4 % 2 == 0 → YES → print `8`
    *   `Observer 2 (Square)`: 4 > 3 → YES → print 4 * 4 = `16`
    *   `Observer 3 (Skip)`: 4 == 4 → YES → **return** (no print)

> [!IMPORTANT]
> **Key Insight:** Observers execute in insertion order (Sequential loop). No parallel execution by default.

---

### 🧠 Q19 — Observer + Recursive State Change (VERY IMPORTANT 🔥)

**❓ Complete Question**
```java
interface Observer {
    void update(Subject s);
}

class Subject {
    private List<Observer> observers = new ArrayList<>();
    private int state;

    void addObserver(Observer o) {
        observers.add(o);
    }

    void setState(int value) {
        state = value;
        notifyObservers();
    }

    int getState() {
        return state;
    }

    void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }
}

// Observers
class PrintObserver implements Observer {
    public void update(Subject s) {
        System.out.print(s.getState() + " ");
    }
}

class ModifyObserver implements Observer {
    public void update(Subject s) {
        if (s.getState() == 2) {
            s.setState(3);
        }
    }
}

class ConditionalObserver implements Observer {
    public void update(Subject s) {
        if (s.getState() > 2) {
            System.out.print((s.getState() * 2) + " ");
        }
    }
}

// Main
Subject s = new Subject();
s.addObserver(new PrintObserver());
s.addObserver(new ModifyObserver());
s.addObserver(new ConditionalObserver());

s.setState(2);
```

**✅ Final Output**
`2 3 6 6`

**🔍 Deep Execution Tracing**
1.  **Step 1 → `setState(2)`**: `state = 2`.
    *   `PrintObserver`: Prints `2`.
    *   `ModifyObserver`: Sees `2` → triggers **`setState(3)`**.
2.  **Step 2 → `setState(3)` (RECURSION)**: `state = 3`.
    *   `PrintObserver`: Prints `3`.
    *   `ModifyObserver`: Sees `3` → No action.
    *   `ConditionalObserver`: 3 > 2 → Prints `3 * 2 = 6`.
3.  **Step 3 → Returning to Step 1 loop**: Original `setState(2)` resumes for the last observer.
    *   `ConditionalObserver`: **State is now 3** (updated mid-loop) → 3 > 2 → Prints `3 * 2 = 6`.

> [!CAUTION]
> **Interview Trap:** Observer mutation during notification leads to nested `notify` loops (recursion). State changes mid-loop affect the remaining observers in the previous stack.

---

### 🧠 Q20 — Assertion Reason (Prototype Pattern)

**❓ Complete Question**
**Assertion:** Prototype pattern is useful when creating large number of similar objects efficiently.  
**Reason:** Prototype achieves efficiency by sharing intrinsic state and externalizing varying state.

**✅ Final Answer**
*   ✔️ **Assertion:** True (Avoids `new` + heavy initialization)
*   ❌ **Reason:** False (This describes the **Flyweight** pattern, NOT Prototype)

| Pattern | Mental Model |
| :--- | :--- |
| **Prototype** | Clone existing objects |
| **Flyweight** | Share common (intrinsic) state |

---

## 🔥 Final Master Summary

### 🚨 The "Big 8" Interview Traps
1.  **Static = Compile-time:** No runtime polymorphism for static methods.
2.  **Fields = Compiled-time:** Bound to the reference type, not the object.
3.  **Methods = Runtime:** Bound to the actual object type.
4.  **Observer Order:** Usually sequential (insertion order), not parallel.
5.  **Observer Recursion:** State changes mid-loop affect remaining observers.
6.  **Decorator Growth:** Objects are wrapped "inside-out".
7.  **Constructor Order:** Parent **ALWAYS** runs before Child.
8.  **Volatile:** Guarantees visibility and ordering, NOT disk persistence.

---

> [!TIP]
> **Revision Hack:** If you see `static` or `field access`, look at the **Reference Type**. If you see `instance method`, look at the **Object Type**.

---
