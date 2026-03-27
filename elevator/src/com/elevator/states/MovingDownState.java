package com.elevator.states;

import com.elevator.models.Elevator;
import com.elevator.models.StateType;

public class MovingDownState extends State {
    public MovingDownState(Elevator e) { super(e); }

    @Override
    public void moveUp() { elevator.setState(new MovingUpState(elevator)); }

    @Override
    public void moveDown() { /* Already moving down */ }

    @Override
    public void stop() { elevator.setState(new IdleState(elevator)); }

    @Override
    public StateType getType() { return StateType.MOVING_DOWN; }
}
