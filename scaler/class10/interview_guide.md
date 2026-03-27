# 🏆 Parking Lot: SDE-2 Interview & Viva Guide

This guide contains the exact breakdown, data structure trade-offs, and verbal scripts needed to ace an SDE-2 LLD interview using our Parking Lot design.

## 🧠 1. Interview Verdict (What Interviewers Look For)

**✅ What you did VERY well (Strong Points):**
*   Clear `ParkingLot` as a Singleton.
*   Proper `SlotStrategy` abstraction.
*   Distance-based optimization (🔥 huge strong point).
*   Clean separation of concerns: `Slot`, `Level`, `Gate`, `Receipt`.

**⚠️ What you must explicitly mention to stand out:**
*   **Explicit Concurrency Handling:** Interviewers expect it. You *must* mention slot-level locking.
*   **Gate-Level Linking:** Mention how gates are tied to explicit levels for accurate distance mapping.
*   **Data Structure for Fast Lookup:** Be explicit about using `TreeSet` or `PriorityQueue`.
*   **Compatibility Logic:** Mention that vehicles have specific slot type compatibilities.

> **💡 The Golden Sentence:**
> *"I would use a `TreeSet` (or `PriorityQueue` + lazy deletion) for efficient nearest slot lookup, and add slot-level locking to completely avoid race conditions."* 
> *(This line alone boosts your evaluation level!)*

---

## 🧱 2. Entity Design (Clean Tables)

### 🏢 ParkingLot (Singleton)
| Field | Type | Description |
| :--- | :--- | :--- |
| `instance` | `ParkingLot` | Singleton instance |
| `levels` | `List<Level>` | All floors |
| `gates` | `List<Gate>` | All entry/exit gates |
| `fareCalculator` | `FareCalculator` | Pricing logic strategy |
| `strategy` | `SlotStrategy` | Slot allocation logic strategy |

**Methods:**
| Method | Input | Output | Description |
| :--- | :--- | :--- | :--- |
| `park()` | `Vehicle, time, slotType, gate` | `Receipt` | Assigns the best slot |
| `exit()` | `Receipt, time` | `double` | Vacates slot & calculates bill |

### 🏬 Level
| Field | Type | Description |
| :--- | :--- | :--- |
| `levelId` | `int` | Floor number |
| `slotMapping` | `Map<SlotType, List<Slot>>` | Slots grouped by type |
| `entryGates` | `List<Gate>` | Entry gates strictly on this floor |
| `exitGates` | `List<Gate>` | Exit gates strictly on this floor |

### 🚪 Gate
| Field | Type | Description |
| :--- | :--- | :--- |
| `gateId` | `String` | Unique ID |
| `floorNumber` | `int` | Which floor this gate is on |
| `type` | `GateType` | ENTRY / EXIT |

### 🅿️ ParkingSlot
| Field | Type | Description |
| :--- | :--- | :--- |
| `slotId` | `String` | Unique slot identifier |
| `slotType` | `SlotType` | SMALL / MEDIUM / LARGE |
| `isOccupied` | `boolean` | Real-time availability |
| `distanceToGates` | `Map<Gate, Integer>` | Distance map relative to entry nodes |

**Methods:**
| Method | Description |
| :--- | :--- |
| `occupy()` | Safely mark as occupied |
| `vacate()` | Free the slot |

### 🎟️ Receipt
| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `String` | Ticket / Transaction ID |
| `vehicle` | `Vehicle` | Vehicle info |
| `assignedSlot` | `ParkingSlot` | Exact slot assigned |
| `entryTime` | `LocalDateTime` | Entry timestamp |

### 🚗 Vehicle & Enums
*   **Vehicle:** `licensePlate` (String), `type` (VehicleType)
*   **SlotType (Enum):** `SMALL`, `MEDIUM`, `LARGE`
*   **VehicleType (Enum):** `TWO_WHEELER`, `CAR`, `TRUCK`

---

## 🎲 3. Strategy Implementations

### SlotStrategy (Interface)
| Method | Description |
| :--- | :--- |
| `findSlot()` | Locates the optimal available slot |

**🟢 NearestSlotStrategy**
*   **Field:** `slotsPerGate` (`Map<Gate, TreeSet<Slot>>`)
*   **Logic:** Picks nearest slot using pre-calculated distance $O(\log N)$.

**🔵 RandomSlotStrategy**
*   **Logic:** Assigns any random strictly available slot $O(1)$ random index.

