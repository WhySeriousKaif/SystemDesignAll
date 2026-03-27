package parking_lot_assignment;

import java.util.Objects;

public class Gate {
    private String gateId;
    private int floorNumber;

    public Gate(String gateId, int floorNumber) {
        this.gateId = gateId;
        this.floorNumber = floorNumber;
    }

    public String getGateId() { return gateId; }
    public int getFloorNumber() { return floorNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gate gate = (Gate) o;
        return floorNumber == gate.floorNumber && Objects.equals(gateId, gate.gateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gateId, floorNumber);
    }
}
