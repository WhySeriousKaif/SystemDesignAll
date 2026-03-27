package parking_lot.java.vehicles;

public class BikeVehicle extends Vehicle {
    public static final double RATE = 5.0; // $5 per hour for bikes

    public BikeVehicle(String licencePlate){
        super(licencePlate,"Bike");
    }
    @Override
    public double calculateFee(int hoursStayed){
        return hoursStayed*RATE;
    }
    
}
