package parking_lot.java.parkinglot;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
    private int floorNumber;

    private List<ParkingSpot> spots;
    public ParkingFloor(int floorNumber,int numOfCarSpots,int numOfBikeSpots){
        this.floorNumber=floorNumber;
        this.spots=new ArrayList<>();

        //Add spots for cars
        for(int i=1;i<=numOfCarSpots;i++){
            spots.add(new CarParkingSpot(i));
        }
        //Add spots for bikes
        for(int i=1;i<=numOfBikeSpots;i++){
            spots.add(new BikeParkingSpot(i));
        }
        
    }

    public ParkingSpot findAvailableSpot(String vehicleType){
        for(ParkingSpot spot: spots){
            if(!spot.isOccupied() && spot.getSpotType().equalsIgnoreCase(vehicleType)){
                return spot; //return the first availabe spot for the vehicle 
            }
        }
        return null; //no spot available for given vehicle type

    }

    public List<ParkingSpot> getParkingSpots(){
        return spots;
    }

    

    


    
}
