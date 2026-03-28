package com.parkinglot.strategies;

import com.parkinglot.enums.VehicleType;
import com.parkinglot.models.Gate;
import com.parkinglot.models.ParkingSlot;

public interface SlotAllocationStrategy {
    ParkingSlot findSlot(VehicleType type, Gate gate);
}
