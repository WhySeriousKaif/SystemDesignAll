package snake_and_ladder_assignment;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter n (size of board n x n): ");
        int n = scanner.nextInt();
        
        System.out.println("Enter x (number of players): ");
        int x = scanner.nextInt();
        
        System.out.println("Enter difficulty level (easy/hard): ");
        String difficulty = scanner.next();

        System.out.println("Initializing Snake and Ladder on a " + (n*n) + " cell board...");
        System.out.println("Placing " + n + " snakes and " + n + " ladders.");
        
        Game game = GameFactory.createGame(n, x, difficulty);
        game.play();
        scanner.close();
    }
}
