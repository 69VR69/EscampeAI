package Engine.IA;

import Engine.Board.IBoard;
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
    public EvaluatedMove getBestMove(IBoard board, boolean isWhite) {
        // Security check
        if (board == null)
            return NothingMove;

        // Initialize the variables
        EvaluatedMove bestMove = NothingMove;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        IBoard tempBoard = board.Clone();

        // Call the alphaBeta method
        bestMove = alphaBeta(tempBoard, maxDepth, alpha, beta, isWhite);

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
     * @return the best move
     */
    protected EvaluatedMove alphaBeta(IBoard board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        // Security check
        if (board == null)
            return NothingMove;

        // If the depth is 0 or if the game is over, return the evaluation of the board
        if (depth == 0 || board.isGameOver())
            board.evaluate();

        // Initialize the variables
        EvaluatedMove bestMove = NothingMove;
        EvaluatedMove[] moves = (EvaluatedMove[]) board.getPossibleMoves(isMaximizingPlayer);

        // If there are no possible moves, return the current best move
        if (moves.length == 0)
            return bestMove;

        // If the current player is maximizing
        if (isMaximizingPlayer) {
            bestMove.setScore(Integer.MIN_VALUE);
            bestMove = CheckNestedMoves(board, bestMove, depth - 1, alpha, beta, moves, false);
        } else {
            bestMove.setScore(Integer.MAX_VALUE);
            bestMove = CheckNestedMoves(board, bestMove, depth - 1, alpha, beta, moves, true);
        }

        // Return the best move
        return bestMove;
    }

    /**
     * Check the nested moves
     *
     * @param board              the current board
     * @param bestMove           the best move
     * @param depth              the depth of the search
     * @param alpha              the alpha value
     * @param beta               the beta value
     * @param moves              the possible moves
     * @param isMaximizingPlayer true if the current player is maximizing, false otherwise
     * @return the best move
     */
    private EvaluatedMove CheckNestedMoves(IBoard board, EvaluatedMove bestMove, int depth, int alpha, int beta, EvaluatedMove[] moves, boolean isMaximizingPlayer) {
        // Iterate over the possible moves
        for (EvaluatedMove move : moves) {

            // Apply the move
            board.applyMove(move);

            // Call the alphaBeta method recursively
            EvaluatedMove currentMove = alphaBeta(board, depth, alpha, beta, isMaximizingPlayer);

            // Undo the move
            board.undoMove(move);

            if (isMaximizingPlayer) {
                // Max between the current move and the best move
                if (currentMove.getScore() > bestMove.getScore()) {
                    bestMove = currentMove;
                    alpha = currentMove.getScore();
                }
            } else {
                // Min between the current move and the best move
                if (currentMove.getScore() < bestMove.getScore()) {
                    bestMove = currentMove;
                    beta = currentMove.getScore();
                }
            }

            // If the beta value is less than or equal to the alpha value
            if (beta <= alpha)
                break;
        }
        return bestMove;
    }
}
