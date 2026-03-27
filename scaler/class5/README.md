# Class 5: Advanced Design Patterns

This session covers powerful **Creational** and **Structural** design patterns: **Prototype, Proxy, and Flyweight**.

---

## 🏗️ Creational Patterns: Prototype
-->object creation of objects which are having heavy constructor

### 🛑 The Problem: Heavy Object Creation
Imagine a class with a **heavy constructor** (e.g., it performs complex math, hits an API, or parses a huge file).

```java
class HeavyConstClass {
    int a, b;
    double c;
    int t;

    public HeavyConstClass(int a, int b, double c, int t) {
        // Assume this constructor takes 2 seconds to run
        this.a = a;
        this.b = b;
        this.c = c;
        this.t = t;
    }
}
```

If we need 100 objects where `a, b, c` are identical and only `t` changes, calling `new HeavyConstClass(...)` 100 times is a massive waste of time and resources.

### 🧩 The Solution: Prototype Pattern (The T-Shirt Story)
Instead of creating a new object from scratch, we **clone** an existing one. 

#### The Scenario: Brand1 Mass Production
Imagine "Brand1" produces three models: **T1, T2, and T3**.
- Each model has **heavy computations** in its constructor (calculating fabric quality, thread count, global brand standards).
- There are thousands of shirts for each model.
- Only the `size` and `color` change for each individual shirt.

**The Strategy**: 
1. Create **one master prototype** for each type (`T1`, `T2`, `T3`). The heavy constructor runs only **once** per type.
2. For every new shirt, simply **clone** the corresponding prototype.
3. Use setters to tweak only the varying attributes (`size`, `color`).

#### 💻 Complete Implementation
```java
// 1. Prototype Class
class Tshirt implements Cloneable {
    String brand, type, size, color;
    int threadCount;

    // Heavy Constructor (Called only for Prototyping)
    public Tshirt(String brand, String type, int threadCount) {
        System.out.println(">>> [HEAVY] Calculating branding details for: " + type);
        this.brand = brand;
        this.type = type;
        this.threadCount = threadCount;
    }

    @Override
    public Tshirt clone() throws CloneNotSupportedException {
        // bit-wise copy: Reserves memory and copies values without re-running constructor
        return (Tshirt) super.clone();
    }

    public void setSize(String size) { this.size = size; }
    public void setColor(String color) { this.color = color; }

    public void display() {
        System.out.println(String.format("[%s %s] Color: %s, Size: %s, Thread: %d", brand, type, color, size, threadCount));
    }
}

// 2. Execution Logic
public class PrototypeDemo {
    public static void main(String[] args) throws CloneNotSupportedException {
        // 1. Create prototypes (Heavy work done once)
        Tshirt t1Prototype = new Tshirt("Brand1", "T1", 400);

        // 2. Create mass units by cloning (Instantaneous)
        Tshirt shirt1 = t1Prototype.clone();
        shirt1.setSize("M");
        shirt1.setColor("Blue");

        Tshirt shirt2 = t1Prototype.clone();
        shirt2.setSize("XL");
        shirt2.setColor("Red");

        shirt1.display();
        shirt2.display();
    }
}
```

#### 🌟 Key Advantages (Interviewer's Focus)
1.  **Avoids Constructor Overload**: Drastically reduces overhead—the heavy computation is done once for each type (T1, T2, T3) instead of thousands of times.
2.  **Efficient Scaling**: Creating 1,000,000 shirts becomes nearly instantaneous because cloning is a low-level binary copy.
3.  **Segregation**: The client code doesn't need to know the complex "recipe" for a `T1 Shirt`; it just asks the master prototype for a copy.

---

## 🛡️ Structural Patterns: Proxy Pattern

The Proxy pattern acts as a **middleman** between the Client and the Real Object. It controls access, adds security, or handles expensive operations.

### 🌉 The Proxy Story: Resource Optimization (Before vs After)

The Proxy pattern is most effective when managing **heavy resources** that might not always be used.

