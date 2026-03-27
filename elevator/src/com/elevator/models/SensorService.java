package com.elevator.models;

public class SensorService {
    private static final double MAX_WEIGHT = 750.0;

    public boolean checkWeight(Elevator elevator, double currentWeight) {
        if (currentWeight > MAX_WEIGHT) {
            System.out.println("⚠️ [Sensor] Overweight detected! (" + currentWeight + "kg)");
            elevator.soundAlarm();
            elevator.keepDoorOpen();
            return false;
        }
        return true;
    }
}
