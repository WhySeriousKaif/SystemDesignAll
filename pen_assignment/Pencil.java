public class Pencil implements WritingImplement {
    private String hardness;

    public Pencil() {
        this.hardness = "HB"; // Default
    }

    public Pencil(String hardness) {
        this.hardness = hardness;
    }

    @Override
    public void write(String text) {
        System.out.println("Writing with a " + hardness + " Pencil: " + text);
    }
}
