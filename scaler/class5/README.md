# Class 5: Advanced Design Patterns

This session covers powerful **Creational** and **Structural** design patterns: **Prototype, Proxy, and Flyweight**.

---

## 🏗️ Creational Patterns: Prototype

### 🧠 Prototype Pattern – Simple Version (Exam Ready)

✅ **1. Interface**
```java
interface Shape {
    Shape copy();   // clone method
}
```

✅ **2. Concrete Class**
```java
class Rectangle implements Shape {
    int width, height;

    Rectangle(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public Shape copy() {
        return new Rectangle(width, height); // cloning
    }

    public void setWidth(int w) {
        this.width = w;
    }

    public String toString() {
        return "Rectangle: " + width + " x " + height;
    }
}
```

✅ **3. Registry**
```java
import java.util.*;

class ShapeRegistry {
    private Map<String, Shape> map = new HashMap<>();

    void register(String key, Shape shape) {
        map.put(key, shape);
    }

    Shape get(String key) {
        return map.get(key).copy(); // return clone
    }
}
```

✅ **4. Client Example**
```java
public class Main {
    public static void main(String[] args) {
        ShapeRegistry registry = new ShapeRegistry();

        // Register prototype
        registry.register("rect", new Rectangle(10, 5));

        // Clone and modify
        Rectangle r1 = (Rectangle) registry.get("rect");
        r1.setWidth(20); // small change

        Rectangle r2 = (Rectangle) registry.get("rect");

        System.out.println(r1);
        System.out.println(r2);
    }
}
```

🎯 **Output**
```text
Rectangle: 20 x 5
Rectangle: 10 x 5
```

⚡ **Viva Explanation (VERY IMPORTANT)**

👉 **What is Prototype Pattern?**
Instead of creating object from scratch, we copy (clone) an existing object.

👉 **Why use it?**
*   Faster than new object creation
*   Avoid complex constructor logic

👉 **Key Parts**
*   **Interface** → `copy()`
*   **Concrete class** → implements clone
*   **Registry** → stores prototypes
*   **Client** → asks registry for clone

👉 **Important Point**
*   `r1` changed → does NOT affect `r2` (because clone)

🔥 **One-Line Memory Trick**
👉 “Store one object → clone many → modify each”

---


---

## 🎀 Structural Patterns: Decorator Pattern

### 🧠 Decorator Pattern – Notification Example

💡 **Idea**
👉 “Add features dynamically (Email → Email+SMS → Email+SMS+WhatsApp)”

✅ **1. Interface**
```java
interface Notifier {
    void send(String msg);
}
```

✅ **2. Base Class (Initial Email)**
```java
class EmailNotifier implements Notifier {
    public void send(String msg) {
        System.out.println("Sending Email: " + msg);
    }
}
```

✅ **3. Decorator Base Class**
```java
abstract class NotifierDecorator implements Notifier {
    protected Notifier notifier;

    NotifierDecorator(Notifier notifier) {
        this.notifier = notifier;
    }

    public void send(String msg) {
        notifier.send(msg);
    }
}
```

✅ **4. Concrete Decorators**

📩 **SMS Decorator**
```java
class SMSDecorator extends NotifierDecorator {
    SMSDecorator(Notifier notifier) {
        super(notifier);
    }

    public void send(String msg) {
        super.send(msg);
        System.out.println("Sending SMS: " + msg);
    }
}
```

💬 **WhatsApp Decorator**
```java
class WhatsAppDecorator extends NotifierDecorator {
    WhatsAppDecorator(Notifier notifier) {
        super(notifier);
    }

    public void send(String msg) {
        super.send(msg);
        System.out.println("Sending WhatsApp: " + msg);
    }
}
```

✅ **5. Client Example**
```java
public class Main {
    public static void main(String[] args) {
        // Email + SMS + WhatsApp
        Notifier n3 = new WhatsAppDecorator(
                            new SMSDecorator(
                                new EmailNotifier()));
        n3.send("Hello");
    }
}
```

🎯 **Output**
```text
Sending Email: Hello
Sending SMS: Hello
Sending WhatsApp: Hello
```

⚡ **Viva Explanation (VERY IMPORTANT)**

👉 **What is Decorator?**
Add new behavior without changing original class.

👉 **Flow**
Email → wrapped by SMS → wrapped by WhatsApp.

👉 **Key Benefit**
*   Flexible combinations
*   No class explosion

👉 **Important Point**
*   Each decorator wraps another object.

🔥 **One-Line Memory Trick**
👉 “Wrap object → add feature → chain multiple”

---

