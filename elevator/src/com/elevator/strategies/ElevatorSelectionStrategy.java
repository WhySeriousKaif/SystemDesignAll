package com.elevator.strategies;

import com.elevator.models.Elevator;
import com.elevator.models.Direction;
import java.util.List;

public interface ElevatorSelectionStrategy {
    Elevator selectElevator(int floor, Direction direction, List<Elevator> elevators);
}
