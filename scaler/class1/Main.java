package scaler.class1;

class Bird{
    String type;
    int age;
    double height;
    double weight;
    
    Bird(String type, int age, double height, double weight){
        this.type = type;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    void fly(){
        if(type.equals("Hen")){
            System.out.println("Hen fly low");
        }
        else if(type.equals("Eagle")){
            System.out.println("Eagle fly high");
        }
        else{
            System.out.println(type+" fly");
        }
    }
}


/*
here this design is not wrong but it is not a good design unlike DSA we have object solution for a problem
here depending upon the requiments if we have very few types of birds then this design is good but if we have many types of birds then this design is not good
we can use inheritance to solve this problem
 */


class Bird1{
    int age;
    double height;
    double weight;

    
    
    Bird1(int age, double height, double weight){
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    void fly(){
        System.out.println("Bird fly");
    }

}

class Hen extends Bird1{

    Hen(int age, double height, double weight){
        super(age, height, weight);
    }
    void fly(){
        System.out.println("Hen fly low");
    }       

}
class Eagle extends Bird1{

    Eagle(int age, double height, double weight){
        super(age, height, weight);
    }
    void fly(){
        System.out.println("Eagle fly high");
    }

}

/*
here we have used inheritance to solve the problem
Maintainability: If we have to add new type of bird then we have to add new class and override the fly method and do not have to touch other subclasses 
Readability: We can understand the code easily as code is small now 
Flexibility: We can add new features to the code easily
Here you do not need to map of diffrent Bird vs how they exactly should fly

*/

//  we need to make it more object oriented and make the fly method abstact because not all birds can fly 
//  aslo there is problem of Silent Killer 
//  say Another class FlyingBird extends Bird1 and if we create object of FlyingBird and class fly() is not overriden then it will throw error
// we are not getting the full benefit of inheritance and the behavior we want

//  we can make the method abstact so that whatever subclasses are there they must implement the method

abstract class Bird2{
    int age;
    double height;
    double weight;

    
    Bird2(int age, double height, double weight){
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    abstract void fly();
}

class Hen2 extends Bird2{
    Hen2(int age, double height, double weight){
        super(age, height, weight);
    }
    void fly(){
        System.out.println("Hen fly low");
    }
}
class Eagle2 extends Bird2{
    Eagle2(int age, double height, double weight){
        super(age, height, weight);
    }
    void fly(){
        System.out.println("Eagle fly high");
    }
}


/*

there is one more problem 
there are many birds which can not fly at all like Penguin, Kiwi etc

we can have multiclass interitance like 
we may have BirdThatFly and BirdThatCanNotFly
then we can have Hen and Eagle extends <BirdThatFly> (<fly()>) and Penguin extends <BirdThatCanNotFly>
but there are other behaviors like  swim() so we have the same problem again


Hence we can not have multiclass inheritance



*/
class Client{

    void Render(Bird2 bird){
        bird.fly();
    }
    /*
    we have passed object of different classes(types) to the same method
    and the method is able to call the fly method of the object
    this is called Polymorphism
    Bird2 b=new Hen2(1,1.2,1.2)/Eagle2(2,2.2,2.2)/Penguin2(3,3.3,3.3);
    by this we are creating the object of bird as well as hen or eagle or penguin
    we are able to achieve the same behavior by different objects ==> polymorphism

    Client does not know about the type of object it is creating
    it just knows that it is creating a bird
    this is called abstraction(hiding the implementation details)

    but again here 
    if client is getting to which all subclasses are tehre of bird thenn implementing if/else in their subclasses is waste
    in comparision to what were doing in the first senario

    the solution is instead of multiclass inheritance we can have multiple inheritace
    but java does not support multiple inheritace unlike c++
    in jave we have diamod inheritace problem which states -> 
    class Bird{
       void flapWings(){
            System.out.println("Flapping wings");
        }
    }
    class Flyable{
       abstract void fly();
        void flapWings(){
            System.out.println("Flapping wings");
        }
            
        
    }
    class Penguin extends Bird,Flyable{

        
    }
    
    public static void main(String[] args) {
    Bird penguin=new Penguin();

    penguin.flapWings();
    // diamond problem
    // confusion of which implementation to use
        
    }
    

    we can use interface to solve this problem
    interface can have multiple methods(collection of abstract methods)
    and we can implement multiple interfaces in a class]

    


     */

    interface Flyable{
        void fly();
    }
    interface Swimmable{
        void swim();
    }
    class Eagle3 extends Bird2 implements Flyable{
        Eagle3(int age, double height, double weight){
            super(age, height, weight);
        }
        void fly(){
            System.out.println("Eagle fly high");
        }

        
    }
    class Penguin3 extends Bird2 implements Swimmable{
        Penguin3(int age, double height, double weight){
            super(age, height, weight);
        }
        void swim(){
            System.out.println("Penguin swim");
        }

        
    }
    public class Main {
    public static void main(String[] args) {
        Bird hen=new Bird("Hen",1,1.2,1.2);
        Bird eagle=new Bird("Eagle",2,2.2,2.2);
        eagle.fly();
        hen.fly();

        // ################################################//#endregion

        Hen hen1 = new Hen(1,1.2,1.2);
        Eagle eagle1 = new Eagle(2,2.2,2.2);

        hen1.fly();
        eagle1.fly();

        // ################################################//#endregion

        
        Hen2 hen2 = new Hen2(1,1.2,1.2);
        Eagle2 eagle2 = new Eagle2(2,2.2,2.2);

        hen2.fly();
        eagle2.fly();

        // ################################################//#endregion

        Eagle3 eagle3 = new Eagle3(2,2.2,2.2);
        Penguin3 penguin3 = new Penguin3(3,3.3,3.3);

        eagle3.fly();
        penguin3.swim();


        
    }
    /*
    class Client{
        void render(Flyable b){
            b.fly();
        }
    // now we can pass any object that implements Flyable interface by this we are creating object of Flyable and also the subclases which are implementing Flyable interface
    // this is called duck typing
    object of there classes are referred as reference of interface 
    so interface also provides polymorphism
    --> runtime polymorphism


    }


    */

    // 
    // --> compile time polymorphism
    // method overloading

    int add(int a, int b){
        return a+b;
    }
    int add(int a, int b, int c){
        return a+b+c;
    }
// here we have same method name but different parameters
// this is called method overloading
// and the method which is called is decided at compile time

// compile time polymorphism




}