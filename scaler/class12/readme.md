# Event-Driven Trading System - Low Level Design

## 🧠 10-Second Viva Summary
> “This is an event-driven trading system where market data flows into a pluggable strategy engine. Strategies use indicators to generate orders, which pass through a chain of risk rules and are executed via an execution engine using broker adapters. The system uses Strategy, Observer, Chain of Responsibility, and Adapter patterns, with normalized entities for orders, trades, and positions.”

---

## 🎯 How To Draw This (Important)
👉 **Don't try to draw everything randomly.**
👉 **Draw in 3 layers:**
1. **Core Flow (top)**
2. **Engines (middle)**
3. **Data & DB (bottom)**

---

## 🧩 Complete System UML Diagram

```mermaid
classDiagram
    %% Core Architecture
    class MarketDataService {
        +subscribe()
        +publishTick()
    }

    class StrategyEngine {
        +onTick()
        +evaluate()
    }

    class RiskEngine {
        +validate(order)
    }

    class ExecutionEngine {
        +placeOrder()
        +cancelOrder()
    }

    class BrokerAdapter {
        <<interface>>
        +sendOrder()
        +cancelOrder()
    }

    %% Core Flow flow
    MarketDataService --> StrategyEngine : Tick Data
    StrategyEngine --> RiskEngine : Generates Order
    RiskEngine --> ExecutionEngine : Validated Order
    ExecutionEngine --> BrokerAdapter : Routes Order

    %% Strategy Side
    class Strategy {
        <<interface>>
        +onTick()
        +onOrderFill()
    }

    class RSIStrategy { }
    class MACDStrategy { }
    class CustomScript { }

    Strategy <|-- RSIStrategy
    Strategy <|-- MACDStrategy
    Strategy <|-- CustomScript

    StrategyEngine o-- Strategy : Evaluates

    %% Indicators
    class Indicator {
        <<interface>>
        +compute()
    }

    class RSI { }
    class EMA { }
    class MACD { }

    Indicator <|-- RSI
    Indicator <|-- EMA
    Indicator <|-- MACD

    Strategy o-- Indicator : Uses

    %% Risk Engine (Chain of Responsibility)
    class RiskRule {
        <<interface>>
        +check(order)
    }

    class MaxLoss { }
    class MaxPos { }
    class Margin { }
    class Concentration { }

    RiskRule <|-- MaxLoss
    RiskRule <|-- MaxPos
    RiskRule <|-- Margin
    RiskRule <|-- Concentration

    RiskEngine o-- RiskRule : Executes Chain

    %% Execution Engine Polymorphism
    class LiveExec { }
    class PaperExec { }

    ExecutionEngine <|-- LiveExec
    ExecutionEngine <|-- PaperExec

    %% Broker Adapter Implementations
    class Zerodha { }
    class Upstox { }

    BrokerAdapter <|-- Zerodha
    BrokerAdapter <|-- Upstox
    
    %% Entities (Database)
    class User {
        +String id
        +String name
        +String email
    }

    class StrategyEntity {
        +String id
        +String user_id
        +String name
        +String type
        +String status
        +Double capital_allocated
        +Integer version
    }

    class Position {
        +String id
        +String user_id
        +String symbol
        +Double quantity
        +Double avg_price
        +Double pnl
    }

    class Order {
        +String id
        +String strategy_id
        +String symbol
        +String type
        +Double quantity
        +Double price
        +String status
        +DateTime timestamp
    }

    class Trade {
        +String id
        +String order_id
        +Double fill_price
        +Double quantity
        +DateTime timestamp
    }

    class MarketTick {
        +String symbol
        +Double price
        +DateTime timestamp
        +Double volume
    }

    %% DB Relations
    User "1" --> "*" StrategyEntity
    StrategyEntity "1" --> "*" Order
    Order "1" --> "*" Trade
    User "1" --> "*" Position
```

---

## 🧱 Component Breakdown

### 🟦 1. Core Architecture (Top Layer)

The highest level event-driven flow of core operations in the system.

