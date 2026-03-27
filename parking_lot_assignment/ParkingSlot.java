package parking_lot_assignment;

import java.util.HashMap;
import java.util.Map;

// Implements Comparable so it can be uniquely ordered in a TreeSet purely by SlotID
public class ParkingSlot implements Comparable<ParkingSlot> {
    private String slotId;
    private SlotType type;
    private boolean isOccupied;
    private Map<Gate, Integer> distanceToGates;

    public ParkingSlot(String slotId, SlotType type) {
        this.slotId = slotId;
        this.type = type;
        this.isOccupied = false;
        this.distanceToGates = new HashMap<>();
    }

    public String getSlotId() { return slotId; }
    public SlotType getType() { return type; }
    public boolean isOccupied() { return isOccupied; }

    public Map<Gate, Integer> getDistanceToGates() {
        return distanceToGates;
    }

    public void addDistance(Gate gate, int distance) {
        distanceToGates.put(gate, distance);
    }

    // Fine-grained locking for multi-threading collision prevention
    public synchronized boolean occupy(Vehicle vehicle) {
        if (!this.isOccupied) {
            this.isOccupied = true;
            return true;
        }
        return false;
    }

    public synchronized void vacate() {
        this.isOccupied = false;
    }

    @Override
    public int compareTo(ParkingSlot other) {
        return this.slotId.compareTo(other.slotId);
    }
}
