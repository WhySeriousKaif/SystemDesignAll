package parking_lot.java.vehicles;

public abstract class  Vehicle {
    private String licencePlate;
    private String vehicleType;

    public Vehicle(String licencePlate,String vehicleType){
        this.licencePlate=licencePlate;
        this.vehicleType=vehicleType;
    }

    public abstract double calculateFee(int hoursStayed);

    public String getLicencePlate() {
        return licencePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    
}