```mermaid
flowchart TD
    MDS[MarketDataService<br/>subscribe()<br/>publishTick()] -->|Tick Data| SE
    SE[StrategyEngine<br/>onTick()<br/>evaluate()] -->|Generates Order| RE
    RE[RiskEngine<br/>validate(order)] -->|Validated Order| EE
    EE[ExecutionEngine<br/>placeOrder()] -->|Routes Order| BA
    BA[BrokerAdapter<br/>sendOrder()<br/>cancelOrder()]
```

### 🟨 2. Strategy Side (Pluggable Design)

Utilizes the **Strategy Pattern** to swap algorithms on the fly to fulfill conditions.

```mermaid
classDiagram
    class Strategy {
        <<interface>>
        +onTick()
        +onOrderFill()
    }
    class RSIStrategy
    class MACDStrategy
    class CustomScript

    Strategy <|-- RSIStrategy
    Strategy <|-- MACDStrategy
    Strategy <|-- CustomScript
```

### 📊 Indicators (Composition)

Each strategy contains one or more indicators.

```mermaid
classDiagram
    class Indicator {
        <<interface>>
        +compute()
    }
    class RSI
    class EMA
    class MACD

    Indicator <|-- RSI
    Indicator <|-- EMA
    Indicator <|-- MACD

    note for Indicator "Strategy → uses → Indicator"
```

### 🟥 3. Risk Engine (Chain of Responsibility)

Utilizes **Chain of Responsibility Pattern** to filter out orders if they violate predefined risk management rules.

```mermaid
classDiagram
    class RiskRule {
        <<interface>>
        +check(order)
    }
    class MaxLoss
    class MaxPos
    class Margin
    class Concentration

    RiskRule <|-- MaxLoss
    RiskRule <|-- MaxPos
    RiskRule <|-- Margin
    RiskRule <|-- Concentration

    note for RiskRule "RiskEngine → List~RiskRule~"
```

### 🟩 4. Execution Engine

Exhibits **Polymorphism** where strategies are untethered to the execution mode.

```mermaid
classDiagram
    class ExecutionEngine {
        +placeOrder()
        +cancelOrder()
    }
    class LiveExec
    class PaperExec

    ExecutionEngine <|-- LiveExec
    ExecutionEngine <|-- PaperExec
```

### 🟪 5. Broker Adapter

Utilizes the **Adapter Pattern** to interface cleanly with unique external APIs.

```mermaid
classDiagram
    class BrokerAdapter {
        <<interface>>
        +placeOrder()
        +cancelOrder()
    }
    class Zerodha
    class Upstox

    BrokerAdapter <|-- Zerodha
    BrokerAdapter <|-- Upstox
```

---

## 🗄️ 6. Database / Entities

### Conceptual Entity Overview
| Entity | Attributes | Scope / Details |
|---|---|---|
| **User** | `id`, `name`, `email` | Standard application user |
| **StrategyEntity** | `id`, `user_id`, `name`, `type`, `status` (DRAFT/LIVE), `capital_allocated`, `version` | Represents the persistent configuration of strategies mapped to a runtime `Strategy` |
| **Position** | `id`, `user_id`, `symbol`, `quantity`, `avg_price`, `pnl` | Stores open market positions in real-time |
| **Order** | `id`, `strategy_id`, `symbol`, `type` (MARKET/LIMIT), `quantity`, `price`, `status`, `timestamp` | Triggered intent generated via strategies |
| **Trade** | `id`, `order_id`, `fill_price`, `quantity`, `timestamp` | Represents real settled transactions resulting financially from an order |
| **MarketTick** | `symbol`, `price`, `timestamp`, `volume` | Tick data representation for historical charting/testing |

### Schema Relationships
```mermaid
erDiagram
    USER ||--o{ STRATEGY_ENTITY : creates
    USER ||--o{ POSITION : holds
    STRATEGY_ENTITY ||--o{ ORDER : triggers
    ORDER ||--o{ TRADE : settles_into
```

## 🚀 Future Options
* Map components perfectly to production level interface/class structures in Java
* Prepare Mock Interview queries based directly on the decisions made in the diagram

---
---

