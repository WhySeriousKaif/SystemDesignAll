package snake_and_ladder_assignment;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private int size;
    private Map<Integer, BoardEntity> entities = new HashMap<>();

    public Board(int size) {
        this.size = size;
    }

    public void addEntity(BoardEntity entity) {
        entities.put(entity.getStart(), entity);
    }

    public int resolvePosition(int position) {
        if (entities.containsKey(position)) {
            BoardEntity entity = entities.get(position);
            int newPosition = entity.getEnd();
            if (entity instanceof Snake) {
                System.out.println("Oops! Bitten by snake at " + entity.getStart() + ", sliding down to " + entity.getEnd());
            } else if (entity instanceof Ladder) {
                System.out.println("Yay! Climbed ladder at " + entity.getStart() + ", going up to " + entity.getEnd());
            }
            return newPosition;
        }
        return position;
    }

    public int getSize() {
        return size;
    }
}
