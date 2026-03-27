public class Main {
    public static void main(String[] args) {
        System.out.println("=== Testing Factory Created Pens ===");
        
        WritingImplement blueBallpoint = PenFactory.getPen("BALLPOINT", "Blue", true); // Clickable
        if (blueBallpoint instanceof Pen) ((Pen) blueBallpoint).start();
        blueBallpoint.write("Hello from factory ballpoint!");
        if (blueBallpoint instanceof Pen) ((Pen) blueBallpoint).close();

        System.out.println("\n=== Testing Factory Created Gel Pen ===");
        WritingImplement redGel = PenFactory.getPen("GEL", "Red", false); // Cap
        if (redGel instanceof Pen) ((Pen) redGel).start();
        redGel.write("Hello from factory gel!");
        if (redGel instanceof Pen) ((Pen) redGel).close();
        
        System.out.println("\n=== Testing Pencil ===");
        WritingImplement pencil = PenFactory.getPen("PENCIL", "Graphite", false);
        pencil.write("Sketching a design");
    }
}