#### 🏗️ The Setup: Interfaces & Heavy Object
```java
interface ITextParser { int getWordCount(); }
interface ITextProcessor { void f(); }

class BookParser implements ITextParser {
    public BookParser() {
        // HEAVY CONSTRUCTOR: Network calls, Database parsing, etc.
        System.out.println(">>> [CRITICAL] Running Heavy BookParser Initialization (2 Seconds)...");
    }
    public int getWordCount() { return 1000; }
}

class TextProcessor implements ITextProcessor {
    private ITextParser tp;
    public TextProcessor(ITextParser tp) { this.tp = tp; }
    public void f() { tp.getWordCount(); }
}
```

---

#### 🛑 Stage 1: Before (The Problem Scenario)
In this scenario, we create the `BookParser` directly and inject it into the `TextProcessor`.

```java
interface ITextParser { int getWordCount(); }

class BookParser implements ITextParser {
    public BookParser() {
        // HEAVY CONSTRUCTOR: Network calls, Database parsing, etc.
        System.out.println(">>> [CRITICAL] Running Heavy BookParser Initialization (2 Seconds)...");
    }
    public int getWordCount() { return 1000; }
}

class TextProcessor {
    private ITextParser tp;
    public TextProcessor(ITextParser tp) { this.tp = tp; }
    public void f() { tp.getWordCount(); }
}

public class ProxyBeforeDemo {
    public static void main(String[] args) {
        // PROBLEM: The BookParser is created HERE, even if its methods are never called.
        System.out.println("Initializing TextProcessor...");
        TextProcessor processor = new TextProcessor(new BookParser());
        
        System.out.println("TextProcessor ready.");
        // If we never call processor.f(), the 2-second heavy initialization was a waste!
    }
}
```

**The Issues**:
1.  **Wasteful Creation**: The `BookParser` object is created when the `Client` is instantiated, regardless of whether `clientMethod1` or `clientMethod2` are ever called.
2.  **Resource Drain**: Even if the conditions are always `false`, the heavy constructor has already executed, wasting RAM and CPU.

---

#### ✨ Stage 2: After (The Proxy Solution)
We introduce a **Proxy** to defer the heavy creation until it's actually needed.

```java
// THE PROXY: Implements same interface, but holds a reference to the real object
class BookParserProxy implements ITextParser {
    private BookParser realParser; // Delayed creation

    public int getWordCount() {
        if (realParser == null) {
            // DEFERRED: Actual creation happens only on FIRST call
            realParser = new BookParser();
        }
        return realParser.getWordCount();
    }
}

public class ProxyAfterDemo {
    public static void main(String[] args) {
        System.out.println("Initializing TextProcessor with Proxy...");
        // SOLUTION: The Proxy is created instantly (zero cost)
        TextProcessor processor = new TextProcessor(new BookParserProxy());
        
        System.out.println("TextProcessor ready. (Notice: No heavy loading yet!)");
        
        System.out.println("\nExecuting f() for the first time:");
        processor.f(); // Heavy loading happens only now!
    }
}
```

**The Benefits**:
1.  **Lazy Loading**: The heavy object is created **only if** one of the conditional calls actually occurs.
2.  **Zero Client Changes**: The `Client` still works with `ITextProcessor`, unaware a Proxy middleman is saving resources.
3.  **Encapsulation**: The logic for checking "is the object created?" is hidden inside the Proxy.

---

#### ✨ Stage 3: Direct Proxy Injection (Optimized After Situation)

In this final optimization, we modify the `TextProcessor` to specifically accept the `BookParserProxy`. This guarantees that the heavy creation is deferred until the first actual use inside the processor, while maintaining a clean, injection-based design.

**The Strategy**:
1. `TextProcessor` now specifically accepts a `BookParserProxy` in its constructor.
2. The `Client` creates the proxy and injects it.
3. The heavy `BookParser` is created **only** when `getWordCount()` is called inside `TextProcessor.f()`.

