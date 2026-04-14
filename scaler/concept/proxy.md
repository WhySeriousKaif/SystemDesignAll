# 🛡️ Proxy Design Pattern

**Intent:** Provide a surrogate or placeholder for another object to control access to it.
**Summary:** The Proxy acts as a "Middleman". It implements the exact same interface as the real object, allowing clients to interact with it seamlessly, while the Proxy handles "extra" work underneath (like lazy loading, security, caching, or network connections).

---

## 🛑 Problem: The Heavy Initialization Problem
Imagine an application that processes large text files using a `BookParser`. The constructor of `BookParser` is incredibly heavy (e.g., establishing DB connections, loading a massive string into memory).

```java
interface ITextParser { int getWordCount(); }

class BookParser implements ITextParser {
    public BookParser() {
        System.out.println(">>> [HEAVY] Loading 2GB book into memory...");
        // 2 Seconds delay
    }
    public int getWordCount() { return 50000; }
}

class TextProcessor {
    private ITextParser parser;
    public TextProcessor(ITextParser parser) { this.parser = parser; }
    
    public void executeLogic(boolean userClickedCount) {
        System.out.println("UI Loaded.");
        if (userClickedCount) {
            System.out.println("Count is: " + parser.getWordCount());
        }
    }
}
```

### 🚩 The Wasteful Execution
If the client injects `new BookParser()` into the `TextProcessor`, the 2GB book is loaded immediately during initialization. **But what if `userClickedCount` is false?** We just wasted 2GB of RAM and 2 seconds of loading time for a feature the user didn't even use!

---

## 🛠️ Solution: The Virtual Proxy (Lazy Initialization)
We create a `BookParserProxy` that implements the same interface but **delays** creating the real object until its methods are actually invoked.

```java
class BookParserProxy implements ITextParser {
    private BookParser realParser; // Kept null initially

    @Override
    public int getWordCount() {
        // LAZY LOADING: Create the heavy object only on the very first call
        if (realParser == null) {
            realParser = new BookParser();
        }
        return realParser.getWordCount();
    }
}
```

### 💻 The Refactored Client
```java
public class Main {
    public static void main(String[] args) {
        // [INSTANT] Zero delay, 0 RAM used. The proxy is incredibly lightweight.
        ITextParser proxy = new BookParserProxy(); 
        TextProcessor ui = new TextProcessor(proxy);

        ui.executeLogic(false); // Book is NEVER loaded! Massive optimalization.
        
        // If user DOES click:
        ui.executeLogic(true); // >> [HEAVY] Loading 2GB book... -> Count is 50000.
    }
}
```

---

## 🖼️ Everyday Example: Image Placeholder (Virtual Proxy)
When a webpage loads a heavy image, it often shows a grey box or a low-res preview while the real image downloads in the background.

```java
interface Graphic { void draw(); }

class HighResImage implements Graphic {
    public HighResImage(String url) { System.out.println("Downloading massive image from " + url); }
    public void draw() { System.out.println("Drawing crystal clear image"); }
}

class ImageProxy implements Graphic {
    private String url;
    private HighResImage realImage;

    public ImageProxy(String url) { this.url = url; }

    @Override
    public void draw() {
        if (realImage == null) {
            System.out.println("Drawing placeholder grey box...");
            realImage = new HighResImage(url); // Starts download
        }
        realImage.draw();
    }
}
```

---

## 🌠 Types of Proxies

1. **Virtual Proxy (Lazy Initialization)**: Delays creation of expensive objects (e.g., `BookParser`, `ImageProxy`).
2. **Protection Proxy (Access Control)**: Checks if the user has permission before forwarding the request to the real object (e.g., A proxy that checks if a user is `Admin` before allowing a `delete()` call).
3. **Remote Proxy**: The real object lives on a different server. The proxy handles the network communication (e.g., gRPC stubs, RMI).
4. **Caching / Smart Proxy**: Stores the result of expensive operations so subsequent requests don't hit the real object.

---

## 👑 Relationship with SOLID Principles
*   **Single Responsibility Principle (SRP)**: The real object focuses entirely on its core business logic (parsing a book). The proxy focuses entirely on lifecycle/access management.
*   **Open/Closed Principle (OCP)**: We added lazy loading and caching without modifying the original `BookParser` class. We just mapped a proxy on top of it.

---

## ⚔️ Proxy vs Other Patterns

| Pattern | Goal | Relationship |
| :--- | :--- | :--- |
| **Proxy** | Control access to an object. | Implements the SAME interface as the real object. |
| **Decorator** | Add responsibilities/features dynamically. | Implements the SAME interface, but is designed to be chained/stacked. |
| **Adapter** | Make incompatible classes work together. | Implements a DIFFERENT interface to act as a translator. |

---

## 🎙️ Frequently Asked Interview Questions (Viva)

#### Q1: What is the defining characteristic of a Proxy?
**Answer**: A Proxy holds a reference to a Real Object and implements the exact same interface as that Real Object. To the client, there is absolutely no difference between talking to the proxy and talking to the real object.

#### Q2: How does a Proxy implement "Lazy Loading"?
**Answer**: By deferring instantiation. The Proxy's constructor does not instantiate the heavy object; it simply stores the arguments needed. The heavy object is only instantiated with a `null` check inside the actual execution method (like `getWordCount()`) precisely when the client demands the data.

#### Q3: Difference between Proxy and Decorator? (Classic Interview Question)
**Answer**: A Proxy usually controls *access* (when and how the object is reached) and often manages the lifecycle (creation/destruction) of the real object internally. A Decorator *adds behavior* and is typically handed its real object by the client via its constructor, so decorators can be stacked indefinitely. Proxy focuses on "Protection/Delay", Decorator focuses on "Enhancement".

#### Q4: Why is Proxy heavily used in ORMs (like Hibernate)?
**Answer**: ORMs use Virtual Proxies to solve the "N+1 query problem" and infinite loops in relational mapping. If you load a `User`, Hibernate doesn't instantly query the database for all 50,000 of their `Posts`. Instead, it provides a Proxy list. The database is only actually queried when you call `.get(0)` or `.size()` on that Proxy list.

---
*Created for viva preparation using notes from Scaler LLD sessions.*
