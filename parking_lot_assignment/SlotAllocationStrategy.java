package parking_lot_assignment;

public interface SlotAllocationStrategy {
    ParkingSlot findSlot(SlotType slotType, Gate entryGate);
}
