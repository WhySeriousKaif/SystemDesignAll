package com.elevator.strategies;

import java.util.Queue;

public class FIFOSchedulingStrategy implements TaskSchedulingStrategy {
    @Override
    public int getNextFloor(int currentFloor, Queue<Integer> floorQueue) {
        if (floorQueue.isEmpty()) {
            return -1;
        }
        // Basic FIFO: return the first requested floor
        return floorQueue.peek();
    }
}
