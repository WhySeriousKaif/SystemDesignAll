package com.elevator.states;

import com.elevator.models.Elevator;
import com.elevator.models.StateType;

public class MovingUpState extends State {
    public MovingUpState(Elevator e) { super(e); }

    @Override
    public void moveUp() { /* Already moving up */ }

    @Override
    public void moveDown() { elevator.setState(new MovingDownState(elevator)); }

    @Override
    public void stop() { elevator.setState(new IdleState(elevator)); }

    @Override
    public StateType getType() { return StateType.MOVING_UP; }
}
