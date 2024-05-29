package Engine.Heuristics;

import Engine.Board.IBoard;
import Engine.Board.Move;

public class PawnMobility implements IHeuristics{

	@Override
	public String getName() {
		return "Pawn mobility";
	}

	@Override
	public int evaluate(IBoard board) {
		// returned int: plus grand = better (range [0;10])
		// Return score depending on pawns'mobility (aka. number of possible moves, all pawns included)
		int nbMoves = board.getPossibleMoves(true).length;
		// min: 0, max (théorique): 126
		// Normalization: zi = (xi – min(x)) / (max(x) – min(x)) * 100
		return (nbMoves - 0)/(126-0)*10;
	}

}
