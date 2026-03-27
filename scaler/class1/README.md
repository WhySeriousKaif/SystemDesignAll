# Object-Oriented Design Evolution: The Bird Example

This document explains the evolution of an object-oriented design using a classic example of designing `Bird` classes. We will go through step-by-step code snippets, highlighting the problems with each approach and how the subsequent approach solves them.

## Step 1: The Monolithic / If-Else Approach

```java
class Bird {
    String type;
    int age;
    double height;
    double weight;
    
    Bird(String type, int age, double height, double weight) {
        this.type = type;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    void fly() {
        if(type.equals("Hen")) {
            System.out.println("Hen fly low");
        } else if(type.equals("Eagle")) {
            System.out.println("Eagle fly high");
        } else {
            System.out.println(type + " fly");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Bird hen = new Bird("Hen", 2, 10.0, 5.0);
        hen.fly(); // Outputs: "Hen fly low"
        
        Bird eagle = new Bird("Eagle", 3, 20.0, 10.0);
        eagle.fly(); // Outputs: "Eagle fly high"
    }
}
```

**Explanation:** 
While this design is not fundamentally incorrect, it is not scalable. If we only have a few types of birds, this approach might suffice. However, if we need to add many different types of birds, the `if-else` block becomes massive and violates good design principles. To solve this, we can use **Inheritance**.

---

## Step 2: Using Inheritance

```java
class Bird1 {
    int age;
    double height;
    double weight;

    Bird1(int age, double height, double weight) {
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    void fly() {
        System.out.println("Bird fly");
    }
}

class Hen extends Bird1 {
    Hen(int age, double height, double weight) {
        super(age, height, weight);
    }
    void fly() {
        System.out.println("Hen fly low");
    }       
}

class Eagle extends Bird1 {
    Eagle(int age, double height, double weight) {
        super(age, height, weight);
    }
    void fly() {
        System.out.println("Eagle fly high");
    }
}

public class Main {
    public static void main(String[] args) {
        Hen hen = new Hen(2, 15.0, 4.0);
        hen.fly(); // Outputs: "Hen fly low"

        Eagle eagle = new Eagle(3, 30.0, 8.0);
        eagle.fly(); // Outputs: "Eagle fly high"
    }
}
/*
Steps
Hen h = new Hen(2, 30, 4);
1. Memory Allocation -> JVM allocates memory for Hen object on basis of the attibutes of Hen class.
2. Constructor Call ->  Hen() constructor is called.
3. 	super(...) is called -> Bird1() ie parent constructor is called.
here it calls Bird1(2, 30, 4)
These variables belong to Bird1, so Bird1 constructor must run first
4.	Parent constructor executes -> Parent variables get initialized.
this.age = age
this.height = height
this.weight = weight
5.	Control returns to child constructor
	•	Hen constructor finishes execution.
6.	Object becomes ready


*/
```
![Project](Screenshot%202026-03-11%20at%2011.27.30%E2%80%AFPM.png) 
Method call is decided at runtime based on the actual object type, not the reference type.


**Explanation:**
By utilizing inheritance, we gain several advantages:
*   **Maintainability:** If we have to add a new type of bird, we just add a new class extending `Bird1` and override the `fly()` method. We do not need to modify existing classes (unlike the if-else mapping).
*   **Readability:** The code logic is isolated in separate classes rather than a monolithic structure.
*   **Flexibility:** We can add new features and behaviors selectively to specific subclases.

---

## Step 3: Abstract Classes (Preventing the "Silent Killer")

**The Problem:** Not all birds can fly. Additionally, what if someone creates a new class `FlyingBird extends Bird1` but forgets to override `fly()`? It won't throw a compile-time error—this is a "Silent Killer" leading to unintended default behavior.

```java
// THE SILENT KILLER: Forgetting to override fly()
class FlyingBird extends Bird1 {
    FlyingBird(int age, double height, double weight) {
        super(age, height, weight);
    }
    // Oops! I forgot to override fly()!
    // It will use Bird1's default implementation: System.out.println("Bird fly");
}

/* 
   Inside Main:
   Even if we use a FlyingBird reference, it still calls the 
   wrong method because it wasn't overridden.
*/
public class Main {
    public static void main(String[] args) {
        Bird1 hen = new FlyingBird(2, 10, 5);
        hen.fly(); // Outputs: "Bird fly" (Silent Killer!)
    }
}
```


```java
abstract class Bird2 {
    int age;
    double height;
    double weight;

    Bird2(int age, double height, double weight) {
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    abstract void fly();
}

class Hen2 extends Bird2 {
    Hen2(int age, double height, double weight) {
        super(age, height, weight);
    }
    void fly() {
        System.out.println("Hen fly low");
    }
}

class Eagle2 extends Bird2 {
    Eagle2(int age, double height, double weight) {
        super(age, height, weight);
    }
    void fly() {
        System.out.println("Eagle fly high");
    }
}

public class Main {
    public static void main(String[] args) {
        // Bird2 b = new Bird2(2, 10, 5); // ❌ Error: Cannot instantiate abstract class
        
        Bird2 hen = new Hen2(1, 12, 3);
        hen.fly(); // Outputs: "Hen fly low"
        
        Bird2 eagle = new Eagle2(5, 40, 15);
        eagle.fly(); // Outputs: "Eagle fly high"
    }
}
```

**Explanation:**
Making the base class `Bird2` and the `fly()` method abstract ensures that any concrete subclass must provide an implementation for `fly()`. This enforces contracts and guarantees safety from unhandled default behaviors.

---

## Step 4: Interfaces (Solving the Diamond Problem & Multiple Behaviors)