#### 💻 Complete Runnable Code
```java
// 1. Original ITextParser interface
interface ITextParser {
    int getWordCount();
}

// 2. Concrete implementation (Heavy)
class BookParser implements ITextParser {
    public BookParser() {
        System.out.println(">>> [CRITICAL] Running Heavy BookParser Initialization (2 Seconds)...");
    }

    public int getWordCount() {
        return 1000;
    }
}

// 3. Proxy class (Middleman)
class BookParserProxy implements ITextParser {
    private BookParser realParser;

    public int getWordCount() {
        if (realParser == null) {
            // Lazy loading happens here
            realParser = new BookParser();
        }
        return realParser.getWordCount();
    }
}

// 4. Modified TextProcessor (Accepts Proxy directly)
class TextProcessorOptimized {
    private BookParserProxy parserProxy;

    public TextProcessorOptimized(BookParserProxy parserProxy) {
        this.parserProxy = parserProxy;
    }

    public void f() {
        // This will only trigger the real creation when getWordCount is called
        System.out.println("Executing f()...");
        int count = parserProxy.getWordCount();
        System.out.println("Word Count: " + count);
    }
}

// 5. Usage Demo
public class ProxyOptimizationDemo {
    public static void main(String[] args) {
        System.out.println("--- Scenario: Client creates TextProcessor ---");
        // Inject the proxy instantly (zero cost)
        TextProcessorOptimized tp = new TextProcessorOptimized(new BookParserProxy());
        
        System.out.println("TextProcessor ready. (Notice: No heavy object created yet)");

        // Assume some conditional logic here
        boolean someCondition = true; 
        if (someCondition) {
            System.out.println("\nCondition met! Calling f()...");
            tp.f(); // Heavy object created ONLY now
        }
    }
}
```

**Why this matters**:
- **Guaranteed Lazy Loading**: By using the Proxy in the constructor, we explicitly signal that we want deferred initialization.
- **Structural Integrity**: It keeps the proxy logic (the "middleman" checks) separate from the core `TextProcessor` logic.
- **Adherence to Principles**: We are still using Dependency Injection, but with an added layer of resource management that prevents wasteful creation if `f()` is never called.

---

### 1. Remote Proxy: Retry Logic (GeoLocator Story)
A proxy can check permissions, add logs, or handle retries before calling a real (possibly unreliable) API.

```java
interface GeoService { void locate(); }

class GeoLocate implements GeoService {
    public void locate() { 
        System.out.println("Calling Government Map API...");
        // Might throw timeout simulation
        throw new RuntimeException("Timeout!");
    }
}

class GeoProxy implements GeoService {
    private GeoLocate realService = new GeoLocate();

    public void locate() {
        System.out.println("LOG: Checking client permissions and initializing retry logic...");
        int attempts = 0;
        while (attempts < 3) {
            try {
                realService.locate();
                return;
            } catch (Exception e) {
                attempts++;
                System.out.println("Attempt " + attempts + " failed. Retrying...");
            }
        }
        System.out.println("All retry attempts failed.");
    }
}

public class GeoProxyDemo {
    public static void main(String[] args) {
        GeoService service = new GeoProxy();
        service.locate();
    }
}
```

### 2. Access Control & Lazy Loading Proxy (WordDoc Story)

In this refined scenario, we combine **Access Control** (Security) and **Lazy Loading** (Resource Optimization). The `WordDocProxy` ensures that the heavy `WordDoc` is only created if authorized and actually needed.

**The Strategy**:
1. `DocumentProcessor` specifically accepts a `WordDocProxy`.
2. The `Proxy` manages different access rights for **Employee**, **Student**, and **Mentor**.
3. The `WordDoc` itself is only created upon the first successful authorized call.

