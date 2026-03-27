package snake_and_ladder_assignment;

import java.util.Random;

public class Dice {
    private Random random = new Random();
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
