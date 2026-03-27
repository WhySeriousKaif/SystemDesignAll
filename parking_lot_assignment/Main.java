package parking_lot_assignment;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing Multi-Level Parking Lot Components...");
        
        ParkingLot parkingLot = ParkingLot.getInstance();

        // Initialize extensible strategies
        NearestSlotStrategy nearestSlotStrategy = new NearestSlotStrategy();
        PricingStrategy pricingStrategy = new WeekendPricingStrategy(new HourlyPricingStrategy());

        parkingLot.setSlotAllocationStrategy(nearestSlotStrategy);
        parkingLot.setPricingStrategy(pricingStrategy);

        // Setup Physical Layout
        Gate entry1 = new Gate("E1", 0);
        Gate entry2 = new Gate("E2", 1);
        Gate exit1 = new Gate("EXT1", 0);

        parkingLot.addEntryGate(entry1);
        parkingLot.addEntryGate(entry2);
        parkingLot.addExitGate(exit1);

        Level l0 = new Level(0);
        Level l1 = new Level(1);

        // Adding SMALL slots
        ParkingSlot s1 = new ParkingSlot("S1", SlotType.SMALL);
        s1.addDistance(entry1, 10); // Close to E1
        s1.addDistance(entry2, 100);

        ParkingSlot s2 = new ParkingSlot("S2", SlotType.SMALL);
        s2.addDistance(entry1, 50);
        s2.addDistance(entry2, 20); // Close to E2

        // Adding LARGE slot
        ParkingSlot s3 = new ParkingSlot("S3", SlotType.LARGE);
        s3.addDistance(entry1, 5);
        s3.addDistance(entry2, 5); // Close to both

        l0.addSlot(s1);
        l0.addSlot(s3);
        l1.addSlot(s2);

        parkingLot.addLevel(l0);
        parkingLot.addLevel(l1);

        // Register slots optimally to Strategy
        nearestSlotStrategy.addSlot(entry1, s1);
        nearestSlotStrategy.addSlot(entry2, s1);
        nearestSlotStrategy.addSlot(entry1, s2);
        nearestSlotStrategy.addSlot(entry2, s2);
        nearestSlotStrategy.addSlot(entry1, s3);
        nearestSlotStrategy.addSlot(entry2, s3);

        System.out.println("Initialization Complete!");
        parkingLot.showStatus();

        // ------------- System Demonstration -------------
        System.out.println("\n[Action] Bike 1 arrives at E1 requesting SMALL.");
        Vehicle bike1 = new Vehicle("DL-101", VehicleType.TWO_WHEELER);
        Receipt r1 = parkingLot.park(bike1, entry1, SlotType.SMALL);
        System.out.println("--> Assigned Slot: " + r1.getSlot().getSlotId() + 
                           " (Distance from E1: " + r1.getSlot().getDistanceToGates().get(entry1) + ")");
        parkingLot.showStatus();

        System.out.println("\n[Action] Bike 2 arrives at E2 requesting SMALL.");
        Vehicle bike2 = new Vehicle("DL-202", VehicleType.TWO_WHEELER);
        Receipt r2 = parkingLot.park(bike2, entry2, SlotType.SMALL);
        System.out.println("--> Assigned Slot: " + r2.getSlot().getSlotId() + 
                           " (Distance from E2: " + r2.getSlot().getDistanceToGates().get(entry2) + ")");
        parkingLot.showStatus();

        System.out.println("\n[Action] Bike 1 exits 4 hours later.");
        LocalDateTime fakeExitTime = r1.getEntryTime().plusHours(4); 
        double cost = parkingLot.exit(r1, fakeExitTime);
        System.out.println("--> Total Cost calculated: $" + cost);
        parkingLot.showStatus();
    }
}
