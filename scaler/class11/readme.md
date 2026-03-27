# 🍿 Class 11 — Movie Ticket Booking System (BookMyShow)

A comprehensive low-level design (LLD) for a Movie Ticket Booking System, handling multiple theaters, cities, concurrent bookings, and dynamic pricing strategies.

⸻

## 📋 1. Core Requirements

### Functional Requirements
- **Location-based Browsing**: Users must select a city/location first.
- **Search & Discovery**:
  - List all theaters or movies in a selected city.
  - List all movies playing at a specific theater.
  - List all shows (different theaters and timings) for a specific movie.
- **Booking Flow**:
  - Display current real-time status of all seats for a given show.
  - Select seats and proceed to payment.
  - Confirm booking and generate a ticket.
  - Support cancellation of bookings.
- **Dynamic Pricing**: Base price is set per show. Final price is calculated by applying custom rules:
  - **Timing**: Morning vs Evening shifts.
  - **Day**: Weekends and Holiday surcharges.
  - **Week of month**: (e.g., month-end specials).
  - **Demand**: Auto-scaling based on % of seats filled.
- **Admin Capabilities**: 
  - `addMovie()`: Metadata, duration, and language.
  - `addTheater()`: Locations and screen mappings.
  - `addShow()`: Link Movie + Theater + Screen + Slot.

### Non-Functional & Technical Requirements
- **Concurrency**: Ensure the exact same seat cannot be booked by multiple users fighting for the same show.
- **Payment Timeouts**: Handle scenarios where an inventory lock is acquired but the user abandons or fails payment.
- **Performance**: Use caching for heavy-read operations (movie/theater catalogs) and efficient data structures (e.g., bit vectors) for seat tracking.
- **User Identification**: Use **Email Address** as the unique identifier for all users.
- **Multilingual Support**: Design for movies in multiple languages and regions.

### 🚫 Out of Scope (For V1)
- No internal currency or custom tokens.
- No discount policies, coupon codes, or referral systems.
- No add-on features like food, beverages, or merchandise.

⸻

## 🔄 2. User Journey (Flow)

1. **Select Location** 📍
2. **Path A (By Movie):**
   - View all movies in the city ➡️ Click Movie ➡️ See Shows (Theaters & Timings)
3. **Path B (By Theater):**
   - View all theaters ➡️ Select Theater ➡️ See Movies showing there
4. **Show Selection** ➡️ View Seat Map (Available vs. Booked)
5. **Seat Selection** ➡️ The system places a **temporary lock** on the seats.
6. **Payment** ➡️ If successful, confirm booking. If failed/timeout, release the lock.

⸻

## 🏛️ 3. Core Entities & Data Model

| Entity | Description |
| :--- | :--- |
| **User** | Customers and Admins using the system. |
| **City / Location** | Represents geographical boundaries for filtering. |
| **Theater (Cinema)** | Physical building containing multiple screens. |
| **Screen (Auditorium)** | A specific hall inside a theater. |
| **Movie** | The film metadata (Title, Duration, Language, Rating). |
| **Show** | The intersection of a Movie, Screen, and Time. |
| **Seat** | Physical seats in a screen. Can have types (Gold, Platinum, Silver) which vary per show. |
| **Booking (Ticket)** | The reservation record for a user, show, and specific seats. |

### 📊 V0 UML — Core Entities

```mermaid
classDiagram
    class City {
        -String name
        -List~Theater~ theaters
    }
    class Theater {
        -String name
        -String address
        -List~Screen~ screens
    }
    class Screen {
        -String name
        -List~Seat~ seats
    }
    class Seat {
        -String seatNumber
        -SeatType type
    }
    class Movie {
        -String title
        -int durationMins
        -Language language
    }
    class Show {
        -LocalDateTime startTime
        -LocalDateTime endTime
        -Movie movie
        -Screen screen
    }
    class User {
        -String name
        -String email
    }
    class Customer {
        -String name
        -String email
        +makeBooking()
        +cancelBooking()
    }
    class Admin {
        -String name
        -String email
        +addMovie(Movie)
        +addTheater(Theater)
        +addShow(Show, Theater)
        +updateMovie(Movie)
    }
    class Booking {
        -String bookingId
        -List~Seat~ bookedSeats
        -Show show
        -BookingStatus status
    }

    User <|-- Customer
    User <|-- Admin
    City "1" *-- "*" Theater : Association
    Theater "1" *-- "*" Screen : Composition
    Screen "1" *-- "*" Seat : Composition
    Movie "1" -- "*" Show : Association
    Theater "1" -- "*" Show : Association
    Show "1" -- "*" Seat : Association
    User "1" -- "*" Booking : Creates
    Booking "1" -- "1" Show : Holds
    Booking "*" -- "*" Seat : Reserves
```

⸻

## 🛡️ 4. Services & Abstractions

### 💸 Payment & Refund Services
To handle multiple payment modes and follow **Open/Closed Principle**, we use interfaces.

```java
// Payment Abstraction
public interface PaymentService {
    boolean processPayment(double amount);
}

public class UPIPayment implements PaymentService {
    @Override public boolean processPayment(double amt) { /* UPI Logic */ return true; }
}

public class CardPayment implements PaymentService {
    @Override public boolean processPayment(double amt) { /* Card Logic */ return true; }
}

// Refund Abstraction
public interface RefundService {
    boolean processRefund(String bookingId, double amount);
}

public class UPIRefund implements RefundService {
    @Override public boolean processRefund(String id, double amt) { /* UPI Refund */ return true; }
}
```

⸻

## 💻 4. V1 Implementation — Core Logic

Below is the initial structured implementation using **Singleton**, **Strategy**, and **Controller** patterns.

