package scaler.concept.prototype;

/**
 * Prototype Design Pattern Demonstration
 * Concept: Efficient Object Cloning (Car Example)
 */

// 1. PROTOTYPE INTERFACE (The Contract)
interface Prototype {
    Prototype clone();
}

// 2. CONCRETE PROTOTYPE (Car Class)
class Car implements Prototype {
    private String model;
    private String engineType;

    // Imagine this constructor does heavy work (DB lookup, network etc.)
    public Car(String model, String engineType) {
        this.model = model;
        this.engineType = engineType;
        System.out.println("[DB CALL] Car initialized for: " + model);
    }

    // Copy Constructor (Used for cloning)
    public Car(Car source) {
        this.model = source.model;
        this.engineType = source.engineType;
    }

    // Clone method: Returns a copy of itself
    @Override
    public Prototype clone() {
        // Option 1: Using copy constructor
        return new Car(this);
        
        // Option 2: Java's native super.clone() (requires Cloneable interface)
    }

    @Override
    public String toString() {
        return "Car{model='" + model + "', engineType='" + engineType + "', hash=" + System.identityHashCode(this) + "}";
    }
}

// 3. MAIN EXECUTION
public class PrototypePatternDemo {

    public static void main(String[] args) {
        System.out.println("--- Prototype Pattern Demo ---\n");

        // 1. Create the 'Master' Prototype (Expensive setup)
        Car prototypeCar = new Car("BMW M3", "Twin-Turbo I6");
        System.out.println("Original: " + prototypeCar);

        // 2. Fast cloning for multiple instances
        System.out.println("\n--- Cloning instances ---");
        Car clone1 = (Car) prototypeCar.clone();
        Car clone2 = (Car) prototypeCar.clone();

        System.out.println("Clone 1: " + clone1);
        System.out.println("Clone 2: " + clone2);

        // 3. Verification
        System.out.println("\n--- Integrity Check ---");
        System.out.println("Are object references same? " + (prototypeCar == clone1)); // False
        System.out.println("Is data identical? " + prototypeCar.toString().split("hash")[0].equals(clone1.toString().split("hash")[0])); // True

        System.out.println("\n✅ Success: Created multiple objects without repeating expensive [DB CALL] initialization.");
    }
}
