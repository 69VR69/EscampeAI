package Engine.Heuristics;

import Engine.Board.IBoard;

public interface IHeuristics {
    String getName();

    /**
     * Evaluate the board
     * @param board The board to evaluate
     * @return The evaluation of the board
     */
    int evaluate(IBoard board);
}
