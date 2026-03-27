package parking_lot_assignment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ParkingLot {
    private static ParkingLot instance;
    private List<Level> levels;
    private List<Gate> entryGates;
    private List<Gate> exitGates;
    
    private PricingStrategy pricingStrategy;
    private SlotAllocationStrategy slotAllocationStrategy;

    // Explicit concurrency control 
    private final ReentrantLock lock = new ReentrantLock();

    private ParkingLot() {
        levels = new ArrayList<>();
        entryGates = new ArrayList<>();
        exitGates = new ArrayList<>();
    }

    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void setSlotAllocationStrategy(SlotAllocationStrategy slotAllocationStrategy) {
        this.slotAllocationStrategy = slotAllocationStrategy;
    }

    public void addLevel(Level level) { levels.add(level); }
    public void addEntryGate(Gate gate) { entryGates.add(gate); }
    public void addExitGate(Gate gate) { exitGates.add(gate); }

    public Receipt park(Vehicle vehicle, Gate entryGate, SlotType slotType) {
        lock.lock();
        try {
            ParkingSlot slot = slotAllocationStrategy.findSlot(slotType, entryGate);
            if (slot != null && !slot.isOccupied()) {
                // Safely occupy thanks to lock and strategy
                slot.occupy(vehicle);
                return new Receipt(vehicle, slot, LocalDateTime.now());
            }
            throw new RuntimeException("No completely available slot of type " + slotType + " found!");
        } finally {
            lock.unlock();
        }
    }

    public double exit(Receipt receipt, LocalDateTime exitTime) {
        double amount = pricingStrategy.calculateAmount(receipt, exitTime);
        receipt.getSlot().vacate();
        return amount;
    }

    public void showStatus() {
        Map<SlotType, Long> availableSlots = levels.stream()
            .flatMap(level -> level.getParkingSlots().stream())
            .filter(slot -> !slot.isOccupied())
            .collect(Collectors.groupingBy(ParkingSlot::getType, Collectors.counting()));
            
        System.out.println("--- Parking Lot Status ---");
        for (SlotType type : SlotType.values()) {
            System.out.println(type + " available spots: " + availableSlots.getOrDefault(type, 0L));
        }
        System.out.println("--------------------------");
    }
}
