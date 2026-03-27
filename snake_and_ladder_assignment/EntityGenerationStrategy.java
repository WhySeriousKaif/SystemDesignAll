package snake_and_ladder_assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface EntityGenerationStrategy {
    List<BoardEntity> generateSnakes(int totalCells, int count);
    List<BoardEntity> generateLadders(int totalCells, int count, List<BoardEntity> existingEntities);
}

class RandomEntityGenerationStrategy implements EntityGenerationStrategy {
    private Random random = new Random();

    @Override
    public List<BoardEntity> generateSnakes(int totalCells, int count) {
        List<BoardEntity> snakes = new ArrayList<>();
        int formed = 0;
        int maxAttempts = count * 100;
        int attempts = 0;
        
        while (formed < count && attempts < maxAttempts) {
            attempts++;
            int head = random.nextInt(totalCells - 2) + 2; 
            int tail = random.nextInt(head - 1) + 1;
            
            boolean conflict = false;
            for (BoardEntity s : snakes) {
                if (s.getStart() == head || s.getEnd() == tail || s.getStart() == tail || s.getEnd() == head) {
                    conflict = true; break;
                }
            }
            if (!conflict) {
                snakes.add(new Snake(head, tail));
                formed++;
            }
        }
        return snakes;
    }

    @Override
    public List<BoardEntity> generateLadders(int totalCells, int count, List<BoardEntity> existingEntities) {
        List<BoardEntity> ladders = new ArrayList<>();
        int formed = 0;
        int maxAttempts = count * 100;
        int attempts = 0;
        
        while (formed < count && attempts < maxAttempts) {
            attempts++;
            int start = random.nextInt(totalCells - 2) + 1;
            int end = random.nextInt(totalCells - start - 1) + start + 1;

            boolean conflict = false;
            for (BoardEntity e : existingEntities) {
                if (e.getStart() == start || e.getStart() == end || e.getEnd() == start || e.getEnd() == end) {
                    conflict = true; break;
                }
            }
            for (BoardEntity l : ladders) {
                if (l.getStart() == start || l.getStart() == end || l.getEnd() == start || l.getEnd() == end) {
                    conflict = true; break;
                }
            }
            if (!conflict) {
                ladders.add(new Ladder(start, end));
                formed++;
            }
        }
        return ladders;
    }
}
