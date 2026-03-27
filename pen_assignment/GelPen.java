public class GelPen extends Pen {
    public GelPen(StartStrategy startStrategy, RefillStrategy refillStrategy, String color) {
        super(startStrategy, refillStrategy, color);
    }

    @Override
    public void write(String text) {
        System.out.println("Writing smoothly in " + color + " with a Gel Pen: " + text);
    }
}
