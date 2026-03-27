package parking_lot_assignment;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int levelNumber;
    private List<ParkingSlot> parkingSlots;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.parkingSlots = new ArrayList<>();
    }

    public int getLevelNumber() { return levelNumber; }

    public void addSlot(ParkingSlot slot) {
        parkingSlots.add(slot);
    }

    public List<ParkingSlot> getParkingSlots() {
        return parkingSlots;
    }
}
