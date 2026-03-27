package com.scaler.concept.structural_patterns;

// 6. Facade Pattern
// Purpose: Hide system complexity from client by providing a simplified interface.

class InternalUnit {
    public void startInternal() {
        System.out.println("🌀 [InternalUnit] Starting fan and blower...");
    }
}

class ExternalUnit {
    public void checkVoltage() {
        System.out.println("⚡ [ExternalUnit] Checking voltage stability...");
    }
    public void startCondenser() {
        System.out.println("❄️ [ExternalUnit] Starting condenser and compressor...");
    }
}

class ACFacade {
    private InternalUnit internal;
    private ExternalUnit external;
    
    public ACFacade() {
        this.internal = new InternalUnit();
        this.external = new ExternalUnit();
    }
    
    public void turnOnAC() {
        System.out.println("--- Facade: Processing 'Turn On AC' Command ---");
        external.checkVoltage();
        external.startCondenser();
        internal.startInternal();
        System.out.println("✅ [Facade] AC is now ON and cooling.");
    }
}

public class FacadePattern {
    public static void main(String[] args) {
        System.out.println("--- Facade Pattern (AC System) ---");
        ACFacade ac = new ACFacade();
        ac.turnOnAC(); // Client only calls one simple method
    }
}
