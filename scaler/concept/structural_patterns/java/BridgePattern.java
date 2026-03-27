package scaler.concept.structural_patterns.java;

// 5. Bridge Pattern
// Purpose: Decouple abstraction from implementation so both vary independently.

interface BreatheImplementer {
    void breatheProcess();
}

class LandBreathe implements BreatheImplementer {
    @Override
    public void breatheProcess() {
        System.out.println("👃 [LandBreathe] Breathe through nose: O2 in, CO2 out");
    }
}

class WaterBreathe implements BreatheImplementer {
    @Override
    public void breatheProcess() {
        System.out.println("🐟 [WaterBreathe] Breathe through gills: O2 in, CO2 out");
    }
}

abstract class LivingThing {
    protected BreatheImplementer breatheImpl;
    
    public LivingThing(BreatheImplementer breatheImpl) {
        this.breatheImpl = breatheImpl;
    }
    
    public abstract void breathe();
}

class Dog extends LivingThing {
    public Dog() {
        super(new LandBreathe());
    }
    
    @Override
    public void breathe() {
        breatheImpl.breatheProcess();
    }
}

class Fish extends LivingThing {
    public Fish() {
        super(new WaterBreathe());
    }
    
    @Override
    public void breathe() {
        breatheImpl.breatheProcess();
    }
}

public class BridgePattern {
    public static void main(String[] args) {
        System.out.println("--- Bridge Pattern (Living Things) ---");
        LivingThing dog = new Dog();
        dog.breathe(); // LandBreathe
        
        LivingThing fish = new Fish();
        fish.breathe(); // WaterBreathe
    }
}