### 🎮 Booking Orchestrator (Singleton)
The `BookingService` manages the user session and coordinates between movies and theaters.
```java
public class BookingService {
    private static BookingService instance;
    private final MovieController movieController;
    private final TheatreController theatreController;
    private final PaymentService paymentService;
    private final RefundService refundService;

    private BookingService(PaymentService paymentService, RefundService refundService) {
        this.movieController = new MovieController();
        this.theatreController = new TheatreController();
        this.paymentService = paymentService;
        this.refundService = refundService;
    }

    public static BookingService getInstance() {
        if (instance == null) instance = new BookingService();
        return instance;
    }

    public void startBookingSession() {
        City userCity = selectCity();
        Movie selectedMovie = selectMovie(userCity);
        Show selectedShow = selectShow(userCity, selectedMovie);
        bookSeat(selectedShow);
    }
}
```

### 🎛️ Controllers & Filtering
```java
public class TheatreController {
    // Maps City to its list of Theaters
    Map<City, List<Theatre>> cityVsTheatre;

    public Map<Theatre, List<Show>> getAllShow(Movie movie, City city) {
        Map<Theatre, List<Show>> theatreVsShows = new HashMap<>();
        List<Theatre> theatres = cityVsTheatre.get(city);

        for(Theatre theatre : theatres) {
            List<Show> givenMovieShows = new ArrayList<>();
            for(Show show : theatre.getShows()) {
                if(show.getMovie().getMovieId() == movie.getMovieId()) {
                    givenMovieShows.add(show);
                }
            }
            if(!givenMovieShows.isEmpty()) theatreVsShows.put(theatre, givenMovieShows);
        }
        return theatreVsShows;
    }
}
```

⸻

## 📊 5. Ultimate & Complete UML Diagram

Below is the definitive architecture combining all **V0 Entities**, **Controllers**, and **Abstract Services**.

```mermaid
classDiagram
    class BookingService {
        <<singleton>>
        -MovieController movieController
        -TheatreController theatreController
        -PaymentService paymentService
        -RefundService refundService
        +startBookingSession()
        +initialize()
    }
    class MovieController {
        -Map cityVsMovies
        -List allMovies
        +addMovie(Movie, City)
        +getMovieByName(String)
        +getMoviesByCity(City)
    }
    class TheatreController {
        -Map cityVsTheatre
        -List allTheatre
        +addTheatre(Theatre, City)
        +getAllShow(Movie, City) Map
    }
    
    class User { <<abstract>> -String name | -String email }
    class Admin { +addMovie() | +addTheater() | +addShow() }
    class Customer { +makeBooking() | +cancelBooking() }

    class PaymentService { <<interface>> +processPayment(amount) }
    class RefundService { <<interface>> +processRefund(bookingId, amount) }

    %% Relationships
    BookMyShow ..> BookingService : uses
    BookingService *-- MovieController : Composition
    BookingService *-- TheatreController : Composition
    BookingService *-- PaymentService : Composition
    BookingService *-- RefundService : Composition

    User <|-- Admin : Inheritance
    User <|-- Customer : Inheritance
    Customer "1" -- "*" Booking : Creates

    City "1" -- "*" Theater : Association
    Theater "1" *-- "*" Screen : Composition
    Theater "1" -- "*" Show : Association
    Screen "1" *-- "*" Seat : Composition
    Show "1" -- "1" Movie : Association
    Show "1" -- "1" Screen : Association
    Show "1" -- "*" Seat : Tracking Availability

    Booking "1" -- "1" Show : Reserves
    Booking "1" -- "*" Seat : Contains

    PaymentService <|.. UPIPayment : Implementation
    PaymentService <|.. CardPayment : Implementation
    RefundService <|.. UPIRefund : Implementation
```


⸻

## 🛠️ 6. Technical Implementation Notes (Advanced)

### ⚠️ Concurrency & Seat Locking
- **The Problem:** Two users clicking the same `Seat_A1` at the exact same millisecond.
- **Database Transactions:** Essential to use strict ACID properties.
- **Optimistic vs. Pessimistic Locking:** 
  - *Pessimistic Locking* (e.g., `SELECT ... FOR UPDATE`) is safer for highly contended rows but limits throughput.
  - *Optimistic Locking* (using a `version` column) handles high throughput better but requires handling `OptimisticLockException` gracefully.
- **Temporary Lock:** When a user selects seats, switch status to `LOCKED` with a TTL (Time To Live, e.g., 10 minutes). If payment succeeds, switch to `BOOKED`. If TTL expires, revert to `AVAILABLE`.

### 🧠 Efficient Seat Tracking
- Instead of querying hundreds of row locks per show, consider using **Bit Vectors** (Bitmaps) in cache (e.g., Redis). 
- `1` = Booked/Locked, `0` = Available. 
- Fast Bitwise operations can instantly find contiguous available seats.

### 💰 Pricing Strategy Pattern
Pricing rules can be elegantly handled using the **Strategy Pattern**. Base price can be decorated or augmented based on runtime factors.

```java
public interface PricingStrategy {
    double calculatePrice(Show show, SeatType type);
}

// Example Implementations:
// - WeekendPricingStrategy
// - HolidayPricingStrategy
// - BlockbusterPricingStrategy
```

### ⚡ Caching
- Implement caching for static/read-heavy data like the Movie Catalog, Theater Lists, and static Show Schedules to reduce database load.
- Ensure proper cache invalidation policies when admins add or modify shows.

### 🔐 API & Security
- **API Design:** Use strictly RESTful principles with versioning (e.g., `/api/v1/bookings`).
- **Security:** Implement JWT/OAuth authentication for users and strict Role-Based Access Control (RBAC) for Admins. Ensure secure PCI-compliant handling of payment gateways.
