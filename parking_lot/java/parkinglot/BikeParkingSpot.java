package parking_lot.java.parkinglot;

import parking_lot.java.vehicles.Vehicle;

public class BikeParkingSpot extends ParkingSpot {
    
    public BikeParkingSpot(int spotNumber){
        super(spotNumber,"Bike");
    }
    @Override
    public boolean canParkVehicle(Vehicle vehicle) {
        return "Bike".equalsIgnoreCase(vehicle.getVehicleType());
    }
}
