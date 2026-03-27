package com.elevator;

import com.elevator.manager.ElevatorManager;
import com.elevator.models.Direction;
import com.elevator.models.Elevator;
import com.elevator.models.OuterPanel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Elevator System Simulation");
        System.out.println("===================================\n");

        // Create manager
        ElevatorManager manager = new ElevatorManager();

        // Create elevators (Autonomous units)
        Elevator elevator1 = new Elevator(1);
        Elevator elevator2 = new Elevator(2);
        manager.addElevator(elevator1);
        manager.addElevator(elevator2);

        // Start elevator threads
        new Thread(elevator1).start();
        new Thread(elevator2).start();

        // Create panels for floors
        OuterPanel panel1 = new OuterPanel(1, manager);
        OuterPanel panel2 = new OuterPanel(2, manager);
        OuterPanel panel3 = new OuterPanel(3, manager);

        // Observers now subscribe to individual elevators according to interviewer feedback
        elevator1.addObserver(panel1);
        elevator1.addObserver(panel2);
        elevator1.addObserver(panel3);
        elevator2.addObserver(panel1);
        elevator2.addObserver(panel2);
        elevator2.addObserver(panel3);

        System.out.println("\n--- Simulating elevator requests ---");

        // Simulate simultaneous requests
        panel3.requestElevator(Direction.DOWN);
        panel1.requestElevator(Direction.UP);
        panel2.requestElevator(Direction.UP);

        // Simulate Inner Panel Buttons
        System.out.println("\n--- Testing Inner Panel Buttons ---");
        elevator1.getInnerPanel().pressFloorButton(2);
        elevator1.getInnerPanel().pressOpen();
        elevator1.getInnerPanel().pressClose();
        elevator2.getInnerPanel().pressAlarm();

        // Simulate Weight Sensor Check
        System.out.println("\n--- Testing Safety Sensor ---");
        com.elevator.models.SensorService sensor = new com.elevator.models.SensorService();
        sensor.checkWeight(elevator1, 800.0); // Should trigger alarm

        // Wait for elevators to finish processing for summary
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nSimulation completed. Elevators are running in the background.");
    }
}
