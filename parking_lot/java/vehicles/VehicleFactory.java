package parking_lot.java.vehicles;

public class VehicleFactory {

    public static Vehicle createVehicle(String vehicleType,String licencePlate){
         if(vehicleType.equalsIgnoreCase("Car")){
            return new CarVehicle(licencePlate);
         }
         else if(vehicleType.equalsIgnoreCase("Bike")){
            return new BikeVehicle(licencePlate);
         }
         else{
            throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
         }

    }
    
}
