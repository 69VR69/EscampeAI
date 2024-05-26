package Engine.Heuristics;

import Engine.Board.*;
import Engine.Utils;

public class DistanceToEnemy implements IHeuristics {
    @Override
    public String getName() {
        return "Distance to ennemy";
    }

    @Override
    public int evaluate(IBoard board) {
        // Calculate the minimal distance between the player's Unicorn and the enemy's Pawns
        int minDistance = Integer.MAX_VALUE;

        // Find the player's Unicorn using bitwise operations
        IPawn playerUnicorn = board.getPlayerUnicorn();
        if(playerUnicorn == Utils.NullPawn()) return Integer.MAX_VALUE;

        for (int cell = 1; cell <= 6; cell++) {
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
                    {
                        // Calculate the distance between the player's Unicorn and the enemy's Pawn
                        int distance = Math.abs(playerUnicorn.getColumnNumber() - pos.getColumn()) + Math.abs(playerUnicorn.getLineNumber() - pos.getLine());
                        if (distance < minDistance)
                            minDistance = distance;
                    }
                }
            }
        }

        return minDistance;
    }
}
