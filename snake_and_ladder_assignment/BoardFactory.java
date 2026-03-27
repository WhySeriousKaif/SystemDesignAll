package snake_and_ladder_assignment;

import java.util.List;

public class BoardFactory {
    private EntityGenerationStrategy strategy;

    public BoardFactory(EntityGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    public Board createBoard(int n) {
        int totalCells = n * n;
        Board board = new Board(totalCells);
        // Requirement implies n snakes and n ladders for an n x n board
        int snakeCount = n;
        int ladderCount = n;

        List<BoardEntity> snakes = strategy.generateSnakes(totalCells, snakeCount);
        List<BoardEntity> ladders = strategy.generateLadders(totalCells, ladderCount, snakes);

        for (BoardEntity snake : snakes) {
            board.addEntity(snake);
        }
        for (BoardEntity ladder : ladders) {
            board.addEntity(ladder);
        }

        return board;
    }
}
