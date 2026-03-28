package com.parkinglot.singleton;

import com.parkinglot.models.Level;
import com.parkinglot.models.Gate;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private static ParkingLot instance;
    private List<Level> levels = new ArrayList<>();
    private List<Gate> gates = new ArrayList<>();

    private ParkingLot() {}

    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }

    public List<Level> getLevels() { return levels; }
    public List<Gate> getGates() { return gates; }
}
