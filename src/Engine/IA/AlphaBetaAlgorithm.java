package Engine.IA;

import Engine.Board.IBoard;
import Engine.Board.IMove;
import Engine.Board.Move;
import Engine.Utils;

public class AlphaBetaAlgorithm {
    protected int maxDepth;

    private final EvaluatedMove NothingMove = new EvaluatedMove(Utils.NothingMove());

    public AlphaBetaAlgorithm(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * Get the best move for the current player
     *
     * @param board   the current board
     * @param isWhite true if the current player is white, false otherwise
     * @return the best move
     */
    public Move getBestMove(IBoard board, boolean isWhite) {
        // Security check
        if (board == null)
            return NothingMove;

        // Initialize the variables
        Move bestMove = NothingMove;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        IBoard tempBoard = board.Clone();

        // Get the possible moves
        Move[] temp = (Move[]) tempBoard.getPossibleMoves(isWhite);
        EvaluatedMove[] moves = new EvaluatedMove[temp.length];
        for (int i = 0; i <temp.length; i++) {
            moves[i] = new EvaluatedMove(temp[i]);
        }

        // Iterate over the possible moves
        for (EvaluatedMove move : moves) {
            // Apply the move
            tempBoard.applyMoveWithChecks(move);

            // Call the alphaBeta method recursively
            int value = alphaBeta(tempBoard, maxDepth, alpha, beta, false);

            // Undo the move
            tempBoard.undoMove(move);

            // If the value is greater than the alpha value
            if (value > alpha) {
                // Update the alpha value
                alpha = value;
                // Update the best move
                bestMove = move;
            }
        }

        // Return the best move
        return bestMove;
    }

    /**
     * Alpha-beta algorithm
     *
     * @param board              the current board
     * @param depth              the depth of the search
     * @param alpha              the alpha value
     * @param beta               the beta value
     * @param isMaximizingPlayer true if the current player is maximizing, false otherwise
     * @return the value of the best move
     */
    protected int alphaBeta(IBoard board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        // Security check
        if (board == null)
            return 0;

        // If the depth is 0 or if the game is over, return the evaluation of the board
        if (depth == 0 || board.isGameOver())
            return board.evaluate();

        // Initialize the variables
        EvaluatedMove[] moves = (EvaluatedMove[]) board.getPossibleMoves(isMaximizingPlayer);

        // If there are no possible moves, return the current best move
        if (moves.length == 0)
            return board.evaluate();

        // If the current player is maximizing

        // Return the best move
        return isMaximizingPlayer ?
                CheckNestedMoves(board, depth - 1, alpha, beta, moves, false) :
                CheckNestedMoves(board, depth - 1, alpha, beta, moves, true);
    }

    /**
     * Check the nested moves
     *
     * @param board              the current board
     * @param depth              the depth of the search
     * @param alpha              the alpha value
     * @param beta               the beta value
     * @param moves              the possible moves
     * @param isMaximizingPlayer true if the current player is maximizing, false otherwise
     * @return the value of the best move
     */
    private int CheckNestedMoves(IBoard board, int depth, int alpha, int beta, EvaluatedMove[] moves, boolean isMaximizingPlayer) {
        int value = isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // Iterate over the possible moves
        for (EvaluatedMove move : moves) {

            // Apply the move
            board.applyMoveWithChecks(move);

            // Call the alphaBeta method recursively
            int nestedValue = alphaBeta(board, depth, alpha, beta, !isMaximizingPlayer);

            // Undo the move
            board.undoMove(move);

            // If the current player is maximizing
            if (isMaximizingPlayer) {
                // Maximize the value
                value = Math.max(value, nestedValue);
                // Update the alpha value
                alpha = Math.max(alpha, value);
            } else {
                // Minimize the value
                value = Math.min(value, nestedValue);
                // Update the beta value
                beta = Math.min(beta, value);
            }

            // If the beta value is less than or equal to the alpha value
            if (beta <= alpha)
                break;
        }
        return value;
    }
}
