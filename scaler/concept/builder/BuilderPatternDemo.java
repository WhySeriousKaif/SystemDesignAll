package scaler.concept.builder;

/**
 * Builder Design Pattern Demonstration
 * Concept: Complex Object Construction (User Profile)
 */
public class BuilderPatternDemo {

    public static void main(String[] args) {
        System.out.println("--- Builder Pattern Demo ---\n");

        // 1. Building a full user profile
        User user1 = new User.UserBuilder("John", "Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .age(30)
                .build();
        
        System.out.println("User 1: " + user1);

        // 2. Building a minimal user profile (skipping optional fields)
        User user2 = new User.UserBuilder("Jane", "Smith")
                .build();
        
        System.out.println("User 2: " + user2);

        // 3. Testing Validation
        try {
            System.out.println("\nTesting invalid age...");
            new User.UserBuilder("Error", "User").age(-5).build();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Expected Error: " + e.getMessage());
        }
        
        System.out.println("\n✅ Success: Clean, readable, and immutable object construction achieved.");
    }

    // --- CONCRETE PRODUCT ---
    public static final class User {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phone;
        private final int age;

        // Private constructor - only accessible via Builder
        private User(UserBuilder builder) {
            this.firstName = builder.firstName;
            this.lastName = builder.lastName;
            this.email = builder.email;
            this.phone = builder.phone;
            this.age = builder.age;
        }

        @Override
        public String toString() {
            return String.format("User [Name=%s %s, Email=%s, Phone=%s, Age=%d]", 
                                firstName, lastName, email, phone, age);
        }

        // --- STATIC INNER BUILDER ---
        public static class UserBuilder {
            private final String firstName; // Mandatory
            private final String lastName;  // Mandatory
            private String email = "N/A";     // Optional
            private String phone = "N/A";     // Optional
            private int age = 0;              // Optional

            public UserBuilder(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
            }

            public UserBuilder email(String email) {
                this.email = email;
                return this; // Return this for chaining
            }

            public UserBuilder phone(String phone) {
                this.phone = phone;
                return this;
            }

            public UserBuilder age(int age) {
                this.age = age;
                return this;
            }

            public User build() {
                // Perform across-field validation here
                if (age < 0) {
                    throw new IllegalArgumentException("Age cannot be negative");
                }
                return new User(this);
            }
        }
    }
}
