package Engine.Heuristics;

import Engine.Board.*;
import Engine.Utils;

public class DistanceToEnemy implements IHeuristics {
    private final int MAX_DISTANCE = 3;

    @Override
    public String getName() {
        return "Count the number of enemy pawns in the range of " + MAX_DISTANCE + " cells";
    }

    @Override
    public int evaluate(IBoard board) {
        // Count the number of enemy pawns in the range
        int nbEnemies = 0;

        // Find the player's Unicorn using bitwise operations
        IPawn playerUnicorn = board.getPlayerUnicorn();
        if(playerUnicorn == Utils.NullPawn()) return Integer.MAX_VALUE;

        for (int cell = 1; cell <= MAX_DISTANCE; cell++) {
            // Get the number of moves the pawn can do by multiplying the cell type by 4 directions
            int nbMaxMoves = cell * 4;

            // Get the possible moves of the pawn
            int lowerLimit = playerUnicorn.getColumnNumber();
            int upperLimit = playerUnicorn.getLineNumber() + cell;
            int negativeUpperLimit = playerUnicorn.getLineNumber() - cell;
            Position pos = new Position(lowerLimit, upperLimit);
            Position increments = new Position(0, 0);
            for (int i = 0; i < nbMaxMoves; i++) {

                // Update the coordinates
                if (pos.equals(new Position(lowerLimit, upperLimit)))
                    increments.setTo(1, -1);
                else if (pos.equals(new Position(upperLimit, lowerLimit)))
                    increments.setTo(-1, -1);
                else if (pos.equals(new Position(lowerLimit, negativeUpperLimit)))
                    increments.setTo(-1, 1);
                else if (pos.equals(new Position(negativeUpperLimit, lowerLimit)))
                    increments.setTo(1, 1);

                pos.add(increments);

                // Check if the position is valid
                if (!pos.isInBounds(board.getBoardLineSize())) continue;

                // Check if the cell at the new position is empty from bitboard
                IPawn cellPawn = board.getPawnFromPosition(pos);
                if (cellPawn == null || cellPawn.getIsOccupied())
                {
                    // Check if the cell is occupied by an enemy pawn
                    if (cellPawn != null && cellPawn.getIsWhite() != playerUnicorn.getIsWhite())
                        nbEnemies++;
                }
            }
        }

        // Normalize the count between 0 and 10 knowing that the maximum number of enemies is 6
        return (int) ((nbEnemies / 6.0) * 10);
    }
}
