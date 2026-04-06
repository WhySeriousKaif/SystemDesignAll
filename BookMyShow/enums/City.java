package BookMyShow.enums;

import BookMyShow.theaters.Theater;
import java.util.ArrayList;
import java.util.List;

public class City {
    private String name;
    public List<Theater> theaters = new ArrayList<>();

    public City(String name) { this.name = name; }

    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}
