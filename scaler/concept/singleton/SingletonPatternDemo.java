package scaler.concept.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton Pattern Demonstration
 * Concept: Thread-Safe Config Manager (Double-Checked Locking)
 */
public class SingletonPatternDemo {

    public static void main(String[] args) {
        System.out.println("--- Singleton Pattern Demo ---\n");

        // 1. Thread 1 gets instance
        ConfigManager config1 = ConfigManager.getInstance();
        config1.addSetting("DB_URL", "jdbc:mysql://localhost:3306/scaler");

        // 2. Thread 2 (or another part of code) gets the SAME instance
        ConfigManager config2 = ConfigManager.getInstance();
        
        System.out.println("Config 1 Hash: " + config1.hashCode());
        System.out.println("Config 2 Hash: " + config2.hashCode());

        // 3. Verification
        if (config1 == config2) {
            System.out.println("\n✅ Success: Both references point to the SAME instance.");
        }

        System.out.println("Retrieved Setting from config2: " + config2.getSetting("DB_URL"));
    }

    // --- CONCRETE SINGLETON ---
    static class ConfigManager {
        /**
         * 'volatile' is CRITICAL here. 
         * It ensures that multiple threads handle the 'instance' variable correctly 
         * when it is being initialized by one of the threads.
         */
        private static volatile ConfigManager instance;
        private Map<String, String> settings;

        // Private constructor prevents instantiation from other classes
        private ConfigManager() {
            System.out.println("[INIT] ConfigManager initialized (Expensive operation).");
            settings = new HashMap<>();
        }

        /**
         * Double-Checked Locking for performance and thread-safety
         */
        public static ConfigManager getInstance() {
            if (instance == null) { // First check: optimization
                synchronized (ConfigManager.class) { // Synchronize on the class
                    if (instance == null) { // Second check: atomic creation
                        instance = new ConfigManager();
                    }
                }
            }
            return instance;
        }

        public void addSetting(String key, String value) {
            settings.put(key, value);
        }

        public String getSetting(String key) {
            return settings.get(key);
        }
    }
}
