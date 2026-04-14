# Class 6: Structural & Behavioral Patterns

This class heavily focuses on two crucial design patterns that solve deep architectural problems related to integrating heterogeneous systems and managing combinatorial explosions of behaviors.

---

## 🔌 Part 1: Adapter Design Pattern

**Intent:** Convert the interface of a class into another interface clients expect. Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.

### 🛑 The Problem: Merging Two E-commerce Systems
Imagine Snapdeal acquired another e-commerce platform called "Exclusively". Both systems have a service to search for merchants/sellers based on a product's SKU, but they were built by different teams using different method names.

**Snapdeal's Architecture (The Expectation)**
Snapdeal has a strict client class (`SellerRankingService`) that relies on an interface called `ISellerSearchService`.

```java
// The Target Interface
interface ISellerSearchService {
    List<Seller> getSellerBySKU(String sku);
}

// Snapdeal's Native Implementation
class SnapdealSellerSearchService implements ISellerSearchService {
    @Override
    public List<Seller> getSellerBySKU(String sku) {
        System.out.println("Searching Snapdeal Database...");
        return new ArrayList<>(); 
    }
}

// The Client (Strict dependency inversion applied)
class SellerRankingService {
    private ISellerSearchService searchService;
    
    public SellerRankingService(ISellerSearchService searchService) {
        this.searchService = searchService;
    }

    public void rankingFunc(String sku) {
        // Relies EXACTLY on the getSellerBySKU method signature
        List<Seller> list = searchService.getSellerBySKU(sku);
    }
}
```

**The New Requirement (The Incompatible Service)**
We now need to inject Exclusively's service into the `SellerRankingService`. However, Exclusively's class looks like this:

```java
// The Adaptee (Incompatible)
class ExclusivelyMerchantService { // Does NOT implement ISellerSearchService
    public List<Seller> getMerchantsBySKU(String sku) {
        System.out.println("Searching Exclusively Database...");
        return new ArrayList<>();
    }
}
```

