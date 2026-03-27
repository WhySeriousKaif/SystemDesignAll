# 🏗️ Structural Design Patterns - Complete Guide

Structural design patterns explain how to assemble objects and classes into larger structures while keeping these structures flexible and efficient. They focus on how classes and objects are composed to form larger structures.

---

## 1. 🍕 Decorator Pattern
**Purpose**: Add functionality to existing objects **without changing their structure** (Runtime modification).

**Case Study**: Pizza Toppings
Avoid "Class Explosion" (e.g., creating `ThickCrustWithCheeseAndMushroomPizza`).

### 📊 UML Diagram
```mermaid
classDiagram
    class BasePizza {
        <<Abstract>>
        +cost()* int
    }
    class FarmhousePizza { +cost() int }
    class MargheritaPizza { +cost() int }
    class ToppingDecorator {
        <<Abstract>>
        #BasePizza basePizza
    }
    class ExtraCheese { +cost() int }
    class Mushroom { +cost() int }

    BasePizza <|-- FarmhousePizza
    BasePizza <|-- MargheritaPizza
    BasePizza <|-- ToppingDecorator
    ToppingDecorator <|-- ExtraCheese
    ToppingDecorator <|-- Mushroom
    ToppingDecorator o-- BasePizza : "has-a"
```

### 💻 Code Snippet
```java
abstract class BasePizza { public abstract int cost(); }

class MargheritaPizza extends BasePizza { public int cost() { return 100; } }

abstract class ToppingDecorator extends BasePizza {
    protected BasePizza basePizza;
    public ToppingDecorator(BasePizza pizza) { this.basePizza = pizza; }
}

class ExtraCheese extends ToppingDecorator {
    public ExtraCheese(BasePizza pizza) { super(pizza); }
    public int cost() { return basePizza.cost() + 10; }
}
```

---

## 2. 🛡️ Proxy Pattern
**Purpose**: Provide a placeholder for another object to control access (Validation, Logging, Lazy Init).

**Case Study**: Employee Access Control
Only Admins can perform `create` operations.

### 📊 UML Diagram
```mermaid
classDiagram
    class EmployeeDAO {
        <<Interface>>
        +create(employee String)
    }
    class EmployeeDAOImpl { +create(employee String) }
    class EmployeeDAOProxy {
        -EmployeeDAOImpl dao
        +create(employee String)
        -isAdmin() boolean
    }
    EmployeeDAO <|.. EmployeeDAOImpl
    EmployeeDAO <|.. EmployeeDAOProxy
    EmployeeDAOProxy o-- EmployeeDAOImpl : delegates
```

---

## 3. 🌳 Composite Pattern
**Purpose**: Treat individual objects and compositions of objects uniformly (Tree Structures).

**Case Study**: File System (Files and Directories).

### 📊 UML Diagram
```mermaid
classDiagram
    class FileSystem {
        <<Interface>>
        +ls()
    }
    class File { +ls() }
    class Directory {
        -List~FileSystem~ children
        +ls()
        +add(fs FileSystem)
    }
    FileSystem <|.. File
    FileSystem <|.. Directory
    Directory o-- FileSystem : contains many
```

---

## 4. 🔌 Adapter Pattern
**Purpose**: Allow objects with incompatible interfaces to collaborate.

**Case Study**: Weight Machine (Pounds to KG).

### 📊 UML Diagram
```mermaid
classDiagram
    class WeightMachine {
        <<Interface>>
        +getWeightInPounds() double
    }
    class WeightMachineImpl { +getWeightInPounds() double }
    class WeightMachineAdapter {
        <<Interface>>
        +getWeightInKG() double
    }
    class KGAdapterImpl {
        -WeightMachine machine
        +getWeightInKG() double
    }
    WeightMachine <|.. WeightMachineImpl
    WeightMachineAdapter <|.. KGAdapterImpl
    KGAdapterImpl o-- WeightMachine : adapts
```

---

## 🌉 5. Bridge Pattern
**Purpose**: Decouple abstraction from its implementation so the two can vary independently.

**Case Study**: Living Things & Breathing Processes.

### 📊 UML Diagram
```mermaid
classDiagram
    class BreatheImplementer {
        <<Interface>>
        +breatheProcess()
    }
    class LandBreathe { +breatheProcess() }
    class LivingThing {
        <<Abstract>>
        #BreatheImplementer impl
        +breathe()
    }
    class Dog { +breathe() }
    
    BreatheImplementer <|.. LandBreathe
    LivingThing <|-- Dog
    LivingThing o-- BreatheImplementer : Bridge
```

---

## 🚪 6. Facade Pattern
**Purpose**: Provide a simplified interface to a complex set of classes/subsystems.

**Case Study**: AC System (Internal + External Units).

### 📊 UML Diagram
```mermaid
classDiagram
    class ACFacade {
        -InternalUnit internal
        -ExternalUnit external
        +turnOnAC()
    }
    class InternalUnit { +acceptOnCommand() }
    class ExternalUnit { +checkVoltage() | +startCondenser() }

    ACFacade --> InternalUnit
    ACFacade --> ExternalUnit
```

---

## 🪶 7. Flyweight Pattern
**Purpose**: Support high numbers of fine-grained objects efficiently by sharing common data.

**Key Concepts**:
- **Intrinsic**: Shared state (e.g., Robot Type, Sprite).
- **Extrinsic**: Unique state passed in (e.g., Coordinates X, Y).

### 📊 UML Diagram
```mermaid
classDiagram
    class Robot {
        <<Interface>>
        +display(x double, y double)
    }
    class HumanoidRobot {
        -String type (Intrinsic)
        -Sprite (Intrinsic)
        +display(x, y)
    }
    class RobotFactory {
        -Map~String, Robot~ cache
        +createRobot(type) Robot
    }
    Robot <|.. HumanoidRobot
    RobotFactory o-- Robot : caches
```

---

## 📈 Summary Table

| Pattern | Goal | Relationship |
| :--- | :--- | :--- |
| **Decorator** | Add features at runtime | **is-a + has-a** |
| **Proxy** | Control access | **is-a** |
| **Composite** | Tree structures | **has-a list** |
| **Adapter** | Interface matching | **has-a** |
| **Bridge** | Independence | **has-a implementation** |
| **Facade** | Simplicity | **has-a subsystem** |
| **Flyweight** | Memory efficiency | **Shared state** |
