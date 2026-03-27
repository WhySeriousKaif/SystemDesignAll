package BookMyShow.theaters;

import BookMyShow.enums.City;
import java.util.ArrayList;
import java.util.List;

public class Theater {
    int theaterId;
    String address;
    String theaterName;
    City city;
    List<Screen>screens=new ArrayList<>();
    List<Show>shows=new ArrayList<>();

    public int  getTheaterId(){
        return theaterId;
    }
    public void setTheaterId(int theaterId){
        this.theaterId=theaterId;
    }

    public  String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address=address;
    }

    public String getTheaterName(){
        return theaterName;
    }
    public void setTheaterName(String theaterName){
        this.theaterName=theaterName;
    }

    public City getCity(){
        return city;
    }
    public void setCity(City city){
        this.city=city;
    }

    public List<Screen> getScreens(){
        return screens;
    }
    public void setScreens(List<Screen> screens){
        this.screens=screens;
    }

    public List<Show> getShows(){
        return shows;
    }
    public void setShows(List<Show> shows){
        this.shows=shows;
    }
}
