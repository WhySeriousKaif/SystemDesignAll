package com.parkinglot.models;

import com.parkinglot.services.ParkingService;

public class EntryGate extends Gate {
    public EntryGate(String id, int floor) {
        super(id, floor);
    }

    public Ticket requestParking(Vehicle v, ParkingService service) {
        return service.park(v, this);
    }
}