### 💳 FareCalculator (PricingStrategy)
*   **Field:** `fareMap` (`Map<SlotType, Double>`)
*   **Method `calculate()`:** Computes the price based on duration boundaries.

---

## ⚙️ 4. Data Structure Choice (CRUCIAL)

| DS | Use Case | Complexity |
| :--- | :--- | :--- |
| **`PriorityQueue`** | Simple nearest | $O(\log N)$ extraction, *but* $O(N)$ arbitrary deletion |
| **`PQ + Lazy deletion`** | Best practical | $O(\log N)$ amortized |
| **`TreeSet`** | Clean + consistent | $O(\log N)$ extraction & strict uniqueness |

---

## 🔒 5. Concurrency Fixes (Must Mention)

**The Problem ❌:** Two threads (vehicles at different gates) try to assign the exact same slot concurrently.

**The Solution ✅:**
| Approach | Code Idea | Verdict |
| :--- | :--- | :--- |
| **Global Lock** | `synchronized(parkingLot)` | Safe, but highly unscalable (blocks all gates). |
| **Slot-Level Lock** | `synchronized(slot)` | **Best:** High throughput, locks only the specific real-estate. |
| **Explicit Locks** | `ReentrantLock` | **Advanced:** Best for managing timeouts or fair queuing. |

---

## 🧠 6. Final "Perfect Answer" Script

If asked to summarize your entire Parking Lot architecture, use this exact script:

> *"I modeled the `ParkingLot` as a Singleton managing instances of `Level`s and `Gate`s. To ensure high-speed proximity routing, each `ParkingSlot` stores its exact distance to all gates. I handled the core allocation via the **Strategy Pattern**, utilizing a `TreeSet` (or Priority Queue with lazy deletion) to drop search latency down to $O(\log N)$. Finally, to handle edge-case concurrency where two gates request the same slot, I utilized slot-level locking to completely prevent double-booking. The entire system remains strictly extensible for new slot types and dynamic pricing strategies without modifying the core lot."*

---

## 🏗️ ASCII Architecture Map

```text
                    <<Singleton>>
                  ┌───────────────┐
                  │  ParkingLot   │
                  ├───────────────┤
                  │ - levels      │
                  │ - gates       │
                  │ - strategy    │
                  │ - fareCalc    │
                  ├───────────────┤
                  │ + park()      │
                  │ + exit()      │
                  └──────┬────────┘
                         │
         ┌───────────────┼────────────────┐
         │                                │
         ▼                                ▼
   ┌──────────────┐               ┌──────────────┐
   │    Level     │               │     Gate     │
   ├──────────────┤               ├──────────────┤
   │ - levelId    │               │ - gateId     │
   │ - slotsMap   │               │ - floorNo    │
   │ - entryGates │               │ - type       │
   │ - exitGates  │               └──────────────┘
   └──────┬───────┘
          │
          ▼
   ┌──────────────┐
   │ ParkingSlot  │
   ├──────────────┤
   │ - slotId     │
   │ - slotType   │
   │ - isOccupied │
   │ - distanceMap│
   ├──────────────┤
   │ + occupy()   │
   │ + vacate()   │
   └──────────────┘

--------------------------------------------------

   ┌──────────────────────────────┐
   │        <<Interface>>         │
   │     SlotStrategy             │
   ├──────────────────────────────┤
   │ + findSlot()                 │
   └──────────────┬──────────────┘
                  │
        ┌─────────┴──────────┐
        ▼                    ▼
┌─────────────────┐   ┌─────────────────┐
│ NearestStrategy │   │ RandomStrategy  │
├─────────────────┤   ├─────────────────┤
│ - slotsPerGate  │   │                 │
│ (TreeSet/PQ)    │   │                 │
├─────────────────┤   ├─────────────────┤
│ + findSlot()    │   │ + findSlot()    │
└─────────────────┘   └─────────────────┘

--------------------------------------------------

   ┌──────────────────────────────┐
   │       FareCalculator         │
   ├──────────────────────────────┤
   │ - fareMap                    │
   ├──────────────────────────────┤
   │ + calculateFare()            │
   └──────────────────────────────┘

--------------------------------------------------

   ┌──────────────┐
   │   Vehicle    │
   ├──────────────┤
   │ - number     │
   │ - type       │
   └──────────────┘

   ┌──────────────┐
   │   Receipt    │
   ├──────────────┤
   │ - id         │
   │ - vehicle    │
   │ - slot       │
   │ - entryTime  │
   └──────────────┘
```
