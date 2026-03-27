Seller Serach service - snapdeal 
Buyer side -> product matching a details u provide
seller side -> product matching a unique code or given product type,no of seller selling such types of product so we have different class which is providing search features to sellers

inside Seller Serach Service --> some fucntionality like
1. getSellerBySKU(sku) //sku for specific product type
2. getSellerBySUPC(supc) //supc for specific item in the storage

another service Seller Ranking Service it is the client of seller search service

// Dependency Injection is voilated here 
class SellerRankingService {

    void rankingFunc(sku){ // we need to class the api of getSellerBySKU
        SellerSerachService sss = new SellerSerachService();
        // we are creating a concrete class in another concrete class  both are tightly coupled here 
        // dependency should be on abstraction 
        // SellerServiceSearch should be interface
        sss.getSellerBySKU(sku);
        

    }
    
}
//correct

interface ISellerSearchService{
    getSellerBySKU(sku);
    getSellerBySUPC(supc);
}

class SnapdealSellerSearchService implements ISellerSearchService{
    getSellerBySKU(sku){
        ....
    }
    getSellerBySUPC(supc){
        ....
        
    }
}
class SellerRankingService{
    ISellerSearchService sellerSearchService;
    
    SellerRankingService(ISellerSearchService sellerSearchService){
        this.sellerSearchService = sellerSearchService;
    }
    void rankingFunc(sku){
       List<Seller> list = sellerSearchService.getSellerBySKU(sku);
    }
}

//Exclusively 

we aslo have MerchantSearchService which has getMerchantBySKU(sku)
how do we ensure 
void rankingFunc(sku){
       List<Seller> list = sellerSearchService.getSellerBySKU(sku);
 }
 this works for MerchantSearchService as well
 Snapdeal had a SellerSearchService with methods like getSellersBySKU.
Exclusively had a MerchantSearchService with similar functionality but different method names like getMerchantsBySKU.
The challenge was to make the Exclusively service work with Snapdeal's existing SellerRankingService, which expected an object implementing the ISellerService interface.
The problem was how to adapt the Exclusively MerchantSearchService to work with Snapdeal's existing code without modifying either codebase significantly. This led to a discussion of the Adapter design pattern as a solution.

we have SellerRankingService (Client) --> we can not this code
we cannot ask the client to do extra work like if else cases


we have SellerSearchService which is an Interface inside this we have some bunch of methods getSellersBySKU(sku)
we have SnapdealSellerSearchService which is implementing this interface so that we can follow the dependency inversion principle

now we have MerchantSearchService has very silimar class to SnapdealSellerSearchService but it is exclusively merchat search service so responsibility is similar but name is different 
we should have a way to inject MerchantSearchService in SellerRankingService so that the client code should work for both  

one way is to implement the interface in merchant search service and then we can inject it in SellerRankingService but this is not a good way 

The instructor presented the Adapter design pattern as the solution to this problem. Here's a summary of the solution:

Create a new wrapper class, let's call it ExclusivelyMerchantServiceWrapper.

This wrapper class implements the ISellerService interface that Snapdeal's SellerRankingService expects.

Inside the wrapper class, keep an object of the ExclusivelyMerchantService.

Implement all the methods of the ISellerService interface in the wrapper class.

In each implemented method, call the corresponding method of the ExclusivelyMerchantService object, translating between the two different interfaces.

For example:

public class ExclusivelyMerchantServiceWrapper implements ISellerService {
    private ExclusivelyMerchantService merchantService;

