package parking_lot_assignment;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class NearestSlotStrategy implements SlotAllocationStrategy {
    private Map<Gate, TreeSet<ParkingSlot>> slotQueues;

    public NearestSlotStrategy() {
        slotQueues = new HashMap<>();
    }

    public void addSlot(Gate gate, ParkingSlot slot) {
        // We use a custom comparator to sort by distance, then fallback to slotId to prevent collisions 
        // when identical distances occur across different slots for the same gate.
        slotQueues.computeIfAbsent(gate, k -> new TreeSet<>(new Comparator<ParkingSlot>() {
            @Override
            public int compare(ParkingSlot a, ParkingSlot b) {
                int distA = getDistance(gate, a);
                int distB = getDistance(gate, b);
                if (distA != distB) {
                    return Integer.compare(distA, distB);
                }
                return a.compareTo(b); // absolute unique tie-breaker
            }
        })).add(slot);
    }

    @Override
    public ParkingSlot findSlot(SlotType slotType, Gate entryGate) {
        TreeSet<ParkingSlot> set = slotQueues.get(entryGate);
        if (set == null) return null;

        // Start scanning from the structurally identical 'nearest' slots
        for (ParkingSlot slot : set) {
            // Lazy deletion equivalent strategy! Instead of explicitly removing slots from every gate's Set
            // we simply track the real-time isOccupied object-level state.
            if (!slot.isOccupied() && slot.getType() == slotType) {
                return slot;
            }
        }
        return null;
    }

    private int getDistance(Gate gate, ParkingSlot slot) {
        return slot.getDistanceToGates().getOrDefault(gate, Integer.MAX_VALUE);
    }
}
