# Class 8: Approaching Low-Level Design (LLD)

This class completely shifts focus away from isolated, pre-defined design patterns (like Strategy or Decorator) and moves into the foundational methodology required for raw **Low-Level Design (Machine Coding)** interviews. 

To teach this methodology, the instructor utilized the classic, purposefully ambiguous interview prompt: **"Design a Pen."**

## 🧠 The Core Philosophy: "Design a Pen"

When an interviewer says "Design a [Physical Object]," junior engineers immediately rush to the whiteboard and start drawing classes with fields like `color`, `length`, `plasticType`, and `weight`. 

The instructor explicitly warned against this trap.

### 1. Requirement Gathering > Architecture
The **#1 Rule of LLD** is: *Never start drawing boxes until you know how the client software intends to use the object.*

Before designing the architecture, you must relentlessly ask clarifying questions. What software is this pen for? A 3D CAD modeling program? A virtual classroom? An inventory management system? The architecture for a "Pen" in a physics simulation game is radically different from a "Pen" in a stationery store checkout app.

### 2. Software APIs > Physical Attributes
The instructor forced a massive mindset shift: **We do not care about physical properties unless the software requires them.**
If a pen is made of metal, does the software need a `.melt()` or `.conductElectricity()` method? If not, the metal composition is irrelevant to the architecture.

We must think strictly in terms of **Software APIs**: What state does the pen hold, and what behaviors (methods) must it expose to the system using it?

---

## 📋 Extracting the Requirements

By questioning the theoretical interviewer, the instructor extracted the following concrete system requirements for this specific software simulation:

1.  **Object Creation**: `Pen` objects should be created systematically, likely through a Factory pattern (e.g., specifying type, ink color, and cap vs. click mechanism at creation).
2.  **State Management**: 
    - A pen must track whether it is open or closed (cap removed, clicked open).
    - It must definitively track its `inkLevel`.
3.  **Behavioral Contracts (Methods)**:
    - The API must support: `start()`, `write()`, `close()`, and `refill()`.
4.  **Polymorphism**: 
    - The writing behavior changes based on the subclass. A `GelPen`, `BallpointPen`, and `FountainPen` will have different internal execution logic when `.write()` is called.
5.  **Runtime Validations (Error Handling)**:
    - The `write()` method must fail/throw an exception if `inkLevel <= 0`.
    - It cannot `.write()` if it hasn't been `start()`-ed (e.g., cap is still on).
    - Writing must accurately reduce the `inkLevel` state.

---

## 🔲 The Role of Class Diagrams (UML)

Once the raw software APIs (`start()`, `write()`, `refill()`, tracking ink state) and the polymorphic variations (Gel, Ballpoint, Fountain) are hammered out, *only then* does the architectural design phase begin.

The instructor emphasized that standardizing this communication is crucial. In LLD interviews, translating these requirements into cohesive **Class Diagrams**—identifying interfaces, abstract classes, composition (Has-A), and inheritance (Is-A)—is the ultimate goal before writing a single line of code.

> [!NOTE]
> **Implementation Exercise**: The instructor did not write the code for this system. The entire exercise was designed to teach the *process of requirement extraction* and the translation of ambiguous physical requests into concrete software APIs. The implementation itself was left as a challenging exercise utilizing interfaces, abstract bases, and the Factory pattern.
