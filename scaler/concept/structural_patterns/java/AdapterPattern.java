package scaler.concept.structural_patterns.java;

// 4. Adapter Pattern
// Purpose: Bridge incompatible interfaces.

interface WeightMachine {
    double getWeightInPounds();
}

class WeightMachineImpl implements WeightMachine {
    @Override
    public double getWeightInPounds() {
        return 30.0; // Hardcoded example
    }
}

interface WeightMachineAdapter {
    double getWeightInKG();
}

class KGAdapterImpl implements WeightMachineAdapter {
    private WeightMachine machine;
    
    public KGAdapterImpl(WeightMachine machine) {
        this.machine = machine;
    }
    
    @Override
    public double getWeightInKG() {
        double pounds = machine.getWeightInPounds();
        return pounds * 0.453592; // Conversion factor
    }
}

public class AdapterPattern {
    public static void main(String[] args) {
        System.out.println("--- Adapter Pattern (Pounds to KG) ---");
        WeightMachine machine = new WeightMachineImpl();
        WeightMachineAdapter adapter = new KGAdapterImpl(machine);
        
        System.out.println("Weight in Pounds: " + machine.getWeightInPounds() + " lbs");
        System.out.println("Weight in KG: " + adapter.getWeightInKG() + " kg");
    }
}
