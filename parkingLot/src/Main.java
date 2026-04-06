import com.parkinglot.models.*;
import com.parkinglot.enums.*;
import com.parkinglot.strategies.*;
import com.parkinglot.services.*;
import com.parkinglot.singleton.ParkingLot;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Executing Modular Parking Lot Demonstration...\n");

        ParkingLot lot = ParkingLot.getInstance();

        // Setup
        Level l1 = new Level(1);
        ParkingSlot s1 = new ParkingSlot("S1", SlotType.SMALL);
        ParkingSlot s2 = new ParkingSlot("S2", SlotType.MEDIUM);
        l1.getSlots().addAll(Arrays.asList(s1, s2));
        lot.getLevels().add(l1);

        EntryGate g1 = new EntryGate("G1", 1);
        ExitGate g2 = new ExitGate("G2", 1);
        lot.getGates().addAll(Arrays.asList(g1, g2));

        NearestSlotStrategy strategy = new NearestSlotStrategy();
        strategy.getSlots().put(g1, new TreeSet<>(Comparator.comparing(a -> a.getSlotId())));
        strategy.getSlots().get(g1).addAll(l1.getSlots());

        ParkingService service = new ParkingService(
                strategy,
                new HourlyPricingStrategy(),
                new PaymentService()
        );

        // Run
        Vehicle v = new Vehicle("KA-01", VehicleType.CAR);
        Ticket t = g1.requestParking(v, service);
        if (t != null) {
            System.out.println("✅ Parked: " + t.getTicketId() + " at " + t.getSlot().getSlotId());
        }

        Bill b = g2.processExit(t, service);
        System.out.println("💵 Final Bill Amount: $" + b.getAmount());
    }
}