#### 💻 Complete Runnable Code
```java
// 1. User Role Definitions
class User {}
class Employee extends User {}
class Student extends User {}
class Mentor extends User {}

class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) { super(message); }
}

// 2. Document interface
interface IDocument {
    void read(User user);
    void write(User user);
    void rename(User user);
}

// 3. Concrete Implementation (Heavy)
class WordDoc implements IDocument {
    public WordDoc() {
        System.out.println(">>> [HEAVY] Initializing Real WordDoc object...");
    }
    public void read(User user) { System.out.println("Reading Word document"); }
    public void write(User user) { System.out.println("Writing to Word document"); }
    public void rename(User user) { System.out.println("Renaming Word document"); }
}

// 4. WordDoc Proxy (Security + Lazy Loading)
class WordDocProxy implements IDocument {
    private WordDoc realDoc;

    public void read(User user) {
        // Everyone can read
        getRealDoc().read(user);
    }

    public void write(User user) {
        if (user instanceof Employee || user instanceof Student) {
            getRealDoc().write(user);
        } else {
            throw new AccessDeniedException("Write access denied");
        }
    }

    public void rename(User user) {
        if (user instanceof Employee || user instanceof Student) {
            getRealDoc().rename(user);
        } else {
            throw new AccessDeniedException("Rename access denied");
        }
    }

    private WordDoc getRealDoc() {
        if (realDoc == null) {
            // Lazy loading happens here
            realDoc = new WordDoc();
        }
        return realDoc;
    }
}

// 5. Document Processor (Accepts Proxy Directly)
class DocumentProcessor {
    private WordDocProxy docProxy;

    public DocumentProcessor(WordDocProxy docProxy) {
        this.docProxy = docProxy;
    }

    public void processDocument(User user) {
        try {
            System.out.println("Processing for: " + user.getClass().getSimpleName());
            docProxy.read(user);
            docProxy.write(user);
            docProxy.rename(user);
        } catch (AccessDeniedException e) {
            System.out.println(">>> Access Error: " + e.getMessage());
        }
    }
}

// 6. Usage Demo
public class WordProxyDemo {
    public static void main(String[] args) {
        DocumentProcessor processor = new DocumentProcessor(new WordDocProxy());

        System.out.println("--- Scenario: Employee ---");
        processor.processDocument(new Employee()); // Full Success

        System.out.println("\n--- Scenario: Mentor ---");
        processor.processDocument(new Mentor());   // Denied for Write/Rename
    }
}
```

**Key Improvements**:
- **Consolidated Logic**: The proxy now enforces roles *and* prevents unnecessary memory allocation.
- **Architectural Clarity**: By injecting the proxy, we stay within the Dependency Injection paradigm while explicitly utilizing the Proxy’s specialized behaviors.
- **Scalability**: New document types or user roles can be added by updating the Proxy, without ever touching the core `WordDoc` logic (preserving the Open/Closed Principle).

---

### 3. Lazy Loading Proxy (BookParser Story)
If an object is very heavy (e.g., parsing a 1000-page book), the Proxy waits until the client *actually* asks for data before creating the real object.

```java
class BookParserProxy implements ITextParser {
    private BookParser realParser; // Only created if needed

    public int getWordCount() {
        if (realParser == null) {
            realParser = new BookParser(); // Deferred creation
        }
        return realParser.getWordCount();
    }
}
```

---

### 🌌 The Versatility of the Proxy Pattern

As discussed in the session (around 1:20:00), the Proxy pattern is highly versatile and solves multiple architectural problems by acting as a specialized middleman.

| Proxy Type | Purpose | Goal |
| :--- | :--- | :--- |
| **Virtual Proxy** | Lazy Initialization | Delays creation of expensive objects until strictly needed (e.g., `BookParser`). |
| **Protection Proxy** | Access Control | Checks permissions/rights before allowed access (e.g., `WordDocWrapper`). |
| **Remote Proxy** | Remote Resource Access | Bridges communication with objects in different address spaces or machines (e.g., `GeoLocator`). |
| **Smart Proxy** | Logging & Caching | Adds functionality like tracking usage or caching results without modifying the real object. |
| **Reliability Proxy** | Error Handling | Implements retry logic and timeout management for unreliable services (e.g., `GeoProxy`). |

**Key Takeaway**: The Proxy pattern allows us to **add behavior without modifying existing code**, perfectly adhering to the **Open/Closed Principle**. It is your "Swiss Army Knife" for controlling access and managing resources.

---

## 🪶 Structural Patterns: Flyweight Pattern

### 🛑 The Memory Problem (The Game Story)
Imagine you are building a game like **BGMI**, **COD**, or **FIFA**.
1.  **The Interaction**: Your character needs to navigate a world filled with millions of objects—stones, trees, houses, and bullets.
2.  **The Attributes**: Every single stone object has coordinates (`x, y`), `speed`, and a high-resolution `image`.
3.  **The Cost**: A single stone object takes about **20-21 KB** of memory (most of which is the 19KB image data).

