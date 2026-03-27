package snake_and_ladder_assignment;

public abstract class MakeMoveStrategy {
    public abstract int makeMove(Player player, Dice dice, Board board);
}

class EasyMoveStrategy extends MakeMoveStrategy {
    @Override
    public int makeMove(Player player, Dice dice, Board board) {
        int roll = dice.roll();
        System.out.print(player.name + " rolled a " + roll + ". ");
        int newPosition = player.position + roll;
        
        if (newPosition > board.getSize()) {
            System.out.println("Needs exact roll to win. Cannot move.");
            return player.position;
        }
        
        newPosition = board.resolvePosition(newPosition);
        player.position = newPosition;
        
        if (roll == 6 && player.position < board.getSize()) {
            System.out.println("Rolled a 6! Extra turn...");
            return makeMove(player, dice, board); // Recursive call for extra turn
        }
        
        return player.position;
    }
}

class HardMoveStrategy extends MakeMoveStrategy {
    @Override
    public int makeMove(Player player, Dice dice, Board board) {
        int sixCount = 0;
        int totalRoll = 0;
        int roll;
        
        do {
            roll = dice.roll();
            System.out.print(player.name + " rolled a " + roll + ". ");
            if (roll == 6) {
                sixCount++;
                totalRoll += roll;
            } else {
                totalRoll += roll;
                break;
            }
        } while (sixCount < 3);

        if (sixCount == 3) {
            System.out.println("Rolled three consecutive 6s! Turn forfeited.");
            return player.position;
        }

        int newPosition = player.position + totalRoll;
        if (newPosition > board.getSize()) {
            System.out.println("Needs exact roll to win. Cannot move.");
            return player.position;
        }
        
        newPosition = board.resolvePosition(newPosition);
        player.position = newPosition;
        return player.position;
    }
}
