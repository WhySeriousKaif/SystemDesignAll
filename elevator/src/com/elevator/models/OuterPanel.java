package com.elevator.models;

import com.elevator.manager.ElevatorManager;
import com.elevator.observers.ElevatorObserver;

public class OuterPanel implements ElevatorObserver {
    private final int floor;
    private final ElevatorManager manager;

    public OuterPanel(int floorNum, ElevatorManager mgr) {
        this.floor = floorNum;
        this.manager = mgr;
        System.out.println("Panel created at floor " + floorNum);
    }

    public void requestElevator(Direction direction) {
        System.out.println("Panel at floor " + floor + " requesting elevator " + direction);
        manager.addToQueue(floor, direction);
    }

    @Override
    public void update(int currentFloor, StateType state) {
        // Individual notifications now include context of the movement
        System.out.println("Panel at floor " + this.floor + " received update: Elevator car moved to floor " 
                 + currentFloor + " (" + state + ")");
    }
}
