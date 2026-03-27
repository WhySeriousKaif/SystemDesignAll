package com.elevator.strategies;

import com.elevator.models.Direction;
import com.elevator.models.Elevator;
import com.elevator.models.StateType;
import java.util.List;

public class NearestElevatorStrategy implements ElevatorSelectionStrategy {
    @Override
    public Elevator selectElevator(int floor, Direction direction, List<Elevator> elevators) {
        if (elevators.isEmpty()) return null;

        Elevator nearestElevator = elevators.get(0);
        int shortestDistance = Math.abs(floor - elevators.get(0).getCurrentFloor());

        for (Elevator elevator : elevators) {
            int distance = Math.abs(floor - elevator.getCurrentFloor());
            StateType currentState = elevator.getState().getType();

            // Prioritize idle elevators
            if (currentState == StateType.IDLE && distance < shortestDistance) {
                shortestDistance = distance;
                nearestElevator = elevator;
                continue;
            }

            // For moving elevators, consider their direction
            if (direction == Direction.UP && currentState == StateType.MOVING_UP && 
                elevator.getCurrentFloor() < floor && distance < shortestDistance) {
                shortestDistance = distance;
                nearestElevator = elevator;
            }
            else if (direction == Direction.DOWN && currentState == StateType.MOVING_DOWN && 
                     elevator.getCurrentFloor() > floor && distance < shortestDistance) {
                shortestDistance = distance;
                nearestElevator = elevator;
            }
        }
        return nearestElevator;
    }
}
