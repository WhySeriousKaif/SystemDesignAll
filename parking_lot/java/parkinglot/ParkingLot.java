package parking_lot.java.parkinglot;

import java.util.List;

import parking_lot.java.vehicles.Vehicle;

public class ParkingLot {
    private List<ParkingFloor>floors;

    public ParkingLot(List<ParkingFloor>floors){
        this.floors=floors;
    }

    //Method to find an available spot accross all floors based on vehicle type
    public ParkingSpot findAvailableSpot(String vehicleType){
        for(ParkingFloor floor:floors){
            ParkingSpot spot=floor.findAvailableSpot(vehicleType);
            if(spot!=null){
                return spot;
            }
        }
        return null;
    }

    public ParkingSpot parkVehicle(Vehicle vehicle){
        ParkingSpot spot=findAvailableSpot(vehicle.getVehicleType());
        if(spot!=null){
            spot.parkVehicle(vehicle);
            return spot;
        }
        return null;
    }

    public void vacateSpot(ParkingSpot spot,Vehicle vehicle){
        if(spot!=null && spot.getVehicle().equals(vehicle)){
            spot.vacate();
            System.out.println("Spot " + spot.getSpotNumber() + " vacated by vehicle " + vehicle.getLicencePlate());
        }
        else{
            System.out.println("Invalid spot or vehicle");
        }
    }
    public ParkingSpot getSpotByNumber(int spotNumber){
        for(ParkingFloor floor:floors){
            for(ParkingSpot spot:floor.getParkingSpots()){
                if(spot.getSpotNumber()==spotNumber){
                    return spot;
                }
            }
        }
        return null;
    }

    public List<ParkingFloor> getFloors(){
        return floors;
    }


    
}
