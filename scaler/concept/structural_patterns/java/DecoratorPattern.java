package scaler.concept.structural_patterns.java;

// 1. Decorator Pattern
// Purpose: Add functionality to existing objects without changing their structure.

abstract class BasePizza {
    public abstract int cost();
}

class FarmhousePizza extends BasePizza {
    @Override
    public int cost() { return 200; }
}

class MargheritaPizza extends BasePizza {
    @Override
    public int cost() { return 100; }
}

abstract class ToppingDecorator extends BasePizza {
    protected BasePizza basePizza;
    
    public ToppingDecorator(BasePizza basePizza) {
        this.basePizza = basePizza;
    }
}

class ExtraCheese extends ToppingDecorator {
    public ExtraCheese(BasePizza basePizza) {
        super(basePizza);
    }
    
    @Override
    public int cost() {
        return basePizza.cost() + 10;
    }
}

class Mushroom extends ToppingDecorator {
    public Mushroom(BasePizza basePizza) {
        super(basePizza);
    }
    
    @Override
    public int cost() {
        return basePizza.cost() + 15;
    }
}

public class DecoratorPattern {
    public static void main(String[] args) {
        System.out.println("--- Decorator Pattern (Pizza) ---");
        BasePizza pizza = new FarmhousePizza();
        pizza = new ExtraCheese(pizza);  // 200 + 10 = 210
        pizza = new Mushroom(pizza);     // 210 + 15 = 225
        System.out.println("Final Pizza Cost: $" + pizza.cost());
    }
}
