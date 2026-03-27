package snake_and_ladder_assignment;

public interface BoardEntity {
    int getStart();
    int getEnd();
}

class Snake implements BoardEntity {
    private int start, end;
    public Snake(int start, int end) {
        this.start = start;  // Head
        this.end = end;      // Tail
    }
    public int getStart() { return start; }
    public int getEnd() { return end; }
}

class Ladder implements BoardEntity {
    private int start, end;
    public Ladder(int start, int end) {
        this.start = start;  // Bottom
        this.end = end;      // Top
    }
    public int getStart() { return start; }
    public int getEnd() { return end; }
}
