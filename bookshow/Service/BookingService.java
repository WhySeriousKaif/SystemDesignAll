package BookMyShow.Service;

import BookMyShow.enums.City;
import BookMyShow.controllers.MovieController;
import BookMyShow.controllers.TheaterController;
import BookMyShow.Movie.Movie;
import BookMyShow.theaters.Show;
import BookMyShow.theaters.Theater;
import BookMyShow.utility.BookingDataFactory;

import java.util.*;

public class BookingService {
    private static BookingService instance; // ✅ Singleton instance

    private final MovieController movieController;
    private final TheaterController theaterController;
    private final Scanner scanner;

    private BookingService() { // ✅ Private constructor
        movieController = new MovieController();
        theaterController = new TheaterController();
        scanner = new Scanner(System.in);
    }

    public static BookingService getInstance() { // ✅ Singleton method
        if (instance == null) {
            instance = new BookingService();
        }
        return instance;
    }

    public void startBookingSession() {
        printHeader("🎬 Welcome to BookMyShow 🎟️");
        boolean continueBooking = true;

        while (continueBooking) {
            City userCity = selectCity();
            
            List<Movie> movies = movieController.getMoviesByCity(userCity);
            if (movies.isEmpty()) {
                System.out.println("❌ No movies available in " + userCity + ". Please select another city.");
                continue;
            }

            Movie selectedMovie = selectMovie(movies, userCity);
            
            Map<Theater, List<Show>> showsMap = theaterController.getAllShow(selectedMovie, userCity);
            if (showsMap.isEmpty()) {
                System.out.println("❌ No shows available for " + selectedMovie.getMovieName() + " in " + userCity + ". Please select another movie/city.");
                continue;
            }

            Show selectedShow = selectShow(showsMap, userCity, selectedMovie);
            bookSeat(selectedShow);

            System.out.print("Do you want to book another ticket? (yes/no): ");
            String response = scanner.next().trim().toLowerCase();
            continueBooking = response.equals("yes");
        }

        printSuccess("Thank you for using BookMyShow! 🎬 Have a great day!");
    }

    private City selectCity() {
        printSection("🏙️ Select Your City");
        for (City city : City.values()) {
            System.out.println("   " + (city.ordinal() + 1) + ". " + city);
        }
        return City.values()[getUserChoice(1, City.values().length) - 1];
    }

    private Movie selectMovie(List<Movie> movies, City city) {
        printSection("🎥 Available Movies in " + city);
        for (int i = 0; i < movies.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + movies.get(i).getMovieName());
        }
        return movies.get(getUserChoice(1, movies.size()) - 1);
    }

    private Show selectShow(Map<Theater, List<Show>> showsMap, City city, Movie movie) {
        List<Show> availableShows = new ArrayList<>();
        printSection("🎭 Available Shows for " + movie.getMovieName() + " in " + city);
        int index = 1;
        for (Map.Entry<Theater, List<Show>> entry : showsMap.entrySet()) {
            for (Show show : entry.getValue()) {
                System.out.println("   " + index + ". " + show.getShowStartTime() + " at 🎦 " + entry.getKey().getTheaterName());
                availableShows.add(show);
                index++;
            }
        }
        return availableShows.get(getUserChoice(1, availableShows.size()) - 1);
    }

    private void bookSeat(Show show) {
        printSection("💺 Select Your Seat (1-100)");
        int seatNumber = getUserChoice(1, 100);
        if (show.getBookedSeatIds().contains(seatNumber)) {
            System.out.println("❌ Seat already booked! Please try another seat.");
            bookSeat(show);
        } else {
            show.getBookedSeatIds().add(seatNumber);
            PaymentService paymentService = new PaymentService();
            boolean paymentSuccess = paymentService.processPayment(250); // Example amount

            if (paymentSuccess) {
                printSuccess("✅ Booking Successful! Enjoy your movie! 🍿");
                generateTicket(show, seatNumber);
            } else {
                System.out.println("❌ Payment failed! Please try again.");
                show.getBookedSeatIds().remove(seatNumber); // Revert seat booking
            }
        }
    }

    private void generateTicket(Show show, int seatNumber) {
        System.out.println("\n========================================");
        System.out.println("🎟️       MOVIE TICKET CONFIRMATION       🎟️");
        System.out.println("========================================");
        System.out.println("🎬 Movie: " + show.getMovie().getMovieName());
        System.out.println("⏰ Show Time: " + show.getShowStartTime() + ":00");
        System.out.println("💺 Seat Number: " + seatNumber);
        System.out.println("----------------------------------------");
        System.out.println("📅 Date: " + java.time.LocalDate.now());
        System.out.println("🆔 Booking ID: " + UUID.randomUUID());
        System.out.println("========================================");
        System.out.println("🎉 Enjoy your movie! 🍿 Have a great time!");
        System.out.println("========================================\n");
    }


    private int getUserChoice(int min, int max) {
        int choice;
        do {
            System.out.print("👉 Enter choice (" + min + "-" + max + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < min || choice > max);
        return choice;
    }

    private void printHeader(String text) {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("          " + text);
        System.out.println("══════════════════════════════════════════\n");
    }

    private void printSection(String text) {
        System.out.println("\n🔹 " + text);
        System.out.println("──────────────────────────────────────────");
    }

    private void printSuccess(String text) {
        System.out.println("\n🎉 " + text + "\n");
    }

    public void initialize() {
        BookingDataFactory.createMovies(movieController);
        BookingDataFactory.createTheatres(movieController, theaterController);
    }
}