package BookMyShow.theaters;

import java.util.ArrayList;
import java.util.List;

public class Theater {
    private String name;
    private List<Screen> screens = new ArrayList<>();

    public Theater(String name) { this.name = name; }

    public String getName() { return name; }
    public List<Screen> getScreens() { return screens; }
}