**The Dilemma**: We cannot modify `SellerRankingService` (it's core routing logic). We shouldn't modify `ExclusivelyMerchantService` (it might be a frozen 3rd party SDK). How do they talk?

---

### 🧩 The Solution: The Wrapper Adapter
We create a new class (`ExclusivelyMerchantServiceAdapter`) that **Implements** the target interface, and **Composes** (wraps) the incompatible class.

```java
// The Adapter
public class ExclusivelyMerchantServiceAdapter implements ISellerSearchService {
    
    // Hard dependency on the alien class
    private ExclusivelyMerchantService merchantService;

    public ExclusivelyMerchantServiceAdapter(ExclusivelyMerchantService merchantService) {
        this.merchantService = merchantService;
    }

    // Fulfills the interface expected by the Client
    @Override
    public List<Seller> getSellerBySKU(String sku) {
        // Translates the call to the alien's method!
        return merchantService.getMerchantsBySKU(sku);
    }
}
```

#### 💻 Client Usage
```java
public class AdapterDemo {
    public static void main(String[] args) {
        // 1. Native usage works perfectly
        SellerRankingService r1 = new SellerRankingService(new SnapdealSellerSearchService());
        r1.rankingFunc("iphone-15");

        // 2. Adapted usage works perfectly!
        ExclusivelyMerchantService alienService = new ExclusivelyMerchantService();
        ISellerSearchService adapter = new ExclusivelyMerchantServiceAdapter(alienService);
        
        SellerRankingService r2 = new SellerRankingService(adapter);
        r2.rankingFunc("macbook-m3");
    }
}
```

---

### 🎙️ Frequently Asked Interview Questions (Viva)

#### Q1: Doesn't the Adapter violate Dependency Inversion by having a hard dependency on a concrete class (`ExclusivelyMerchantService`)?
**Answer**: Technically yes, but it is an intentional and localized violation. The wrapper acts as a bridge specifically built for that concrete third-party class. By isolating this violation inside the adapter, we protect the core `SellerRankingService` (which remains perfectly loosely coupled).

#### Q2: What is the defining difference between a Proxy, an Adapter, and a Decorator? (Classic Viva Trick)
**Answer**: 
*   **Adapter**: Implements a *different* interface to translate calls. Goal: Compatibility.
*   **Proxy**: Implements the *same* interface but intercepts calls. Goal: Protection/Lazy Loading.
*   **Decorator**: Implements the *same* interface but adds new behavior. Goal: Enhancement.

---
---

## ♟️ Part 2: Strategy Design Pattern

**Intent:** Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently from clients that use it.

### 🛑 The Problem: The Inheritance Explosion
We need to build a custom list class `MyList` that can:
1. Provide sorting behavior.
2. Provide printing behavior (e.g., vertical list vs array format).

**The Naive Approach (Inheritance)**
Client 1 needs a `VerticalList` that uses `CountSort` (because they only have 0s, 1s, and 2s).
Client 2 needs a `HorizontalList` that uses `TimSort` (for generic integers).

```java
// We build rigid subclasses
class VerticalListCountSort extends MyList { ... }
class HorizontalListTimSort extends MyList { ... }
```

**The Crisis**: Client 3 arrives. They want `HorizontalList` but with `CountSort` (binary data).
If we use inheritance, we are forced to create `HorizontalListCountSort`. As algorithms increase (QuickSort, MergeSort) and print formats increase (JSONPrint, XMLPrint), we face a massive **Combinatorial Explosion** of subclasses (`XMLPrintMergeSortList`, etc.).

---

### 🧩 The Solution: Composition over Inheritance (Strategy)
Instead of hardcoding "how to sort" or "how to print" inside the List class, we extract those behaviors into separate **Interfaces**.

#### 1. Define the Strategy Interfaces
```java
interface ISortAlgorithm {
    void sort(List<Integer> list);
}

interface IPrintAlgorithm {
    void print(List<Integer> list);
}
```

#### 2. Create Concrete Strategies
```java
// Sorting Strategies
class CountSort implements ISortAlgorithm {
    public void sort(List<Integer> list) { System.out.println("Sorting using CountSort (O(n))..."); }
}
class TimSort implements ISortAlgorithm {
    public void sort(List<Integer> list) { System.out.println("Sorting using TimSort (O(NlogN))..."); }
}

// Printing Strategies
class VerticalPrint implements IPrintAlgorithm {
    public void print(List<Integer> list) { System.out.println("Printing vertically \n1\n2\n3"); }
}
class JSONPrint implements IPrintAlgorithm {
    public void print(List<Integer> list) { System.out.println("Printing softly: [1, 2, 3]"); }
}
```

#### 3. Refactor The Core Class (Composition)
`MyList` now *has* strategies, rather than *being* a strategy.

```java
class MyList {
    private List<Integer> data;
    
    // Composition using Interfaces
    private ISortAlgorithm sortAlgorithm;
    private IPrintAlgorithm printAlgorithm;

    // Dependency Injection
    public MyList(List<Integer> data, ISortAlgorithm sortStrategy, IPrintAlgorithm printStrategy) {
        this.data = data;
        this.sortAlgorithm = sortStrategy;
        this.printAlgorithm = printStrategy;
    }

    // Setters allow changing behavior AT RUNTIME!
    public void setSortAlgorithm(ISortAlgorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }

    public void processData() {
        sortAlgorithm.sort(data);  // Delegate!
        printAlgorithm.print(data); // Delegate!
    }
}
```

#### 💻 Client Usage
```java
public class StrategyDemo {
    public static void main(String[] args) {
        List<Integer> data = Arrays.asList(3, 1, 2);

        // Fulfill Client 3's exact requirement dynamically
        MyList customList = new MyList(data, new CountSort(), new JSONPrint());
        customList.processData();

        // Requirements changed midway? Simply swap the strategy!
        System.out.println("Switching to TimSort at runtime...");
        customList.setSortAlgorithm(new TimSort());
        customList.processData();
    }
}
```

---

### 🎙️ Frequently Asked Interview Questions (Viva)

#### Q1: What specific SOLID principle does the Strategy pattern satisfy?
**Answer**: It perfectly satisfies the **Open-Closed Principle (OCP)**. If you need a new sorting algorithm like `RadixSort`, you create a new class implementing `ISortAlgorithm`. You do not touch the `MyList` class at all. It is closed for modification but open for extension.

#### Q2: How does Strategy solve the "Combinatorial Explosion" problem?
**Answer**: With inheritance, combining 5 sorting algorithms and 4 printing algorithms requires creating `5 * 4 = 20` tightly coupled subclasses. With the Strategy pattern via composition, you create exactly `5 + 4 = 9` independent classes, which can be mixed and matched freely by the client.

#### Q3: Where is the Strategy pattern used in modern real-world architecture?
**Answer**: 
- **Payment Gateways**: An `OrderedCart` holds an `IPaymentStrategy`. At checkout, you inject `CreditCardStrategy`, `UPIStrategy`, or `PayPalStrategy`.
- **Navigation (Google Maps)**: A `RouteCalculator` uses different strategies: `WalkingStrategy`, `DrivingStrategy`, or `PublicTransitStrategy`.

---
