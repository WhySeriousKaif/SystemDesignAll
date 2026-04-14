# 🧬 Prototype Design Pattern

**Intent:** Specify the kinds of objects to create using a prototypical instance, and create new objects by copying this prototype.
**Summary:** Instead of creating objects from scratch (which might require complex configuration, database calls, or heavy constructor logic), you take an existing, fully configured object and **clone** it.

---

## 🛑 Problem: The Heavy Initialization & Coupling
Imagine a Vector Graphics application where users can draw `Rectangle`, `Ellipse`, and `TextBox`.
If you want to duplicate an existing shape on the canvas, how do you do it?
*   **Bad Approach**: Checking the type directly `if (shape instanceof Rectangle) { return new Rectangle(...); }`. This couples your duplication logic to every concrete class.
*   **Worse Approach**: The constructor for these internal shapes might be heavy or rely on private fields you don't even have access to!

---

## 🛠️ Solution: The `clone()` Contract
Force every shape to know how to duplicate itself by implementing a common `CloneableGraphics` interface.

### 1. The Prototypical Interface
```java
interface Graphic {
    Graphic clone(); // The secret sauce
    void draw();
}
```

### 2. Concrete Prototypes (The self-cloners)
Notice how we use a **private copy constructor** to easily transfer state.

```java
class Rectangle implements Graphic {
    private int width, height;
    private String color; // Extrinsic state

    public Rectangle(int width, int height, String color) {
        // Assume this constructor takes 500ms due to GPU context setup
        this.width = width;
        this.height = height;
        this.color = color;
    }

    // Copy Constructor - Fast and secure
    private Rectangle(Rectangle source) {
        this.width = source.width;
        this.height = source.height;
        this.color = source.color;
    }

    @Override
    public Graphic clone() {
        return new Rectangle(this); // Lightning fast duplication
    }

    public void setColor(String color) { this.color = color; }
    public void draw() { System.out.println("Rectangle: " + color); }
}
```

---

## 📖 Advanced Usage: The Prototype Registry
In real systems, we often combine Prototype with a **Registry** (a smart HashMap). Instead of calling `new`, you ask the Registry to give you a customized clone of a pre-existing master template.

```java
class GraphicRegistry {
    private Map<String, Graphic> cache = new HashMap<>();

    public void addMaster(String key, Graphic masterObject) {
        cache.put(key, masterObject);
    }

    public Graphic getClone(String key) {
        // Returns a freshly cloned object, NOT the master itself!
        return cache.get(key).clone(); 
    }
}

// Client Demo
public class Application {
    public static void main(String[] args) {
        GraphicRegistry registry = new GraphicRegistry();
        
        // 1. Initial Heavy Setup (Done once at startup)
        registry.addMaster("RedButton", new Rectangle(100, 50, "Red"));
        registry.addMaster("BlueHeader", new Rectangle(1920, 100, "Blue"));

        // 2. Fast Mass Production
        Graphic btn1 = registry.getClone("RedButton");
        Graphic btn2 = registry.getClone("RedButton");
        // btn1 and btn2 are distinct graphical objects with the same properties!
    }
}
```

---

## ⚡ Shallow vs Deep Copy (Viva Heuristics)

If your Prototype contains objects inside it (like a `List` of vertex points):
*   **Shallow Copy**: The clone copies the *reference* to the list. If `btn1` modifies its list, `btn2`'s list magically changes too. (DANGEROUS).
*   **Deep Copy**: The `clone()` method must manually iterate through the list and create fresh clones of all internal elements. (SAFE, but takes effort).

*Usually, a proper Copy Constructor manually enforces Deep Copying for mutable collections.*

---

## ⚔️ Pattern Wars: Factory Method vs Prototype
| Factory Method | Prototype |
| :--- | :--- |
| Focuses on defining an interface for creating *one type* of object and deferring instantiation. | Focuses on *copying* an already existing, fully configured object. |
| Needs a new sub-factory for every new type of product. | No subclassing required, just register a new master instance! |
| Great for static architecture logic. | Great for dynamic, composite objects. |

---

## 🎙️ Frequently Asked Interview Questions (Viva)

#### Q1: Why use a Registry with the Prototype pattern?
**Answer**: A Registry acts as a catalog of "Master Templates". It allows the application to keep track of frequently used complex configurations dynamically. Instead of hardcoding `new Rectangle(...)` anywhere, the client asks the registry for a clone of `"RedButton"`, vastly improving maintainability and performance.

#### Q2: Why is the Prototype pattern sometimes favored over the Factory pattern?
**Answer**: If you have a system where objects have dozens of minor variations (e.g., T-Shirts with variations in size, color, brand, print), using Factories requires creating dozens of `TShirtFactory` subclasses. Using Prototypes, you just configure a few `TShirt` instances, dump them in a Registry, and clone the one that closely matches your need, tweaking only what's necessary on the clone. No extra factory classes needed!

#### Q3: What is the risk of using `super.clone()` in Java?
**Answer**: `java.lang.Object.clone()` is a JVM-level shallow copy. It perfectly copies primitives, but it copies memory addresses for references (like Arrays or custom objects). If you change an array in the clone, it breaks the original. That is why using a private "Copy Constructor" `new Rectangle(this)` is often safer and much more readable in modern Java.

#### Q4: Name a real-world system where Prototype is essential.
**Answer**: Game Development (cloning enemy NPCs from a master template), Graphic Design software (copy-pasting elements), and Configuration systems where dynamic user-profiles need to be duplicated before editing to allow for "Undo" functionality.

---
*Created for viva preparation using notes from Scaler LLD sessions.*
