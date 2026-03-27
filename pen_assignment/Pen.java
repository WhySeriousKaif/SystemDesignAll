public abstract class Pen implements WritingImplement {
    protected StartStrategy startStrategy;
    protected RefillStrategy refillStrategy;
    protected String color;

    public Pen(StartStrategy startStrategy, RefillStrategy refillStrategy, String color) {
        this.startStrategy = startStrategy;
        this.refillStrategy = refillStrategy;
        this.color = color;
    }

    public void start() {
        startStrategy.start();
    }

    public void close() {
        startStrategy.close();
    }

    public void refill() {
        refillStrategy.refill();
    }

    @Override
    public abstract void write(String text);
}
