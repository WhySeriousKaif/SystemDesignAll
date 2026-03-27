package parking_lot.java.gates;


import java.util.Scanner;

import parking_lot.java.parkinglot.ParkingLot;
import parking_lot.java.parkinglot.ParkingSpot;
import parking_lot.java.vehicles.Vehicle;
import parking_lot.java.vehicles.VehicleFactory;

public class EntranceGate {
    private ParkingLot parkingLot;
    private Scanner scanner;

    // Constructor to initialize EntranceGate with the parking lot and scanner
    public EntranceGate(ParkingLot parkingLot, Scanner scanner) {
        this.parkingLot = parkingLot;
        this.scanner = scanner;
    }

    // Method to process the vehicle entrance
    public void processEntrance() {

        System.out.println("Enter the vehicle license plate: ");
        String licensePlate = scanner.next();
        System.out.println("Enter the vehicle type (Car or Bike): ");
        String vehicleType = scanner.next();

        Vehicle vehicle = VehicleFactory.createVehicle(vehicleType, licensePlate);
        if (vehicle == null) {
            System.out.println("Invalid vehicle type! Only Car and Bike are allowed.");
            return;
        }

        // Try to park the vehicle
        ParkingSpot spot = parkingLot.parkVehicle(vehicle);
        if (spot != null) {
            System.out.println("Vehicle parked successfully in spot: " + spot.getSpotNumber());
        } else {
            System.out.println("No available spots for the vehicle type.");
        }
    }
}