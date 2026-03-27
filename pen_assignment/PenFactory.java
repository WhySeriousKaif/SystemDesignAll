public class PenFactory {
    public static WritingImplement getPen(String type, String color, boolean isClickable) {
        if (type.equalsIgnoreCase("PENCIL")) {
            return new Pencil(); 
        }
        
        StartStrategy startStrategy = isClickable ? new ClickStrategy() : new CapStrategy();
        RefillStrategy refillStrategy;
        
        if (type.equalsIgnoreCase("FOUNTAIN")) {
            refillStrategy = new InkBottleStrategy();
            return new FountainPen(startStrategy, refillStrategy, color);
        } else if (type.equalsIgnoreCase("GEL")) {
            refillStrategy = new InternalCartridgeStrategy();
            return new GelPen(startStrategy, refillStrategy, color);
        } else if (type.equalsIgnoreCase("BALLPOINT")) {
            refillStrategy = new InternalCartridgeStrategy();
            return new BallpointPen(startStrategy, refillStrategy, color);
        }
        
        throw new IllegalArgumentException("Unknown writing implement type: " + type);
    }
}
