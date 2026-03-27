
// v0 HR Management System
/*
 it should support FT employee and intern employee
 provid save feature to save the data of an emplyee in file

 */
import java.util.List;

class Employee {
    String name;
    String id;
    String email;
    String type;
    int phoneNumber;

    public Employee(String name, String id, String email, String type, int phoneNumber) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.type = type;
        this.phoneNumber = phoneNumber;
    }

    void save() {
        // we need to fist seralize the data(object) in which we want to save the
        // data(create the string in specific format)
        // then we need to open the given file
        // write the data in file
        // close the file
    }
}

class FullTimeEmployee extends Employee {

    public FullTimeEmployee(String name, String id, String email, String type, int phoneNumber) {
        super(name, id, email, type, phoneNumber);
    }

}

class Intern extends Employee {

    public Intern(String name, String id, String email, String type, int phoneNumber) {
        super(name, id, email, type, phoneNumber);
    }

}

/*
 * this design does not follow single responsibility principle
 * as in save(){
 * // we need to fist seralize the data(object) in which we want to save the
 * data(create the string in specific format)
 * // then we need to open the given file
 * // write the data in file
 * //close the file
 * this is not following srp --> there are 4 reasons for this method to change
 * }
 */
/*
 * 
 * we need a repository class which will have list of all the employees and can
 * have save() method in it
 */

class EmployeeRepository {
    List<Employee> employees;

    void save(Employee employee) {
        // we need to fist seralize the data(object) in which we want to save the
        // data(create the string in specific format)
        // then we need to open the given file
        // write the data in file
        // close the file
    }

}

/*
 * one way we create two method inside this EmployeeRepository class
 * Serialize and Deserialize
 * but still the class has more than one reason to change
 * so we need different classes formatter and interactingwithfile class which
 * will have their respective responsibility
 * 
 */
class Seralizer {
    String serialize(Employee employee) {
        // we need to fist seralize the data(object) in which we want to save the
        // data(create the string in specific format)
    }
}

class FileHandler {
    void writeFile(String data`){

    }

    void search(String data) {

    }

}

    void save(Employee e) {
        Seralizer s = new Seralizer();
        FileHandler f = new FileHandler();
        String data = s.serialize(e);
        f.writeFile(data);

    }
    /*
     * now if we want to change the format in which data must be saved then
     * Serilizer class will be changed
     * but if we want to change the database then FileHandler class will be changed
     * now every class has one reason to change
     * these serilizer and filehandler should be created in such a way tht we are
     * creating their object at max once
     * 
     */

    /*
     * v1 add feature to calculate income tax of an employee
     * income tax = 20 % income of all type of employees
     * one ways
     * 
     * we can keep this logic inside the employee class calculateIncomeTax() but srp
     * is violated
     * as we have two different reason for this method to change
     * 
     * so we can create another class IncomeTaxCalculator
     */
    class IncomeTaxCalculator {
        double calculateIncomeTax(Employee employee) {
            return employee.getIncome() * 0.2;
        }
    }

    /*
     * v2
     * FT employee income tax = 30 % income of FT employees+2% Professional tax
     * intern employee income tax = 15 % income of intern employees
     * 
     * one way is inside IncomeTaxCalculator class we can have if else block inside
     * calculateIncomeTax method
     * 
     */

    /*
    
    */
    interface CalculateIncomeTax {
        double calculateIncomeTax(Employee employee);
    }

    class FullTimeEmployeeIncomeTaxCalculator implements CalculateIncomeTax {
        @Override
        double calculateIncomeTax(Employee employee) {
            return employee.getIncome() * 0.3 + 2;
        }

    }

    class InternIncomeTaxCalculator implements CalculateIncomeTax {
        @Override
        double calculateIncomeTax(Employee employee) {
            return employee.getIncome() * 0.15;
        }
    }
    /*
    
    */

class Client{
    List<Employee> employees;
    e=employees.get(i);
    if(e.type=="FT"){
        //calculate income tax for FT employee 
        FullTimeEmployeeIncomeTaxCalculator fullTimeEmployeeIncomeTaxCalculator=new FullTimeEmployeeIncomeTaxCalculator();
        fullTimeEmployeeIncomeTaxCalculator.calculateIncomeTax(e);
    }
    else if(e.type=="Intern"){
        //calculate income tax for intern employee
        InternIncomeTaxCalculator internIncomeTaxCalculator=new InternIncomeTaxCalculator();
        internIncomeTaxCalculator.calculateIncomeTax(e);
    }
}

    // no abstraction here from client
    // we do not want client to know about the employee type that should be done by
    // our side
    class Employee {
        String name;
        String id;
        String email;
        String type;
        int phoneNumber;
        TaxCalculatorUtil taxCalculatorUtil;

        public Employee(String name, String id, String email, String type, int phoneNumber) {
            this.name = name;
            this.id = id;
            this.email = email;
            this.type = type;
            this.phoneNumber = phoneNumber;
        }
        public double calculateIncomeTax(){
            return taxCalculatorUtil.calculateIncomeTax(this);
        }

    }

public static void main(String[] args) {
    
    FullTimeEmployee ft=new FullTimeEmployee("John", "1", "[EMAIL_ADDRESS]", "FT", 1234567890);
    Intern intern=new Intern("Jane", "2", "[EMAIL_ADDRESS]", "Intern", 1234567890);

    ft.taxCalculatorUtil=new FullTimeEmployeeIncomeTaxCalculator();
    intern.taxCalculatorUtil=new InternIncomeTaxCalculator();

    ft.taxCalculatorUtil.calculateIncomeTax(ft);
    intern.taxCalculatorUtil.calculateIncomeTax(intern);
}
//we can initailise the taxCalculatorUtil attribute with the object of FullTimeEmployeeIncomeTaxCalculator and CalCulateTaxUtil has calculateTax method

class FullTimeEmployee extends Employee{
    FullTimeEmployee(){
        this.taxCalculatorUtil=new FullTimeEmployeeIncomeTaxCalculator();
    }
    
}
class Intern extends Employee{
    Intern(){
        this.taxCalculatorUtil=new InternIncomeTaxCalculator();
    }
}
