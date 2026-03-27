package scaler.concept.factory;

/**
 * Factory Method Pattern Demonstration
 * Concept: Transport System (Bike & Car)
 */

// 1. PRODUCT (Abstract Product)
abstract class Transport {
    public abstract void displayType();
}

// 2. CONCRETE PRODUCTS
class Bike extends Transport {
    @Override
    public void displayType() {
        System.out.println("I am a Bike (Two-wheeler)");
    }
}

class Car extends Transport {
    @Override
    public void displayType() {
        System.out.println("I am a Car (Four-wheeler)");
    }
}

// 3. CREATOR (Factory Interface)
interface TransportFactory {
    // The Factory Method
    Transport buildTransport();
}

// 4. CONCRETE CREATORS (Specialized Factories)
class BikeFactory implements TransportFactory {
    @Override
    public Transport buildTransport() {
        return new Bike();
    }
}

class CarFactory implements TransportFactory {
    @Override
    public Transport buildTransport() {
        return new Car();
    }
}

// 5. CLIENT (Context)
class User {
    private Transport myTransport;

    /**
     * Dependency Injection via Factory
     * The User doesn't know WHICH factory is passed, 
     * just that it can build a Transport.
     */
    public User(TransportFactory factory) {
        myTransport = factory.buildTransport();
    }

    public Transport getTransport() {
        return myTransport;
    }
}

// 6. MAIN EXECUTION
public class FactoryPatternDemo {

    public static void main(String[] args) {
        System.out.println("--- Factory Method Pattern Demo ---\n");

        // Use Case 1: Create a bike
        TransportFactory bikeFactory = new BikeFactory();
        User bikeUser = new User(bikeFactory);
        Transport myBike = bikeUser.getTransport();
        myBike.displayType();

        // Use Case 2: Create a car
        TransportFactory carFactory = new CarFactory();
        User carUser = new User(carFactory);
        Transport myCar = carUser.getTransport();
        myCar.displayType();
        
        System.out.println("\n✅ Decoupling achieved: User class never directly used 'new Bike()' or 'new Car()'.");
    }
}
