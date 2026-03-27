package com.elevator.states;

import com.elevator.models.Elevator;
import com.elevator.models.StateType;

public abstract class State {
    protected Elevator elevator;

    public State(Elevator elevator) {
        this.elevator = elevator;
    }

    public abstract void moveUp();
    public abstract void moveDown();
    public abstract void stop();
    public abstract StateType getType();
}
