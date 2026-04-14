import java.util.*;

// --- PROTOTYPE PATTERN ---
interface Shape {
    Shape copy();
}

class Rectangle implements Shape {
    int width, height;
    Rectangle(int w, int h) { this.width = w; this.height = h; }
    public Shape copy() { return new Rectangle(width, height); }
    public void setWidth(int w) { this.width = w; }
    public String toString() { return "Rectangle: " + width + " x " + height; }
}

class ShapeRegistry {
    private Map<String, Shape> map = new HashMap<>();
    void register(String key, Shape shape) { map.put(key, shape); }
    Shape get(String key) { return map.get(key).copy(); }
}

// --- PROXY PATTERN ---
interface TextParser {
    String parse(String text);
}

class RealTextParser implements TextParser {
    public String parse(String text) {
        System.out.println("Parsing text...");
        return text.toUpperCase();
    }
}

class ProxyTextParser implements TextParser {
    private RealTextParser realParser;
    public String parse(String text) {
        if (text == null || text.isEmpty()) return "Invalid input!";
        if (realParser == null) realParser = new RealTextParser();
        return realParser.parse(text);
    }
}

// --- DECORATOR PATTERN ---
interface Notifier {
    void send(String msg);
}

class EmailNotifier implements Notifier {
    public void send(String msg) { System.out.println("Sending Email: " + msg); }
}

abstract class NotifierDecorator implements Notifier {
    protected Notifier notifier;
    NotifierDecorator(Notifier notifier) { this.notifier = notifier; }
    public void send(String msg) { notifier.send(msg); }
}

class SMSDecorator extends NotifierDecorator {
    SMSDecorator(Notifier notifier) { super(notifier); }
    public void send(String msg) {
        super.send(msg);
        System.out.println("Sending SMS: " + msg);
    }
}

class WhatsAppDecorator extends NotifierDecorator {
    WhatsAppDecorator(Notifier notifier) { super(notifier); }
    public void send(String msg) {
        super.send(msg);
        System.out.println("Sending WhatsApp: " + msg);
    }
}

// --- FLYWEIGHT PATTERN ---
class BulletType {
    String color;
    String image;
    public BulletType(String color, String image) {
        this.color = color;
        this.image = image;
        System.out.println("Creating BulletType: " + color);
    }
    public void display(int x, int y) {
        System.out.println("Bullet " + color + " at (" + x + "," + y + ")");
    }
}

class Bullet {
    int x, y;
    BulletType type;
    public Bullet(int x, int y, BulletType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    public void draw() { type.display(x, y); }
}

class BulletFactory {
    private static Map<String, BulletType> map = new HashMap<>();
    public static BulletType getBulletType(String color) {
        if (!map.containsKey(color)) {
            map.put(color, new BulletType(color, "bullet.png"));
        }
        return map.get(color);
    }
}

// --- MAIN CLIENT ---
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Prototype Pattern ===");
        ShapeRegistry registry = new ShapeRegistry();
        registry.register("rect", new Rectangle(10, 5));
        Rectangle r1 = (Rectangle) registry.get("rect");
        r1.setWidth(20);
        Rectangle r2 = (Rectangle) registry.get("rect");
        System.out.println(r1);
        System.out.println(r2);

        System.out.println("\n=== Proxy Pattern ===");
        TextParser parser = new ProxyTextParser();
        System.out.println(parser.parse("hello world"));
        System.out.println(parser.parse(""));

        System.out.println("\n=== Decorator Pattern ===");
        Notifier n3 = new WhatsAppDecorator(new SMSDecorator(new EmailNotifier()));
        n3.send("Hello");

        System.out.println("\n=== Flyweight Pattern ===");
        Bullet b1 = new Bullet(10, 20, BulletFactory.getBulletType("red"));
        Bullet b2 = new Bullet(30, 40, BulletFactory.getBulletType("red"));
        Bullet b3 = new Bullet(50, 60, BulletFactory.getBulletType("blue"));
        b1.draw(); b2.draw(); b3.draw();
    }
}
