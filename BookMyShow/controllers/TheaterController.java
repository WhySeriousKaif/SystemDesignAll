package BookMyShow.controllers;

import BookMyShow.enums.City;
import BookMyShow.theaters.Theater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheaterController {
    private Map<City, List<Theater>> cityTheatres = new HashMap<>();

    public void addTheatre(Theater t, City city) {
        cityTheatres.computeIfAbsent(city, k -> new ArrayList<>()).add(t);
    }

    public List<Theater> getTheatersByCity(City city) {
        return cityTheatres.getOrDefault(city, new ArrayList<>());
    }
}
