package BookMyShow.controllers;

import BookMyShow.Movie.Movie;
import BookMyShow.enums.City;
import BookMyShow.theaters.Show;
import BookMyShow.theaters.Theater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheaterController {

    Map<City,List<Theater> > cityVsMovies;
    List<Theater>allTheaters;

    public TheaterController(){
        cityVsMovies=new HashMap<>();
        allTheaters=new ArrayList<>();


    }

    public void addTheater(Theater theater,City city){
        allTheaters.add(theater);

        List<Theater>theaters=cityVsMovies.getOrDefault(city,new ArrayList<>());
        theaters.add(theater);
        cityVsMovies.put(city,theaters);
    }

    public List<Theater> getTheatersByCity(City city){
        return cityVsMovies.getOrDefault(city, new ArrayList<>());
    }

    public Map<Theater,List<Show>> getAllShow(Movie movie,City city){
        Map<Theater,List<Show>> theaterVsShows=new HashMap<>();

        List<Theater>theaters=getTheatersByCity(city);

        for(Theater theater:theaters){
            List<Show>shows=theater.getShows();
            //shows=[morning,evening]
            List<Show>movieShows=new ArrayList<>();

            for(Show show:shows){
                if(show.getMovie().getMovieId()==movie.getMovieId()){
                    movieShows.add(show);
                }
            }

            if(!movieShows.isEmpty()){
                theaterVsShows.put(theater,movieShows);
            }
        }
        return theaterVsShows;
    }
}
