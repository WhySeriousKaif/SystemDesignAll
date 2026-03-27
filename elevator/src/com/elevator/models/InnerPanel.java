package com.elevator.models;

public class InnerPanel {
    private final Elevator elevator;

    public InnerPanel(Elevator e) {
        this.elevator = e;
    }

    public void pressFloorButton(int floor) {
        System.out.println("🔘 [InnerPanel] Floor " + floor + " button pressed.");
        elevator.addToQueue(floor);
    }

    public void pressOpen() {
        System.out.println("🔘 [InnerPanel] OPEN button pressed.");
        elevator.openDoor();
    }

    public void pressClose() {
        System.out.println("🔘 [InnerPanel] CLOSE button pressed.");
        elevator.closeDoor();
    }

    public void pressAlarm() {
        System.out.println("🔘 [InnerPanel] 🚨 ALARM button pressed.");
        elevator.triggerAlarm();
    }
}
