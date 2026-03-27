import java.util.*;
//  say if we have Bird inherited by Eagle and Hen and kiwi 
//  flable(has fly() and flapWings()) interface is implemented by Eagle and Hen but not by kiwi
//  say in future we have to add entity like superman then though superman can fly but cannot flapWings()
// again interface has more than one reason to change so we have to split interface into multiple interfaces
// Interface Segregation Principle: Interfaces should be lean and very specific to a single responsibility and should not be generic 

//LEST TALK ABOUT What we did in hr managemnet system
// initial design --> we have employee abstact class and two concrete class FT and Intern which inherit from the Employee class
// inside emloyee we had save() it was not abstact class b/c for both type of employee the implementation was same
// but in future we have to add new type of employee say contractor and for contractor the save() implementation was different
// so we have to change the save() method in employee class and add a new if-else block to handle the contractor case
// this violates the Open-Closed Principle
// aslo the cureent save() method is also doing serialization and file handling which violates the single responsibility principle many implementation in one method
// we shifted this save() method into employee repository class and created a serializer class to handle the serialization and file handling
// we already needed employee repository to store list of employees in memory and we need to search,iterate or print those 

// this save() method there is some error
// now we have file system now later on if we have to add database system then we have to change the save() method in employee repository class and add a new if-else block to handle the database case
// this violates the Open-Closed Principle ,now it has more reasons to change 
// earlier we had ONly FileStorageService but if we have MongoDbStorageSErvice in future 
//  so we will create IDataBase Service interface and implement it in MongoDbStorageSErvice or SQLStorageService class also in FileStorageService class
// so EmployeeRepo should depend on the interface not on the concrete class 
// so inside this save() we will never create any concrete object rather we will 
//  we will keep the reference of the interface instead of its concrete class 
// so we will inject the dependency from outside 

// now here clinet get better level of abstraction and is complete agnoistic of the underlying storage system of what type of databaseserver it is using
// this is called dependency injection 
public class Main {
    static class Employee {
        private int id;
        private String name;
        private String email;
        private String phone;
        private double salary;

        public Employee(int id, String name, String email, String phone, double salary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.salary = salary;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }
    }

    static class EmployeeRepository {
        private IDataBaseSaervice dbs;

        public EmployeeRepository(IDataBaseSaervice dbs) {
            this.dbs = dbs;
        }

        public void save(Employee e) {
            this.dbs.write(e);
        }
    }

    interface IDataBaseSaervice {
        void write(Employee e);
    }

    static class FileStorageService implements IDataBaseSaervice {
        @Override
        public void write(Employee e) {
            System.out.println("Writing to file");
        }
    }

    static class MongoDbStorageService implements IDataBaseSaervice {
        @Override
        public void write(Employee e) {
            System.out.println("Writing to MongoDB");
        }
    }

    public static void main(String[] args) {
        IDataBaseSaervice dbs = new FileStorageService();
        EmployeeRepository repo = new EmployeeRepository(dbs);
        Employee e = new Employee(1, "Kaif", "[EMAIL_ADDRESS]", "1234567890", 50000);
        repo.save(e);
    }
}

// earlier the employeeReso was dependent on FileStorageDb
// now it is dependent on the interface IDataBaseSaervice
// so it is not dependent on the concrete class
// this is called dependency inversion principle
// instead of one class being dependent on another class now both the classes should be depenennt on abstraction(interface)
//  the EmployeeRepo and FileStorageService should be dependent on IDataBaseSaervice interface
// dependency on interface not on concrete class thorugh Constructor Injection
//  dependency Inversion -> High Level Module should not depend on Low Level Module. Both should depend on Abstraction
//  dependency injuc -> way of executing this principle

// THE JAVA FRAMEWORK IE SpringBoot does the job of inversion of control ie take the control of creating and maintaining the lifecycle of objects
// it decides which object depends on the other and which need to be deleted
// this is called dependency injection
// we want to restrict the object creation to one 
// static variable means only one object is created and shared across all the instances
//  in heap memory only one copy is created and shared accross all the instances
//      static fuction is also shared across all the instances and no need of creating any object of class where static method is defined like main() which is static say inside Demo class  and we do not create any object of Demo class 
//  directly we do Demo.main()
//   if there is a static method, inside the static method we cannot call anything which is associated with something with the object, right? Or inside the static method, we cannot call any non-static variable or non-static method or any other thing. 
/*
We should not make write() and searchById() static because their behavior can change depending on the database implementation (MongoDB, MySQL, etc.).

Static methods:
	•	Belong to the class
	•	Do not support runtime polymorphism
	•	Cannot be overridden properly

Since different database services will implement these methods differently, we need dynamic behavior, which requires instance methods.

Static methods are suitable only when the logic is fixed and never changes (like Math.max()).

So, because the implementation varies, these methods should not be static.
 */
class Demo{
    public static void main(String[] args) {
        
    }

}
