public class BallpointPen extends Pen {
    public BallpointPen(StartStrategy startStrategy, RefillStrategy refillStrategy, String color) {
        super(startStrategy, refillStrategy, color);
    }

    @Override
    public void write(String text) {
        System.out.println("Writing in " + color + " with a Ballpoint Pen: " + text);
    }
}
