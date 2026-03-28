package com.parkinglot.strategies;

import com.parkinglot.enums.SlotType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.models.Gate;
import com.parkinglot.models.ParkingSlot;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class NearestSlotStrategy implements SlotAllocationStrategy {
    private Map<Gate, TreeSet<ParkingSlot>> slots = new HashMap<>();

    @Override
    public ParkingSlot findSlot(VehicleType type, Gate gate) {
        TreeSet<ParkingSlot> set = slots.get(gate);
        if (set == null) return null;

        for (ParkingSlot slot : set) {
            if (slot.isAvailable() && canFit(type, slot.getType())) {
                return slot;
            }
        }
        return null;
    }

    private boolean canFit(VehicleType v, SlotType s) {
        switch (v) {
            case BIKE: return true;
            case CAR: return s == SlotType.MEDIUM || s == SlotType.LARGE;
            case TRUCK: return s == SlotType.LARGE;
            default: return false;
        }
    }

    public Map<Gate, TreeSet<ParkingSlot>> getSlots() { return slots; }
}
