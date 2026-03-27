public interface RefillStrategy {
    void refill();
}

class InternalCartridgeStrategy implements RefillStrategy {
    @Override
    public void refill() {
        System.out.println("Replacing the internal ink cartridge.");
    }
}

class InkBottleStrategy implements RefillStrategy {
    @Override
    public void refill() {
        System.out.println("Refilling the pen using an ink bottle.");
    }
}
