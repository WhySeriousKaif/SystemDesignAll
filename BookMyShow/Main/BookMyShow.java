package BookMyShow.Main;

import BookMyShow.Movie.Movie;
import BookMyShow.Service.*;
import BookMyShow.enums.City;
import BookMyShow.enums.SeatType;
import BookMyShow.theaters.Screen;
import BookMyShow.theaters.Seat;
import BookMyShow.theaters.Show;
import BookMyShow.theaters.ShowSeat;
import BookMyShow.theaters.Theater;
import BookMyShow.users.Customer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BookMyShow {
    public static void main(String[] args) {
        System.out.println("🎬 Starting BookMyShow Modular System...\n");

        // 1. Initial State Setup
        City bangalore = new City("Bangalore");

        Theater pvr = new Theater("PVR Bangalore");
        Screen screen1 = new Screen("Audi 1");

        // Add Seats to Screen
        screen1.getSeats().add(new Seat("A1", SeatType.GOLD));
        screen1.getSeats().add(new Seat("A2", SeatType.GOLD));
        screen1.getSeats().add(new Seat("A3", SeatType.SILVER));

        pvr.getScreens().add(screen1);
        bangalore.theaters.add(pvr);

        Movie avengers = new Movie("Avengers: Endgame", 181);
        Show eveningShow = new Show(avengers, screen1, LocalDateTime.now().plusHours(5));

        // 2. Service Initialization
        BookingService service = BookingService.getInstance(
                new UPIPayment(), 
                new UPIRefund(), 
                new WeekendPricingStrategy()
        );

        // 3. User Setup
        Customer user = new Customer("Kaif", "kaif@email.com");

        // 4. Booking Flow Simulation
        try {
            System.out.println("🎟️ User " + user.getName() + " is looking for seats for " + avengers.getTitle() + "...");
            
            // Step 1: Lock Seats (Concurrency Check)
            List<ShowSeat> selectedSeats = service.lockSeats(eveningShow, Arrays.asList("A1", "A2"), user);
            System.out.println("🔒 Seats Locked: A1, A2 (Expires in 5 minutes)");

            // Step 2: Confirm Booking (Payment + Ticket)
            Booking booking = service.confirmBooking(user, eveningShow, selectedSeats);
            
            System.out.println("\n✅ BOOKING SUCCESSFUL!");
            System.out.println("----------------------------------------");
            System.out.println("ID: " + booking.getBookingId());
            System.out.println("Movie: " + booking.getShow().getMovie().getTitle());
            System.out.println("Status: " + booking.getStatus());
            System.out.println("----------------------------------------");

        } catch (Exception e) {
            System.err.println("❌ Booking Failed: " + e.getMessage());
        }

        System.out.println("\n🎬 Simulation Completed.");
    }
}
