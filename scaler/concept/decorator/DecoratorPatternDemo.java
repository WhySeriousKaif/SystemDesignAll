package scaler.concept.decorator;

public class DecoratorPatternDemo {
    public static void main(String[] args) {

        // Basic
        Pizza p1 = new BasicPizza();

        // Cheese
        Pizza p2 = new CheeseDecorator(new BasicPizza());

        // Cheese + Veggies
        Pizza p3 = new VeggiesDecorator(
                        new CheeseDecorator(
                            new BasicPizza()
                        )
                   );

        System.out.println(p1.getDescription() + " → $" + p1.getCost());
        System.out.println(p2.getDescription() + " → $" + p2.getCost());
        System.out.println(p3.getDescription() + " → $" + p3.getCost());
    }

    interface Pizza {
        String getDescription();
        double getCost();
    }

    static class BasicPizza implements Pizza {
        public String getDescription() {
            return "Basic Pizza";
        }

        public double getCost() {
            return 5.0;
        }
    }

    static abstract class PizzaDecorator implements Pizza {
        protected Pizza pizza; // HAS-A

        public PizzaDecorator(Pizza pizza) {
            this.pizza = pizza;
        }

        public String getDescription() {
            return pizza.getDescription(); // delegation
        }

        public double getCost() {
            return pizza.getCost();
        }
    }

    static class CheeseDecorator extends PizzaDecorator {
        public CheeseDecorator(Pizza pizza) {
            super(pizza);
        }

        public String getDescription() {
            return pizza.getDescription() + ", Cheese";
        }

        public double getCost() {
            return pizza.getCost() + 1.5;
        }
    }

    static class VeggiesDecorator extends PizzaDecorator {
        public VeggiesDecorator(Pizza pizza) {
            super(pizza);
        }

        public String getDescription() {
            return pizza.getDescription() + ", Veggies";
        }

        public double getCost() {
            return pizza.getCost() + 0.5;
        }
    }
}
