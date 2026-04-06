package BookMyShow.controllers;

import BookMyShow.Movie.Movie;
import BookMyShow.enums.City;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieController {
    private Map<City, List<Movie>> cityMovies = new HashMap<>();

    public void addMovie(Movie movie, City city) {
        cityMovies.computeIfAbsent(city, k -> new ArrayList<>()).add(movie);
    }

    public List<Movie> getMoviesByCity(City city) {
        return cityMovies.getOrDefault(city, new ArrayList<>());
    }
}
