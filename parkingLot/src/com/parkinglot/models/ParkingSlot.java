package com.parkinglot.models;

import com.parkinglot.enums.SlotType;
import java.util.HashMap;
import java.util.Map;

public class ParkingSlot {
    private String slotId;
    private SlotType type;
    private boolean isOccupied;
    private Map<Gate, Integer> distanceMap = new HashMap<>();

    public ParkingSlot(String id, SlotType type) {
        this.slotId = id;
        this.type = type;
        this.isOccupied = false;
    }

    public synchronized void occupy(Vehicle v) {
        this.isOccupied = true;
    }

    public synchronized void vacate() {
        this.isOccupied = false;
    }

    public boolean isAvailable() {
        return !isOccupied;
    }

    public String getSlotId() { return slotId; }
    public SlotType getType() { return type; }
}
