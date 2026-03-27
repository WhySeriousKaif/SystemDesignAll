package com.elevator.models;

import com.elevator.states.State;
import com.elevator.states.IdleState;
import com.elevator.observers.ElevatorObserver;
import com.elevator.strategies.TaskSchedulingStrategy;
import com.elevator.strategies.FIFOSchedulingStrategy;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Queue;

public class Elevator implements Runnable {
    private final int id;
    private int currentFloor;
    private State state;
    private final Queue<Integer> floorQueue;
    private final List<ElevatorObserver> observers;
    private final TaskSchedulingStrategy schedulingStrategy;
    private final InnerPanel innerPanel;
    private boolean running = true;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 1;
        this.state = new IdleState(this);
        this.floorQueue = new ConcurrentLinkedQueue<>();
        this.observers = new CopyOnWriteArrayList<>();
        this.schedulingStrategy = new FIFOSchedulingStrategy();
        this.innerPanel = new InnerPanel(this);
        System.out.println("Elevator " + id + " created at floor " + currentFloor);
    }

    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (ElevatorObserver observer : observers) {
            observer.update(currentFloor, state.getType());
        }
    }

    public void setState(State newState) {
        this.state = newState;
        System.out.println("Elevator " + id + " changed state to " + newState.getType());
        notifyObservers();
    }

    public void addToQueue(int floor) {
        floorQueue.add(floor);
        System.out.println("Elevator " + id + " received request for floor " + floor);
    }

    @Override
    public void run() {
        while (running) {
            if (!floorQueue.isEmpty()) {
                processQueue();
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public synchronized void processQueue() {
        int targetFloor = schedulingStrategy.getNextFloor(currentFloor, floorQueue);
        if (targetFloor == -1) return;

        System.out.println("Elevator " + id + " processing request for floor " + targetFloor);

        if (targetFloor > currentFloor) {
            state.moveUp();
            simulateMovement(targetFloor);
        } else if (targetFloor < currentFloor) {
            state.moveDown();
            simulateMovement(targetFloor);
        } else {
            floorQueue.remove(targetFloor);
            System.out.println("Elevator " + id + " already at floor " + targetFloor);
            state.stop();
        }
    }

    private void simulateMovement(int targetFloor) {
        while (currentFloor != targetFloor) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            if (targetFloor > currentFloor) {
                currentFloor++;
            } else {
                currentFloor--;
            }
            System.out.println("Elevator " + id + " moved to floor " + currentFloor);
            notifyObservers();
        }
        floorQueue.remove(targetFloor);
        state.stop();
        System.out.println("Elevator " + id + " arrived at target floor " + currentFloor);
        notifyObservers();
    }

    public void soundAlarm() {
        System.out.println("🔊 [Elevator " + id + "] 🚨 ALARM! 🚨");
    }

    public void triggerAlarm() {
        System.out.println("🔔 [Elevator " + id + "] Emergency Alarm Triggered by User!");
        soundAlarm();
        stop();
    }

    public void openDoor() {
        System.out.println("🚪 [Elevator " + id + "] Opening door...");
    }

    public void closeDoor() {
        System.out.println("🚪 [Elevator " + id + "] Closing door...");
    }

    public void keepDoorOpen() {
        System.out.println("🚪 [Elevator " + id + "] Door kept open for safety.");
    }

    public void stop() {
        System.out.println("🛑 [Elevator " + id + "] EMERGENCY STOP!");
        this.running = false;
    }

    public int getCurrentFloor() { return currentFloor; }
    public int getId() { return id; }
    public State getState() { return state; }
    public InnerPanel getInnerPanel() { return innerPanel; }
}
