package com.elevator.manager;

import com.elevator.models.Direction;
import com.elevator.models.Elevator;
import com.elevator.strategies.ElevatorSelectionStrategy;
import com.elevator.strategies.NearestElevatorStrategy;
import java.util.ArrayList;
import java.util.List;

public class ElevatorManager {
    private final List<Elevator> elevators;
    private ElevatorSelectionStrategy selectionStrategy;

    public ElevatorManager() {
        this.elevators = new ArrayList<>();
        this.selectionStrategy = new NearestElevatorStrategy();
        System.out.println("Elevator Manager created");
    }

    public void setSelectionStrategy(ElevatorSelectionStrategy strategy) {
        this.selectionStrategy = strategy;
    }

    public synchronized void addToQueue(int floor, Direction direction) {
        System.out.println("\n[Manager] Request received for floor " + floor + " (" + direction + ")");
        Elevator selectedElevator = selectionStrategy.selectElevator(floor, direction, elevators);
        if (selectedElevator != null) {
            selectedElevator.addToQueue(floor);
        }
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}