    public ExclusivelyMerchantServiceWrapper(ExclusivelyMerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Override
    public List<Seller> getSellersBySKU(String sku) {
        // Call the Exclusively method and convert the result if necessary
        return merchantService.getMerchantsBySKU(sku);
    }

    // Implement other methods similarly...
}
This solution allows the Exclusively service to be used with Snapdeal's existing code without modifying either original codebase. It acts as an "adapter" between the two different interfaces.

The instructor emphasized that this approach:

Doesn't violate the Open-Closed Principle as we're not modifying existing code.
Allows for integration of the two systems without deep knowledge of Exclusively's codebase.
Provides a clean way to bridge the gap between two similar but differently implemented services.
Assistant
You've raised a good point. The instructor did mention that the wrapper class (adapter) would have a hard dependency on the ExclusivelyMerchantService concrete class. Let me explain why this is considered acceptable in this specific case:

Specific Purpose: The wrapper (adapter) is specifically designed to work with the ExclusivelyMerchantService. It's not meant to be a generic adapter for any merchant service.

Tight Coupling Necessity: The adapter needs to know the exact method names and signatures of the ExclusivelyMerchantService to perform its translation function correctly.

Limited Scope: This adapter is solving a specific integration problem between two existing systems. It's not designed for flexibility beyond this use case.

Pragmatic Approach: In real-world scenarios, sometimes we need to make pragmatic decisions that balance ideal design principles with practical constraints.

Encapsulation of Complexity: While the adapter has a hard dependency, it encapsulates this complexity, allowing the rest of the Snapdeal system to remain loosely coupled.

The instructor acknowledged this point, saying:

"Correct, we are going to make an object of a concrete class, not of an interface, which means that the dependency inversion will be violated. And that is fine. Why? Because this wrapper class that we are writing, it is specific to the exclusively merchant service class. This is going to have a hard dependency and which is also expected because we do not want to provide a flexibility here."

In essence, while it does create a hard dependency, this approach solves the immediate problem without requiring changes to either existing system, which was the main goal in this scenario.
Assistant
Certainly! The instructor also discussed the Proxy design pattern in the class. Here's a summary of the key points about the Proxy pattern:

Definition: A proxy is a wrapper or agent object that is being called by the client to access the real serving object behind the scenes.

Purpose: The main purposes of a proxy can include:

Providing a layer of security
Adding extra logic (like retrying operations)
Handling exceptions
Lazy initialization of resources
Similarity to Adapter: The instructor noted that a proxy is similar to an adapter in that both are wrapper classes, but they serve different purposes.

Use Cases: Some examples where proxies are useful include:

When you need to control access to a resource
When you want to add behaviors before or after the main object's operations
When you need to manage the lifecycle of the real object
Implementation: A proxy typically implements the same interface as the real object it's proxying for, allowing it to be used transparently in place of the real object.

Difference from Adapter: While an adapter is used to make incompatible interfaces work together, a proxy provides a surrogate for another object to control access to it.

The instructor emphasized that proxies are commonly used in large-scale applications, especially when dealing with resource-intensive operations or when you need to add a layer of control or functionality without modifying the original object.
The instructor is discussing the Strategy design pattern in this part of the transcript. Here's a summary of what he's saying:

Problem: The instructor presents a scenario where they need to implement a custom List class (MyList) that has different sorting and printing behaviors.

Initial Approach: They initially tried to solve this using inheritance, creating subclasses for different behaviors (e.g., VerticalList, HorizontalList).

Issue with Inheritance: This approach became problematic when they needed different combinations of sorting and printing behaviors, leading to a proliferation of subclasses.

Strategy Pattern Solution: Instead of using inheritance, the instructor suggests using composition and the Strategy pattern:

Create separate interfaces for sorting (ISortAlgorithm) and printing (IPrintAlgorithm).
Implement different concrete strategies for sorting (e.g., CountSort, TimSort) and printing (e.g., VerticalPrint, HorizontalPrint).
In the MyList class, have attributes for sortAlgorithm and printAlgorithm of these interface types.
Inject these strategies through the constructor or setter methods.
Benefits:
Flexibility to change algorithms at runtime
Adheres to the Open-Closed Principle
Solves the Single Responsibility Principle issue
Real-world examples: The instructor mentions examples like payment gateways in e-commerce and route finding in Google Maps as practical applications of the Strategy pattern.
The key takeaway is that the Strategy pattern allows for flexible, interchangeable algorithms without tightly coupling the client code to specific impl

Great question! Let's break down the issues with the first solution/design:

Violation of Single Responsibility Principle:
The initial MyList class was handling multiple responsibilities: adding elements, sorting, and printing.
This made the class less maintainable and more prone to changes for multiple reasons.
Lack of Flexibility:
The sorting and printing behaviors were hardcoded into the class.
Changing these behaviors would require modifying the MyList class itself, violating the Open-Closed Principle.
Inheritance Limitations:
When they tried to solve this using inheritance (e.g., VerticalList, HorizontalList), it led to a new problem.
Different clients needed different combinations of sorting and printing behaviors.
This would result in an explosion of subclasses to cover all possible combinations (e.g., VerticalCountSortList, HorizontalTimSortList, etc.).
Rigid Design:
The inheritance approach made it difficult to change behaviors at runtime.
Each subclass would have fixed sorting and printing behaviors.
Code Duplication:
Similar code for sorting or printing might need to be duplicated across different subclasses.
Difficulty in Adding New Behaviors:
Adding a new sorting or printing algorithm would require creating new subclasses for each combination with existing behaviors.
These issues made the initial design inflexible and hard to maintain, especially as requirements changed or new behaviors needed to be added. The Strategy pattern was introduced as a solution to these problems, allowing for more flexible and maintainable code.

Today 3:44 AM
Type or record to view the transcript
code

Assistant
I apologize for the confusion. You're right, I made a mistake in my previous explanation. Let me clarify the solution the instructor actually presented for the MyList interface:

Create an Abstract Class or Interface:
Create a MyList abstract class or interface with methods like add(), sort(), and printList().
Create Concrete Implementations:
Implement concrete classes that extend MyList, such as VerticalList and HorizontalList.
These classes would implement the printList() method differently.
Use Strategy Pattern for Sorting:
Create an ISortAlgorithm interface.
Implement concrete sorting algorithms like CountSort and TimSort.
In the MyList abstract class or concrete implementations, have a sortAlgorithm attribute: java protected ISortAlgorithm sortAlgorithm;
Implement the sort() method in MyList to use this algorithm: java public void sort() { sortAlgorithm.sort(this.list); }
Allow Dynamic Algorithm Change:
Provide a method to change the sorting algorithm: java public void setSortAlgorithm(ISortAlgorithm sortAlgorithm) { this.sortAlgorithm = sortAlgorithm; }
Client Usage:
   MyList verticalList = new VerticalList(new CountSort());
   MyList horizontalList = new HorizontalList(new TimSort());

