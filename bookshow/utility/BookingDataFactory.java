package BookMyShow.utility;

import BookMyShow.Movie.Movie;
import BookMyShow.controllers.MovieController;
import BookMyShow.controllers.TheaterController;
import BookMyShow.enums.City;
import BookMyShow.theaters.Screen;
import BookMyShow.theaters.Show;
import BookMyShow.theaters.Theater;

public class BookingDataFactory {

    public static void createMovies(MovieController movieController) {
        movieController.addMovie(new Movie(1, "Inception", 148), City.MUMBAI);
        movieController.addMovie(new Movie(2, "Interstellar", 169), City.MUMBAI);
        movieController.addMovie(new Movie(3, "The Dark Knight", 152), City.DELHI);
        movieController.addMovie(new Movie(4, "Avatar", 162), City.DELHI);
        movieController.addMovie(new Movie(5, "Avengers: Endgame", 181), City.BANGALORE);
    }

    public static void createTheatres(MovieController movieController, TheaterController theaterController) {
        // Create a theater in Mumbai
        Theater pvrMumbai = new Theater();
        pvrMumbai.setTheaterId(1);
        pvrMumbai.setTheaterName("PVR Mumbai");
        pvrMumbai.setCity(City.MUMBAI);

        // Add screens
        Screen screen1 = new Screen();
        screen1.setScreenId(1);
        pvrMumbai.getScreens().add(screen1);

        // Add shows
        Movie inception = movieController.getMovieByName("Inception");
        if (inception != null) {
            Show morningShow = new Show();
            morningShow.setShowId(101);
            morningShow.setMovie(inception);
            morningShow.setShowStartTime(10); // 10 AM
            morningShow.setScreen(screen1);
            pvrMumbai.getShows().add(morningShow);
        }

        theaterController.addTheater(pvrMumbai, City.MUMBAI);

        // Create a theater in Delhi
        Theater inoxDelhi = new Theater();
        inoxDelhi.setTheaterId(2);
        inoxDelhi.setTheaterName("INOX Delhi");
        inoxDelhi.setCity(City.DELHI);

        Screen screen2 = new Screen();
        screen2.setScreenId(2);
        inoxDelhi.getScreens().add(screen2);

        Movie darkKnight = movieController.getMovieByName("The Dark Knight");
        if (darkKnight != null) {
            Show eveningShow = new Show();
            eveningShow.setShowId(201);
            eveningShow.setMovie(darkKnight);
            eveningShow.setShowStartTime(18); // 6 PM
            eveningShow.setScreen(screen2);
            inoxDelhi.getShows().add(eveningShow);
        }

        theaterController.addTheater(inoxDelhi, City.DELHI);

        // Create a theater in Bangalore
        Theater pvrBangalore = new Theater();
        pvrBangalore.setTheaterId(3);
        pvrBangalore.setTheaterName("PVR Bangalore");
        pvrBangalore.setCity(City.BANGALORE);

        Screen screen3 = new Screen();
        screen3.setScreenId(3);
        pvrBangalore.getScreens().add(screen3);

        Movie avengers = movieController.getMovieByName("Avengers: Endgame");
        if (avengers != null) {
            Show nightShow = new Show();
            nightShow.setShowId(301);
            nightShow.setMovie(avengers);
            nightShow.setShowStartTime(21); // 9 PM
            nightShow.setScreen(screen3);
            pvrBangalore.getShows().add(nightShow);
        }

        theaterController.addTheater(pvrBangalore, City.BANGALORE);
    }
}
