public interface StartStrategy {
    void start();
    void close();
}

class CapStrategy implements StartStrategy {
    @Override
    public void start() {
        System.out.println("Removing cap from the pen.");
    }

    @Override
    public void close() {
        System.out.println("Putting the cap back on the pen.");
    }
}

class ClickStrategy implements StartStrategy {
    @Override
    public void start() {
        System.out.println("Clicking to expose the pen tip.");
    }

    @Override
    public void close() {
        System.out.println("Clicking to retract the pen tip.");
    }
}
