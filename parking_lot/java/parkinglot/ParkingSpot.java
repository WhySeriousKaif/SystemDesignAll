package parking_lot.java.parkinglot;

import parking_lot.java.vehicles.Vehicle;

public abstract class ParkingSpot {
    private int spotNumber;
    private boolean isOccupied;

    private Vehicle vehicle;

    private String spotType;

    public ParkingSpot(int spotNumber,String spotType){
        this.spotNumber=spotNumber;
        this.spotType=spotType;
        this.isOccupied=false;
    }

    

    public boolean isOccupied(){
        return isOccupied;
    }
    public abstract boolean canParkVehicle(Vehicle vehicle);
    
    public void parkVehicle(Vehicle vehicle){
        if(isOccupied){
            throw new IllegalStateException("Spot is already occupied !");
        }
        if(!canParkVehicle(vehicle)){
            throw new IllegalArgumentException("Vehicle type not supported for this spot");
        }
        this.vehicle=vehicle;
        this.isOccupied=true;
    }

    public void vacate(){
        if(!isOccupied){
            throw new IllegalStateException("Spot is already empty !");
        }
        this.vehicle=null;
        this.isOccupied=false;
    }

    public int getSpotNumber(){
        return spotNumber;
    }
    public String getSpotType(){
        return spotType;
    }
    public Vehicle getVehicle(){
        return vehicle;
    }
    

    @Override
public String toString() {
    return "ParkingSpot{" +
            "spotNumber=" + spotNumber +
            ", isOccupied=" + isOccupied +
            ", vehicle=" + (vehicle != null ? vehicle.getLicencePlate() : "None") +
            '}';
}



    

    
}