# Hotel Management System - Low Level Design

## 🧠 10-Second Viva Summary
> “Hotel system manages booking lifecycle: availability → booking → payment → notification, with payment implemented using Strategy pattern and clean relational mapping between user, room, and hotel.”

---

## 🎯 How To Draw This (Important)
👉 **Don't try to draw everything randomly.**
👉 **Draw in 3 layers:**
1. **Core Flow (top)**
2. **Engines / Domains (middle)**
3. **Data & DB (bottom)**

---

## 🧩 Complete System UML Diagram

```mermaid
classDiagram
    %% Core Architecture Flow
    class BookingService {
        +createBooking()
        +cancelBooking()
    }

    class AvailabilityService {
        +checkRooms()
    }

    class PaymentService {
        +processPayment()
    }

    class NotificationService {
        +sendEmail()
    }

    BookingService --> AvailabilityService : Checks
    AvailabilityService --> PaymentService : Triggers
    PaymentService --> NotificationService : Notifies

    %% Domain Classes
    class User {
        +String id
        +String name
        +String email
    }

    class Hotel {
        +String id
        +String name
        +String location
    }

    class Room {
        +String id
        +String type
        +Double price
        +String status
    }

    class Booking {
        +String id
        +String userId
        +String roomId
        +DateTime checkIn
        +DateTime checkOut
        +String status
    }

    %% Payment Strategy
    class PaymentStrategy {
        <<interface>>
        +pay(amount)
    }

    class CardPayment { }
    class UpiPayment { }

    PaymentStrategy <|-- CardPayment
    PaymentStrategy <|-- UpiPayment

    PaymentService o-- PaymentStrategy : Uses

    %% Relationships
    User "1" --> "*" Booking
    Booking "*" --> "1" Room
    Room "*" --> "1" Hotel
```

---

## 🧱 Component Breakdown

### 🟦 1. Core Flow (Top Layer)
```mermaid
flowchart TD
    BS[BookingService<br/>createBooking()<br/>cancelBooking()] --> AS
    AS[AvailabilityService<br/>checkRooms()] --> PS
    PS[PaymentService<br/>processPayment()] --> NS
    NS[NotificationService<br/>sendEmail()]
```

### 🟨 2. Domain Classes
```mermaid
classDiagram
    class User {
        +String id
        +String name
        +String email
    }
    class Hotel {
        +String id
        +String name
        +String location
    }
    class Room {
        +String id
        +String type
        +Double price
        +String status
    }
    class Booking {
        +String id
        +String userId
        +String roomId
        +DateTime checkIn
        +DateTime checkOut
        +String status
    }
```

### 🟧 3. Payment Strategy (Important)
Utilizes the **Strategy Pattern** to handle multiple payment methods gracefully.
```mermaid
classDiagram
    class PaymentStrategy {
        <<interface>>
        +pay(amount)
    }
    class CardPayment
    class UpiPayment

    PaymentStrategy <|-- CardPayment
    PaymentStrategy <|-- UpiPayment

    note for PaymentStrategy "PaymentService → uses → PaymentStrategy"
```

### 🟥 4. Relationships
```mermaid
flowchart LR
    User --> Booking
    Booking --> Room
    Room --> Hotel
```

---

## 🗄️ 5. Database / Entities

### Conceptual Entity Overview
| Entity | Attributes |
|---|---|
| **User** | `id`, `name`, `email` |
| **Hotel** | `id`, `name`, `location` |
| **Room** | `id`, `hotel_id`, `type`, `price`, `status` |
| **Booking** | `id`, `user_id`, `room_id`, `checkIn`, `checkOut`, `status` |
| **Payment** | `id`, `booking_id`, `amount`, `status` |

### Schema Relationships
```mermaid
erDiagram
    USER ||--o{ BOOKING : creates
    ROOM ||--o{ BOOKING : reserved_in
    HOTEL ||--o{ ROOM : contains
    BOOKING ||--|{ PAYMENT : requires
```

---
---

# Hospital Management System - Low Level Design

