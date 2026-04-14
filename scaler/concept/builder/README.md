# 🏗️ Builder Design Pattern

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
*Created for viva preparation using notes from Scaler LLD sessions.*