Java does not support multiple class inheritance due to the **Diamond Problem** (confusion over which parent's implementation to inherit if both have the same method signature).

```java
// HYPOTHETICAL: If Java supported multiple inheritance...
class Bird { 
   void breathe() { System.out.println("Bird breathing"); }
}

class FlyingBird extends Bird {
   @Override
   void breathe() { System.out.println("FlyingBird breathing"); }
   void fly() { System.out.println("Flying..."); }
}

class NonFlyingBird extends Bird {
   @Override
   void breathe() { System.out.println("NonFlyingBird breathing"); }
}

// ROADBLOCK: Diamond Problem
// Which breathe() should Penguin inherit? FlyingBird's or NonFlyingBird's?
// class Penguin extends FlyingBird, NonFlyingBird { ... } 

/* 
   Inside Hypothetical Main:
   Penguin p = new Penguin();
   p.breathe(); // ERROR: Ambiguous call! 
*/
```

**The Roadblock with Behaviors:**
If we try to categorize birds based on what they *can* do (Fly vs No-Fly) using classes, we hit a wall when a bird needs multiple behaviors that don't fit a single hierarchy (e.g., a Penguin that can `swim()` but not `fly()`, but both are still `Birds`).


**Solution:** We use **Interfaces** to represent modular behaviors.

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

class Eagle3 extends Bird2 implements Flyable {
    Eagle3(int age, double height, double weight) {
        super(age, height, weight);
    }
    public void fly() {
        System.out.println("Eagle fly high");
    }
}

class Penguin3 extends Bird2 implements Swimmable {
    Penguin3(int age, double height, double weight) {
        super(age, height, weight);
    }
    public void swim() {
        System.out.println("Penguin swim");
    }
}

public class Main {
    public static void main(String[] args) {
        Eagle3 eagle = new Eagle3(5, 40, 15);
        eagle.fly();
        
        Penguin3 penguin = new Penguin3(3, 20, 10);
        penguin.swim();
        
        // Swimmable s = new Penguin3(...); // Interfaces allow polymorphic behavior too!
    }
}
```

**Explanation:**
Interfaces allow a class to inherit multiple behaviors (e.g., `implements Flyable, Swimmable`) without the ambiguity of the Diamond Problem.

---

## Step 5: Polymorphism

### Runtime Polymorphism (Duck Typing and Interfaces)

Client code doesn't need to know the specific implementation details of the objects it uses, as long as it adheres to an interface.

```java
class Client {
    void render(Flyable b) {
        b.fly(); // Dynamic dispatch determines the type at runtime
    }
}
// render(Flyable b) accepts any object that implements Flyable.
//So in main():
// 	•	We create objects like Eagle, Hen
// 	•	We pass them to render()
// 	•	At runtime, Java decides which fly() method to execute

// This is called Runtime Polymorphism (Dynamic Dispatch).


```
```java
public class Main {
    public static void main(String[] args) {

        Client client = new Client();

        Flyable eagle = new Eagle();
        Flyable hen = new Hen();

        client.render(eagle);

        
        //inside render() b now refers to Hen object.
        client.render(hen);

        //❌ Client does NOT check type
        //❌ No if-else
        //❌ No instanceof
        //	•	At runtime, JVM decides which fly() to execute


    }
}
//	•	Reference type = Flyable
	// •	Object type = Eagle
    //Java uses object type at runtime to decide method execution.
    /*
    •	render(Flyable b) accepts interface type
	•	We pass objects that implement Flyable
	•	At runtime, JVM decides which fly() to execute
	•	This is called Dynamic Method Dispatch
	•	Achieves runtime polymorphism
    //if pass for penguin --> 👉 Compile-time error.
    	•	render() expects a Flyable
        •	Penguin is NOT a Flyable
        Required type: Flyable
        Provided: Penguin
    
    A subclass should be replaceable for its parent without breaking behavior.

But here:
	•	Client assumes: “If you’re Flyable, you can fly.”
	•	Penguin says: “I can’t fly 😭”


    */
```

*   **Abstraction:** The client hides implementation details. It only interacts with the overarching `Flyable` interface.
*   **Run-time Polymorphism:** Passing different objects that implement the interface achieves dynamic behavior at runtime.

### Compile-Time Polymorphism (Method Overloading)

Methods can share the same name within the same class as long as their parameters (type, count, or order) differ.

```java
int add(int a, int b) {
    return a + b;
}

int add(int a, int b, int c) {
    return a + b + c;
}
```

**Explanation:** 
This is called Method Overloading. The correct method to call is decided natively by the Java compiler based on the arguments provided (Compile-Time Polymorphism).

---

## Liskov Substitution Principle (LSP)

The Liskov Substitution Principle (LSP) states that objects of a superclass shall be replaceable with objects of its subclasses without breaking the application.

**Key Pointers:**
*   Every child object must be able to replace the object of the parent class.

- If a method expects a Parent,
you should be able to pass a Child,
and everything should still work correctly.
*   A child class can have extra properties or methods, but whatever methods are present in the parent must also be present and valid in the child class.
*   If you are enforced to implement a method in a class which should not be there (e.g., forcing a Penguin to implement `fly()`), you need to step back and rethink the design.
*   The object of a child class must replace the object of the parent class seamlessly. You cannot override a method just to throw an exception for functionality that doesn't apply to the child.

```java
class Bird {
    void eat() {
        System.out.println("Bird eats");
    }
}

class Eagle extends Bird {
    void eat() {
        System.out.println("Eagle eats meat");
    }
}
class Client{
    void feed(Bird b) {
        b.eat();
    }
}
public static void main(String[] args) {
    Client c=new Client();
    c.feed(new Eagle());
}
/*
✅ Works perfectly.
Eagle behaves like a Bird.

✔ Child replaces Parent safely.
*/
```