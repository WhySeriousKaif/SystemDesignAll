package parking_lot.java.parkinglot;

import java.time.LocalDateTime;

import parking_lot.java.vehicles.Vehicle;

public class Ticket {
    private ParkingSpot parkingSpot;
    private Vehicle vehicle;
    private LocalDateTime entryTime;


    public Ticket(ParkingSpot parkingSpot,Vehicle vehicle){
        this.parkingSpot=parkingSpot;
        this.vehicle=vehicle;
        this.entryTime=LocalDateTime.now();
    }
    
    
}