## 🛡️ Structural Patterns: Proxy Pattern

### 🧠 Proxy Pattern – TextParser Example

✅ **1. Interface**
```java
interface TextParser {
    String parse(String text);
}
```

✅ **2. Real Class (Heavy Work)**
```java
class RealTextParser implements TextParser {
    public String parse(String text) {
        System.out.println("Parsing text...");
        return text.toUpperCase(); // heavy processing (example)
    }
}
```

✅ **3. Proxy Class (Controls Access)**
```java
class ProxyTextParser implements TextParser {
    private RealTextParser realParser;

    public String parse(String text) {
        // Access control (example: block empty text)
        if (text == null || text.isEmpty()) {
            return "Invalid input!";
        }

        // Lazy initialization
        if (realParser == null) {
            realParser = new RealTextParser();
        }

        return realParser.parse(text);
    }
}
```

✅ **4. Client Example**
```java
public class Main {
    public static void main(String[] args) {
        TextParser parser = new ProxyTextParser();

        System.out.println(parser.parse("hello world"));
        System.out.println(parser.parse("")); // blocked by proxy
    }
}
```

🎯 **Output**
```text
Parsing text...
HELLO WORLD
Invalid input!
```

⚡ **Viva Explanation (IMPORTANT)**

👉 **What happens here?**
*   Client talks to Proxy
*   Proxy checks input (control)
*   Then calls `RealTextParser`

👉 **Why Proxy here?**
*   Validate input
*   Avoid unnecessary object creation
*   Add control before real logic

🔥 **One-Line Memory Trick**
👉 “Proxy checks → then forwards to real object”


---

## 🪶 Structural Patterns: Flyweight Pattern

### 🧠 Flyweight Pattern – Bullet Example

💡 **Idea**
👉 “BulletType = shared (heavy), Bullet = unique (position)”

✅ **1. Flyweight Class (Shared Object)**
```java
class BulletType {
    String color;
    String image; // heavy data

    public BulletType(String color, String image) {
        this.color = color;
        this.image = image;
        System.out.println("Creating BulletType: " + color);
    }

    public void display(int x, int y) {
        System.out.println("Bullet " + color + " at (" + x + "," + y + ")");
    }
}
```

✅ **2. Bullet (Context – Unique Data)**
```java
class Bullet {
    int x, y; // extrinsic data
    BulletType type; // shared

    public Bullet(int x, int y, BulletType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void draw() {
        type.display(x, y);
    }
}
```

✅ **3. Flyweight Factory**
```java
import java.util.*;

class BulletFactory {
    private static Map<String, BulletType> map = new HashMap<>();

    public static BulletType getBulletType(String color) {
        if (!map.containsKey(color)) {
            map.put(color, new BulletType(color, "bullet.png"));
        }
        return map.get(color); // reuse
    }
}
```

✅ **4. Client Example**
```java
public class Main {
    public static void main(String[] args) {
        Bullet b1 = new Bullet(10, 20, BulletFactory.getBulletType("red"));
        Bullet b2 = new Bullet(30, 40, BulletFactory.getBulletType("red"));
        Bullet b3 = new Bullet(50, 60, BulletFactory.getBulletType("blue"));

        b1.draw();
        b2.draw();
        b3.draw();
    }
}
```

🎯 **Output**
```text
Creating BulletType: red
Creating BulletType: blue
Bullet red at (10,20)
Bullet red at (30,40)
Bullet blue at (50,60)
```

⚡ **Viva Explanation (VERY IMPORTANT)**

👉 **What is shared?**
*   `BulletType` (color, image) → heavy → reused

👉 **What is unique?**
*   `x, y` → position

👉 **Factory Role**
*   Creates object only once
*   Returns same object again

👉 **Key Point**
*   100 bullets → maybe only 2 `BulletType` objects.

🔥 **One-Line Memory Trick**
👉 “Same bullet type → different positions → reuse”


---

## 🎯 Summary Table

| Pattern | Type | Real-World Story | Core Benefit |
| :--- | :--- | :--- | :--- |
| **Prototype** | Creational | Rectangle Cloning | Fast creation from masters |
| **Decorator** | Structural | Notification Layers | Dynamic behavior additions |
| **Proxy** | Structural | Access Middleman | Control, Lazy Loading, Validation |
| **Flyweight** | Structural | Game Bullets | Massive memory savings via sharing |


---

## 🎙️ Interview Tip
When asked about **Proxy vs Decorator**, remember:
- **Proxy** is about *controlling* access to the original object.
- **Decorator** is about *adding* new responsibilities/behaviors to the original object.
