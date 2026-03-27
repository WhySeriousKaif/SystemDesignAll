package snake_and_ladder_assignment;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    private Board board;
    private Queue<Player> players;
    private MakeMoveStrategy strategy;
    private int totalPlayers;

    public Game(Board board, List<Player> players, MakeMoveStrategy strategy) {
        this.board = board;
        this.players = new LinkedList<>(players);
        this.totalPlayers = players.size();
        this.strategy = strategy;
    }

    public void play() {
        System.out.println("=== GAME STARTED ===");
        int winners = 0;
        Dice dice = new Dice();
        while (players.size() > 1 && winners < totalPlayers - 1) {
            Player current = players.poll();
            System.out.print(current.name + "'s turn. ");
            
            strategy.makeMove(current, dice, board);
            System.out.println(current.name + " is now at " + current.position);
            
            if (current.position == board.getSize()) {
                System.out.println(current.name + " WINS the game!");
                winners++;
            } else {
                players.offer(current);
            }
            System.out.println("---------------------------------");
        }
        
        System.out.println("=== GAME OVER ===");
        if (!players.isEmpty()) {
            System.out.println(players.poll().name + " is the last player standing (lost).");
        }
    }
}