#### 💥 The "Billion Stone" Crash
Imagine there are **1,000,000,000 (1 Billion)** stones on the map.
-   **Math**: 21 KB $\times$ 1,000,000,000 $\approx$ **20 TB of RAM!** 
-   **Result**: Even with a 1,000,000 stone scenario, you'd need **20 GB RAM** just for stones. The game would instantly crash on almost any consumer device.

### 🧩 The Solution: The "Replica" Insight
As the instructor observed, despite the apparent variety in massive games, most objects are actually **Replicas**.
-   **FIFA**: There are thousands of spectators in the audience. They aren't 50,000 unique humans; they are a dozen unique models repeated thousands of times.
-   **FPS Games**: 10,000 trees on a mountain are usually just **10-20 unique tree models** repositioned and rotated.

### 🧩 Sharing State: Intrinsic vs Extrinsic
To make this work, we split the object’s data into two categories:

1.  **Intrinsic State (Shared)**: Fixed data that is the same for all objects of a type.
    -   *Example*: The high-res Image data of a stone.
2.  **Extrinsic State (Unique)**: Dynamic attributes that change for every single instance.
    -   *Example*: The `x, y` position, `rotation`, or `speed`.

**The Goal**: Create separate classes for shared data and object instances, using references to shared data rather than duplicating it.

#### 🪶 Efficiency Implementation
```java
import java.util.HashMap;
import java.util.Map;

// 1. Shared State (Intrinsic / Flyweight)
// This object is heavy, but we only create it ONCE per type.
class StoneImage {
    private String data = "19KB_OF_IMAGE_DATA"; // Large object
    private String name;

    public StoneImage(String name) { this.name = name; }
    public String getName() { return name; }
}

// 2. Flyweight Factory
// Manages the shared pool of replicas.
class StoneFactory {
    private static Map<String, StoneImage> cache = new HashMap<>();

    public static StoneImage getImage(String name) {
        if (!cache.containsKey(name)) {
            System.out.println(">>> [LOG] Loading Image from Disk: " + name);
            cache.put(name, new StoneImage(name));
        }
        return cache.get(name);
    }
}

// 3. Object with Shared Data (Extrinsic State)
class Stone {
    int x, y;             // Extrinsic (Unique position)
    int speed;            // Extrinsic (Unique speed)
    StoneImage image;     // Intrinsic (Shared Reference)

    public Stone(int x, int y, int speed, StoneImage img) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.image = img;
    }
}

public class FlyweightDemo {
    public static void main(String[] args) {
        System.out.println("--- Scenario: Rendering 1,000,000 Stones ---");
        
        // We only load the image ONCE
        StoneImage sharedImg = StoneFactory.getImage("SharpStone");

        // We can now create millions of stones with almost zero memory footprint
        Stone s1 = new Stone(10, 20, 5, sharedImg);
        Stone s2 = new Stone(100, 200, 2, sharedImg);

        System.out.println("\nVerifying Memory Sharing:");
        System.out.println("Stone 1 Image ID: " + s1.image.hashCode());
        System.out.println("Stone 2 Image ID: " + s2.image.hashCode());
        System.out.println("Are they sharing the 19KB image? " + (s1.image == s2.image));
        
        System.out.println("\nMemory Saved: (Millions of objects * 19KB) - 19KB");
    }
}
```

---

## 🎯 Summary Table

| Pattern | Type | Real-World Story | Core Benefit |
| :--- | :--- | :--- | :--- |
| **Prototype** | Creational | T-Shirt Cloning | Fast object creation from heavy masters |
| **Proxy** | Structural | Access Card/Middleman | Security, Lazy Loading, Remote Access |
| **Flyweight** | Structural | Game Assets (Stones/Trees) | Massive memory savings via sharing |

---

## 🎙️ Interview Tip
When asked about **Proxy vs Decorator**, remember:
- **Proxy** is about *controlling* access to the original object.
- **Decorator** is about *adding* new responsibilities/behaviors to the original object.
