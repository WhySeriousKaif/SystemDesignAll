package snake_and_ladder_assignment;

import java.util.ArrayList;
import java.util.List;

public class GameFactory {
    public static Game createGame(int n, int x, String difficulty) {
        EntityGenerationStrategy strategy = new RandomEntityGenerationStrategy();
        BoardFactory boardFactory = new BoardFactory(strategy);
        Board board = boardFactory.createBoard(n);

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= x; i++) {
            players.add(new Player("Player " + i));
        }

        MakeMoveStrategy moveStrategy;
        if (difficulty.equalsIgnoreCase("HARD")) {
            moveStrategy = new HardMoveStrategy();
        } else {
            moveStrategy = new EasyMoveStrategy();
        }

        return new Game(board, players, moveStrategy);
    }
}
