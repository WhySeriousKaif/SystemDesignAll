package scaler.concept.solid;



/**
 * HR System Demonstration: Before vs After SOLID Principles
 */
public class HRSystemDemo {

    public static void main(String[] args) {
        System.out.println("==== BEFORE SOLID (Tight Coupling) ====");
        demonstrateBefore();

        System.out.println("\n==== AFTER SOLID (Loose Coupling) ====");
        demonstrateAfter();
    }

    // --- BEFORE VERSION ---

    static class LegacyEmployee {
        private String name;

        public LegacyEmployee(String name) { this.name = name; }

        // Violation: SRP - Employee is saving itself
        // Violation: OCP - Hardcoded to file storage
        public void saveToDisk() {
            System.out.println("Legacy: Saving " + name + " to local disk (C:/temp/employee.txt)");
        }
    }

    private static void demonstrateBefore() {
        LegacyEmployee emp = new LegacyEmployee("John Doe");
        emp.saveToDisk();
    }

    // --- AFTER VERSION ---

    // 1. Employee: Single Responsibility (Data only)
    static class Employee {
        private String name;
        public Employee(String name) { this.name = name; }
        public String getName() { return name; }
    }

    // 2. Abstraction: Dependency Inversion
    interface IStorageService {
        void save(Employee e);
    }

    // 3. Concrete implementations: Open/Closed Principle
    static class FileStorage implements IStorageService {
        @Override
        public void save(Employee e) {
            System.out.println("Final: Saving " + e.getName() + " to File.");
        }
    }

    static class DatabaseStorage implements IStorageService {
        @Override
        public void save(Employee e) {
            System.out.println("Final: Saving " + e.getName() + " to MongoDB.");
        }
    }

    // 4. Repository: Dependency Inversion (Injection)
    static class EmployeeRepository {
        private final IStorageService storage;

        public EmployeeRepository(IStorageService storage) {
            this.storage = storage; // Injecting the dependency
        }

        public void executeSave(Employee e) {
            storage.save(e);
        }
    }

    private static void demonstrateAfter() {
        Employee emp = new Employee("Jane Smith");

        // Scenario A: Use File
        IStorageService fileMethod = new FileStorage();
        EmployeeRepository repoFile = new EmployeeRepository(fileMethod);
        repoFile.executeSave(emp);

        // Scenario B: Use Database (No code change in Repository!)
        IStorageService dbMethod = new DatabaseStorage();
        EmployeeRepository repoDb = new EmployeeRepository(dbMethod);
        repoDb.executeSave(emp);
    }
}
