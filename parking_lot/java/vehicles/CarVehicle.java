package parking_lot.java.vehicles;

public class CarVehicle  extends Vehicle{

    private static final double RATE = 10; // $10 per hour for cars

    public CarVehicle(String licencePlate){
        super(licencePlate,"Car");
    }

    @Override
    public double calculateFee(int hoursStayed) {
       return hoursStayed*RATE;
    }
    
}
