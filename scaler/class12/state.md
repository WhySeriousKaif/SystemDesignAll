# State Design Pattern - Comprehensive Notes (Java Implementation)

## 1. Introduction to State Pattern
The **State Pattern** is a **Behavioral Design Pattern** that allows an object to change its behavior based on its **internal state**. The same object performs **different functionalities** based on its current state.  

**Key Benefits:**
- Eliminates complex if-else/switch statements
- Follows **Single Responsibility Principle** (SRP)
- Follows **Open-Closed Principle** (OCP) - easy to add new states   

## 2. Real-World Example: Document Workflow
Consider a document system (like Google Docs/Microsoft Word) with **3 states**:
- **Draft**: Editing phase, not visible to others  
- **Moderation**: Submitted for admin review  
- **Published**: Final state, publicly visible  

**Behavior varies by state:**
| State | Publish Action | Edit Permission |
|-------|----------------|---------------|
| Draft | Author → Moderation | Author only   |
| Moderation | Admin → Published | Admin only   |
| Published | Invalid operation | No one   |

## 3. Problem with Traditional Approach
**Without State Pattern:**
```java
public class Document {
    private String state; // "DRAFT", "MODERATION", "PUBLISHED"
    
    public void publish(String currentUser) {
        if ("DRAFT".equals(state)) {
            if ("AUTHOR".equals(currentUser)) {
                state = "MODERATION";
                System.out.println("Document sent to admin for review");
            } else {
                System.out.println("Cannot publish in DRAFT state. Only AUTHOR can publish.");
            }
        } else if ("MODERATION".equals(state)) {
            if ("ADMIN".equals(currentUser)) {
                state = "PUBLISHED";
                System.out.println("Document published successfully");
            } else {
                System.out.println("Cannot publish in MODERATION state. Only ADMIN can publish.");
            }
        } else if ("PUBLISHED".equals(state)) {
            System.out.println("Invalid operation. Document already published.");
        }
    }
    
    public void edit(String currentUser) {
        // Similar complex if-else logic repeated here  
    }
}
```

**Problems:**
- **Violates SRP**: Single method handles multiple responsibilities  
- **Violates OCP**: Adding new states requires modifying existing code  
- **Code becomes unmaintainable** as conditions grow   

## 4. State Pattern Solution (Java Implementation)

### 4.1 State Interface
```java
public interface IState {
    void handlePublish(DocumentContext context, String currentUser);
    void handleEdit(DocumentContext context, String currentUser);
    String getName();  
}
```

### 4.2 Context Class (DocumentContext)
```java
public class DocumentContext {
    private IState state;
    
    public DocumentContext() {
        this.state = new DraftState(); // Initial state  
    }
    
    public void setState(IState state) {
        this.state = state;
        System.out.println("Transitioned to: " + state.getName());  
    }
    
    public void publish(String currentUser) {
        state.handlePublish(this, currentUser);  
    }
    
    public void edit(String currentUser) {
        state.handleEdit(this, currentUser);  
    }
    
    public IState getState() {
        return state;
    }
}
```

### 4.3 Concrete State Implementations

**DraftState.java**
```java
public class DraftState implements IState {
    private DocumentContext context;
    
    public DraftState() {} // Can accept context in constructor  
    
    @Override
    public void handlePublish(DocumentContext context, String currentUser) {
        if ("AUTHOR".equals(currentUser)) {
            context.setState(new ModerationState());
        } else {
            System.out.println("Cannot publish in DRAFT state. Only AUTHOR can publish.");
        }  
    }
    
    @Override
    public void handleEdit(DocumentContext context, String currentUser) {
        if ("AUTHOR".equals(currentUser)) {
            System.out.println("Document edited successfully.");
            // Actual editing logic here
        } else {
            System.out.println("Cannot edit in DRAFT state. Only AUTHOR can edit.");
        }
    }
    
    @Override
    public String getName() {
        return "Draft";
    }
}
```

**ModerationState.java**
```java
public class ModerationState implements IState {
    @Override
    public void handlePublish(DocumentContext context, String currentUser) {
        if ("ADMIN".equals(currentUser)) {
            context.setState(new PublishedState());  
        } else {
            System.out.println("Cannot publish in MODERATION state. Only ADMIN can publish.");
        }
    }
    
    @Override
    public void handleEdit(DocumentContext context, String currentUser) {
        if ("ADMIN".equals(currentUser)) {
            System.out.println("Admin edited the document.");
        } else {
            System.out.println("Cannot edit in MODERATION state. Only ADMIN can edit.");
        }
    }
    
    @Override
    public String getName() {
        return "Moderation";
    }
}
```

**PublishedState.java**
```java
public class PublishedState implements IState {
    @Override
    public void handlePublish(DocumentContext context, String currentUser) {
        System.out.println("Invalid operation. Document already published.");  
    }
    
    @Override
    public void handleEdit(DocumentContext context, String currentUser) {
        System.out.println("Cannot edit published document.");
    }
    
    @Override
    public String getName() {
        return "Published";
    }
}
```

### 4.4 Usage Example
```java
public class StatePatternDemo {
    public static void main(String[] args) {
        DocumentContext doc = new DocumentContext();
        
        // Draft state
        doc.publish("AUTHOR");    // → Moderation
        doc.edit("AUTHOR");       // → Edited
        
        // Moderation state
        doc.publish("ADMIN");     // → Published
        doc.publish("AUTHOR");    // → Error
        
        // Published state
        doc.edit("ADMIN");        // → Error  
    }
}
```

## 5. Key Advantages
1. **Single Responsibility**: Each state class handles one state logic  
2. **Open-Closed Principle**: Add new states without modifying existing code  
3. **Maintainable**: Bugs fixed in specific state classes  
4. **Clean Context**: Context delegates to states  

## 6. Class Diagram
```
+----------------+       +-----------------+
| DocumentContext|<>---->|     IState      |
+----------------+       +-----------------+
| - state        |       | + handlePublish()|
| + publish()    |       | + handleEdit()  |
| + edit()       |       | + getName()     |
+----------------+       +-----------------+
         ^                        ^  ^  ^
         |                        |  |  |
    +------------+   +------------+  |  +-----------------+
    | DraftState |   |ModerationState|  | PublishedState  |
    +------------+   +---------------+  +-----------------+
```