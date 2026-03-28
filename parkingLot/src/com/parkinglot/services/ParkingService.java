package com.parkinglot.services;

import com.parkinglot.models.Bill;
import com.parkinglot.models.Gate;
import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.Ticket;
import com.parkinglot.models.Vehicle;
import com.parkinglot.strategies.PricingStrategy;
import com.parkinglot.strategies.SlotAllocationStrategy;
import java.time.LocalDateTime;

public class ParkingService {
    private SlotAllocationStrategy slotStrategy;
    private PricingStrategy pricingStrategy;
    private PaymentService paymentService;

    public ParkingService(SlotAllocationStrategy s,
                          PricingStrategy p,
                          PaymentService pay) {
        this.slotStrategy = s;
        this.pricingStrategy = p;
        this.paymentService = pay;
    }

    public Ticket park(Vehicle v, Gate gate) {
        ParkingSlot slot = slotStrategy.findSlot(v.getType(), gate);

        if (slot == null) {
            System.err.println("❌ Entry Failed: No Slot available for Vehicle " + v.getLicensePlate());
            return null;
        }

        synchronized (slot) {
            if (!slot.isAvailable()) return null;
            slot.occupy(v);
        }

        return new Ticket(v, slot);
    }

    public Bill exit(Ticket t, LocalDateTime exitTime) {
        double amt = pricingStrategy.calculateAmount(t, exitTime);
        t.getSlot().vacate();
        paymentService.processPayment(amt);
        return new Bill(amt);
    }
}
