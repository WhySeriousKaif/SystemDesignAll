# 🍕 Decorator Pattern – Thumb Rules with Code

The **Decorator Design Pattern** is used to add responsibilities to objects dynamically without subclassing.

---

### 1️⃣ Component (Parent) → Interface OR Abstract Class

#### ✅ Option A: Interface (Most Common)
```java
interface Pizza {
    String getDescription();
    double getCost();
}
```
- **Pros**: Flexible, supports multiple implementations, most used in interviews.

#### ✅ Option B: Abstract Class
```java
abstract class Pizza {
    public abstract String getDescription();
    public abstract double getCost();
}
```
- **Pros**: Use when you want default/common logic or shared fields.

---

### 2️⃣ Concrete Component (Base Object)
👉 Same for both interface & abstract

```java
class BasicPizza implements Pizza { // if interface
// class BasicPizza extends Pizza { // if abstract

    public String getDescription() {
        return "Basic Pizza";
    }

    public double getCost() {
        return 5.0;
    }
}
```

---

### 3️⃣ Decorator MUST follow → IS-A + HAS-A

#### ✅ If Component is Interface
```java
abstract class PizzaDecorator implements Pizza {
    protected Pizza pizza; // HAS-A

    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    public String getDescription() {
        return pizza.getDescription(); // delegation
    }

    public double getCost() {
        return pizza.getCost();
    }
}
```

#### ✅ If Component is Abstract Class
```java
abstract class PizzaDecorator extends Pizza {
    protected Pizza pizza; // HAS-A

    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    public String getDescription() {
        return pizza.getDescription();
    }

    public double getCost() {
        return pizza.getCost();
    }
}
```

---

### 4️⃣ Concrete Decorators → Add Behavior

#### Example: Cheese
```java
class CheeseDecorator extends PizzaDecorator {
    public CheeseDecorator(Pizza pizza) {
        super(pizza);
    }

    public String getDescription() {
        return pizza.getDescription() + ", Cheese";
    }

    public double getCost() {
        return pizza.getCost() + 1.5;
    }
}
```

#### Example: Veggies
```java
class VeggiesDecorator extends PizzaDecorator {
    public VeggiesDecorator(Pizza pizza) {
        super(pizza);
    }

    public String getDescription() {
        return pizza.getDescription() + ", Veggies";
    }

    public double getCost() {
        return pizza.getCost() + 0.5;
    }
}
```

---

### 5️⃣ Rule: Always Delegate + Enhance

❌ **Wrong (replacing behavior)**
```java
public double getCost() {
    return 10; // ignores base pizza ❌
}
```

✅ **Correct (enhancing)**
```java
public double getCost() {
    return pizza.getCost() + 1.5;
}
```

---

### 6️⃣ Runtime Wrapping (Core Idea)

```java
Pizza pizza =
    new VeggiesDecorator(
        new CheeseDecorator(
            new BasicPizza()
        )
    );
```

---

### 7️⃣ Full Example (Main)

```java
public class Main {
    public static void main(String[] args) {
        // Basic
        Pizza p1 = new BasicPizza();

        // Cheese
        Pizza p2 = new CheeseDecorator(new BasicPizza());

        // Cheese + Veggies
        Pizza p3 = new VeggiesDecorator(
                        new CheeseDecorator(
                            new BasicPizza()
                        )
                   );

        System.out.println(p1.getDescription() + " → $" + p1.getCost());
        System.out.println(p2.getDescription() + " → $" + p2.getCost());
        System.out.println(p3.getDescription() + " → $" + p3.getCost());
    }
}
```

---

### 8️⃣ Mental Model (Must Remember)
- **IS-A** → Decorator is a Pizza
- **HAS-A** → Decorator contains a Pizza

---

### 9️⃣ When to use Interface vs Abstract (Interview Gold)
| Case | Use |
| :--- | :--- |
| Only behavior contract | Interface ✅ |
| Need shared code/state | Abstract class ✅ |

---

### 🔟 Final One-Line Answer (Interview)
👉 **“Decorator uses a component interface/abstract class, an abstract decorator holding that component, and concrete decorators that wrap and extend behavior dynamically using composition.”**

---

## 🔥 Next Level: Advanced Interview Content

### ❗ Common Mistakes (The Tricky Traps)
- **Order of Wrapping**: While decorators are interchangeable, sometimes the order matters (e.g., adding tax should usually be the final wrap).
- **Over-Decoration**: Creating too many layers can make debugging difficult (the "Stack Overflow" of wrappers).
- **Infinite Loops**: A decorator accidentally wrapping itself or creating a circular dependency.

### ⚔️ Decorator vs Strategy vs Proxy
- **Decorator**: Adds features to an **existing instance** at runtime. (You bring your own object).
- **Strategy**: Changes the **internal algorithm** of an object. (Object has an internal slot for a strategy).
- **Proxy**: Controls **access** to an object (Security, Caching, Lazy Loading). Proxy usually manages the lifecycle of the object it hides.

### 🏗️ Real LLD Question: Coffee Customization (Starbucks/Swiggy)
- **Requirement**: A base `Beverage` (HouseBlend, DarkRoast) with varied condiments (Milk, Soy, Mocha, Whip).
- **Solution**: 
    - `Beverage` is the Component.
    - `Mocha` is a Decorator.
    - `Coffee myBrew = new Mocha(new Whip(new HouseBlend()));`
- **Why?**: Avoids 2^N classes for every combination of coffee and condiments.
