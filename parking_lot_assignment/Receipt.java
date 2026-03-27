package parking_lot_assignment;

import java.time.LocalDateTime;
import java.util.UUID;

public class Receipt {
    private String id;
    private Vehicle vehicle;
    private ParkingSlot assignedSlot;
    private LocalDateTime entryTime;

    public Receipt(Vehicle vehicle, ParkingSlot assignedSlot, LocalDateTime entryTime) {
        this.id = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.assignedSlot = assignedSlot;
        this.entryTime = entryTime;
    }

    public String getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSlot getSlot() { return assignedSlot; }
    public LocalDateTime getEntryTime() { return entryTime; }
}
