package com.parkinglot.models;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static AtomicInteger counter = new AtomicInteger(0);

    private String ticketId;
    private Vehicle vehicle;
    private ParkingSlot slot;
    private LocalDateTime entryTime;

    public Ticket(Vehicle v, ParkingSlot slot) {
        this.ticketId = "T-" + counter.incrementAndGet();
        this.vehicle = v;
        this.slot = slot;
        this.entryTime = LocalDateTime.now();
    }

    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSlot getSlot() { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }
}
