package com.elevator.observers;

import com.elevator.models.StateType;

public interface ElevatorObserver {
    void update(int floor, StateType state);
}
