package BookMyShow.theaters;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private String name;
    private List<Seat> seats = new ArrayList<>();

    public Screen(String name) { this.name = name; }

    public String getName() { return name; }
    public List<Seat> getSeats() { return seats; }
}