## 🧠 10-Second Viva Summary
> “Hospital system handles patient flow: appointment → doctor assignment → billing → pharmacy, using Strategy pattern for billing and maintaining medical records linked to patients.”

---

## 🎯 How To Draw This (Important)
👉 **Don't try to draw everything randomly.**
👉 **Draw in 3 layers:**
1. **Core Flow (top)**
2. **Engines / Domains (middle)**
3. **Data & DB (bottom)**

---

## 🧩 Complete System UML Diagram

```mermaid
classDiagram
    %% Core Architecture Flow
    class AppointmentService {
        +bookAppointment()
    }

    class DoctorService {
        +assignDoctor()
    }

    class BillingService {
        +generateBill()
    }

    class PharmacyService {
        +dispenseMedicine()
    }

    AppointmentService --> DoctorService
    DoctorService --> BillingService
    BillingService --> PharmacyService

    %% Domain Classes
    class Patient {
        +String id
        +String name
        +Integer age
    }

    class Doctor {
        +String id
        +String name
        +String specialization
    }

    class Appointment {
        +String id
        +String patientId
        +String doctorId
        +DateTime date
        +String status
    }

    class MedicalRecord {
        +String id
        +String diagnosis
        +String prescription
    }

    %% Billing Strategy
    class BillingStrategy {
        <<interface>>
        +calculate()
    }

    class Insurance { }
    class SelfPayment { }

    BillingStrategy <|-- Insurance
    BillingStrategy <|-- SelfPayment

    BillingService o-- BillingStrategy : Uses

    %% Relationships
    Patient "1" --> "*" Appointment
    Doctor "1" --> "*" Appointment
    Patient "1" --> "*" MedicalRecord
    Appointment "1" --> "1" BillingService : Triggers Billing
```

---

## 🧱 Component Breakdown

### 🟦 1. Core Flow
```mermaid
flowchart TD
    AS[AppointmentService<br/>bookAppointment()] --> DS
    DS[DoctorService<br/>assignDoctor()] --> BS
    BS[BillingService<br/>generateBill()] --> PS
    PS[PharmacyService<br/>dispenseMedicine()]
```

### 🟨 2. Domain Classes
```mermaid
classDiagram
    class Patient {
        +String id
        +String name
        +Integer age
    }
    class Doctor {
        +String id
        +String name
        +String specialization
    }
    class Appointment {
        +String id
        +String patientId
        +String doctorId
        +DateTime date
        +String status
    }
    class MedicalRecord {
        +String id
        +String diagnosis
        +String prescription
    }
```

### 🟥 3. Billing Strategy
Utilizes **Strategy Pattern** for differing payment logics.
```mermaid
classDiagram
    class BillingStrategy {
        <<interface>>
        +calculate()
    }
    class Insurance
    class SelfPayment

    BillingStrategy <|-- Insurance
    BillingStrategy <|-- SelfPayment
```

### 🟩 4. Relationships
```mermaid
flowchart TD
    Patient --> Appointment
    Doctor --> Appointment
    Patient --> MedicalRecord
    Appointment --> Billing
```

---

## 🗄️ 5. Database / Entities

### Conceptual Entity Overview
| Entity | Attributes |
|---|---|
| **Patient** | `id`, `name`, `age` |
| **Doctor** | `id`, `name`, `specialization` |
| **Appointment** | `id`, `patient_id`, `doctor_id`, `date`, `status` |
| **MedicalRecord**| `id`, `patient_id`, `diagnosis`, `prescription` |
| **Billing** | `id`, `appointment_id`, `amount`, `status` |

### Schema Relationships
```mermaid
erDiagram
    PATIENT ||--o{ APPOINTMENT : books
    DOCTOR ||--o{ APPOINTMENT : attends
    PATIENT ||--o{ MEDICAL_RECORD : owns
    APPOINTMENT ||--|| BILLING : generates
```

---

## 🚀 Quick Comparison (Interview Gold)
| System | Key Pattern | Flow |
|---|---|---|
| **Trading** | Strategy + Adapter + Chain | Tick → Order |
| **Hotel** | Strategy | Booking → Payment |
| **Hospital** | Strategy | Appointment → Billing |
