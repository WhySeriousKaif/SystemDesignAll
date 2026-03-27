public class FountainPen extends Pen {
    public FountainPen(StartStrategy startStrategy, RefillStrategy refillStrategy, String color) {
        super(startStrategy, refillStrategy, color);
    }

    @Override
    public void write(String text) {
        System.out.println("Writing elegantly in " + color + " with a Fountain Pen: " + text);
    }
}
