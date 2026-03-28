package com.parkinglot.models;

public abstract class Gate {
    protected String gateId;
    protected int floorNumber;

    public Gate(String id, int floor) {
        this.gateId = id;
        this.floorNumber = floor;
    }

    public String getGateId() { return gateId; }
    public int getFloorNumber() { return floorNumber; }
}
