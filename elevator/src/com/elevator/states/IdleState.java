package com.elevator.states;

import com.elevator.models.Elevator;
import com.elevator.models.StateType;

public class IdleState extends State {
    public IdleState(Elevator e) { super(e); }

    @Override
    public void moveUp() { elevator.setState(new MovingUpState(elevator)); }

    @Override
    public void moveDown() { elevator.setState(new MovingDownState(elevator)); }

    @Override
    public void stop() { /* Already idle */ }

    @Override
    public StateType getType() { return StateType.IDLE; }
}
