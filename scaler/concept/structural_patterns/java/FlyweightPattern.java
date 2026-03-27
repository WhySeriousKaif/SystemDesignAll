package scaler.concept.structural_patterns.java;

import java.util.HashMap;
import java.util.Map;

// 7. Flyweight Pattern
// Purpose: Reduce memory by sharing data among similar objects.

interface Robot {
    void display(double x, double y); // Extrinsic data
}

class HumanoidRobot implements Robot {
    private final String type = "Humanoid";
    private final String sprite; // Intrinsic (Simulated heavy object)
    
    public HumanoidRobot(String sprite) {
        this.sprite = sprite;
        System.out.println("🤖 [Humanoid] Creating new object with sprite: " + sprite);
    }
    
    @Override
    public void display(double x, double y) {
        System.out.println("Displaying " + type + " at (" + x + "," + y + ")");
    }
}

class RobotFactory {
    private static final Map<String, Robot> cache = new HashMap<>();
    
    public static Robot createRobot(String type) {
        if (cache.containsKey(type)) {
            return cache.get(type);
        }
        
        Robot robot;
        if (type.equalsIgnoreCase("Humanoid")) {
            robot = new HumanoidRobot("Humanoid_Sprite_V1");
        } else {
            robot = (x, y) -> System.out.println("Displaying generic robot at (" + x + "," + y + ")");
        }
        
        cache.put(type, robot);
        return robot;
    }
}

public class FlyweightPattern {
    public static void main(String[] args) {
        System.out.println("--- Flyweight Pattern (Robot Game) ---");
        
        // Create 5 robots, but only 1 unique object is created
        for (int i = 0; i < 5; i++) {
            Robot robot = RobotFactory.createRobot("Humanoid");
            robot.display(i * 10, i * 20);
        }
        
        System.out.println("\n✅ [Flyweight] Reused the same object for all 5 displays.");
    }
}
