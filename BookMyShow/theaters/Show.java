package BookMyShow.theaters;

import BookMyShow.Movie.Movie;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Show {
    private Movie movie;
    private Screen screen;
    private LocalDateTime startTime;
    private Map<String, ShowSeat> seatMap = new ConcurrentHashMap<>();

    public Show(Movie movie, Screen screen, LocalDateTime time) {
        this.movie = movie;
        this.screen = screen;
        this.startTime = time;

        for (Seat s : screen.getSeats()) {
            seatMap.put(s.getSeatNumber(), new ShowSeat(s, 100));
        }
    }

    public Movie getMovie() { return movie; }
    public Screen getScreen() { return screen; }
    public LocalDateTime getStartTime() { return startTime; }
    public Map<String, ShowSeat> getSeatMap() { return seatMap; }
}
