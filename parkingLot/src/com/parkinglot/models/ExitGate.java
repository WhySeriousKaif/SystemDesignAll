package com.parkinglot.models;

import com.parkinglot.services.ParkingService;
import java.time.LocalDateTime;

public class ExitGate extends Gate {
    public ExitGate(String id, int floor) {
        super(id, floor);
    }

    public Bill processExit(Ticket t, ParkingService service) {
        return service.exit(t, LocalDateTime.now());
    }
}