   // Later, if needed:
   verticalList.setSortAlgorithm(new TimSort());
This solution combines inheritance for the printing behavior (vertical vs horizontal) with the Strategy pattern for the sorting behavior. It allows for flexibility in sorting algorithms while maintaining distinct list types for different printing behaviors.

I apologize again for the earlier confusion, and thank you for the opportunity to clarify.

Today 3:46 AM

Assistant
I apologize for the confusion. You're right to point this out. Let me clarify the specific scenario the instructor presented:

Client Requirements:
Client 1 wanted a vertical print and mentioned their data would only contain 0s, 1s, and 2s.
Client 2 wanted a horizontal print and could have any integer values.
Initial Solution: The instructor first suggested creating two classes:
VerticalList (with CountSort for 0, 1, 2 data)
HorizontalList (with TimSort for any integer data)
New Requirement: A third client (Client 3) came with a new requirement:
Wanted horizontal print
But their data would only be binary (0s and 1s)
Problem Identified: The instructor pointed out that the initial design based on inheritance (VerticalList, HorizontalList) became problematic because:
It coupled the printing behavior (vertical/horizontal) with the sorting algorithm (CountSort/TimSort)
It didn't allow for easy combination of different printing and sorting behaviors
Realization: The instructor realized that segregating classes based on a single feature (like printing behavior) wasn't flexible enough to handle various combinations of printing and sorting requirements.
This scenario led to the discussion of the Strategy pattern as a more flexible solution, allowing different combinations of printing and sorting behaviors without creating a separate class for each combination.

Thank you for bringing this up. It's an important part of the instructor's explanation that shows why a more flexible design was needed.
Assistant
After identifying the issue with the initial design, the instructor proceeded to explain the Strategy pattern as a solution. Here's what he taught:

Decoupling Behaviors:
Instead of having behaviors (sorting and printing) tied to specific subclasses, treat them as separate, interchangeable algorithms.
Creating Interfaces:
Create an interface for sorting algorithms (ISortAlgorithm)
Create an interface for printing algorithms (IPrintAlgorithm)
Implementing Concrete Strategies:
Implement various sorting algorithms (e.g., CountSort, TimSort) that conform to ISortAlgorithm
Implement various printing algorithms (e.g., VerticalPrint, HorizontalPrint) that conform to IPrintAlgorithm
Modifying MyList:
Instead of subclassing MyList for different behaviors, keep MyList as a single class
Add attributes to MyList for sorting and printing algorithms: java private ISortAlgorithm sortAlgorithm; private IPrintAlgorithm printAlgorithm;
Dependency Injection:
Pass the required algorithms through the constructor or setter methods
Delegating Behavior:
In the sort() method of MyList, delegate to the sortAlgorithm
In the printList() method, delegate to the printAlgorithm
Benefits:
Flexibility to change algorithms at runtime
Easy to add new sorting or printing algorithms without modifying MyList
Adheres to Open-Closed Principle
Solves Single Responsibility Principle issue
Real-world Examples:
Payment gateways in e-commerce systems
Route finding algorithms in mapping applications like Google Maps
The instructor emphasized that this approach allows for more flexible and maintainable code, as behaviors can be easily swapped or added without modifying the core MyList class. This solution addresses the needs of all clients (including the third client) without creating an explosion of subclasses.


