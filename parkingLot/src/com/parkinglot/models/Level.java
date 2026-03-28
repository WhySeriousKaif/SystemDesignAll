package com.parkinglot.models;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int floorNumber;
    private List<ParkingSlot> slots = new ArrayList<>();

    public Level(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() { return floorNumber; }
    public List<ParkingSlot> getSlots() { return slots; }
}
