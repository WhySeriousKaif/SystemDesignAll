package com.elevator.strategies;

import java.util.Queue;

public interface TaskSchedulingStrategy {
    /**
     * Determines the next floor to visit based on the current floor and the pending requests.
     * @param currentFloor The elevator's current location.
     * @param floorQueue The queue of pending floor requests.
     * @return The next floor to visit, or -1 if no requests are pending.
     */
    int getNextFloor(int currentFloor, Queue<Integer> floorQueue);
}
