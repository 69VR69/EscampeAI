package Engine.Board;

import Engine.IA.EvaluatedMove;

public interface IBoard
{
    /**
     * Get the string representation of the board
     *
     * @return the string representation of the board
     */
    public String getString();

    /**
     * Get the possible moves for the current player
     *
     * @param isWhite true if the current player is white, false otherwise
     * @return an array of possible moves
     */
    Move[] getPossibleMoves(boolean isWhite);

    /**
     * Clone the board
     *
     * @return the cloned board
     */
    IBoard Clone();

    /**
     * Evaluate the board
     *
     * @return the evaluation of the board
     */
    int evaluate();

    /**
     * Check if the game is over
     *
     * @return true if the game is over, false otherwise
     */
    boolean isGameOver();

    /**
     * Apply a move to the board
     *
     * @param move         the move to apply
     * @param bypassChecks true if the move should be applied without checks, false otherwise
     */
    void applyMove(IMove move, boolean bypassChecks);

    /**
     * Apply a move to the board with legal checks
     *
     * @param move the move to apply
     */
    void applyMoveWithChecks(IMove move);

    /**
     * Undo a move from the board
     *
     * @param move the move to undo
     */
    void undoMove(IMove move);

    /**
     * Check if the board is empty
     *
     * @return true if the board is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Get the initialisation move
     *
     * @param isWhite true if the current player is white, false otherwise
     * @return the initialisation move
     */
    String getInitialisationMove(boolean isWhite);

    /**
     * Apply the initialisation move
     *
     * @param initMove the initialisation move
     * @param s        true if the current player is white, false otherwise
     */
    void applyInitialisationMove(String initMove, boolean s);

    /**
     * Get the player's unicorn
     *
     * @return the player's unicorn
     */
    IPawn getPlayerUnicorn();

    /**
     * Get the board line size
     *
     * @return the board line size
     */
    int getBoardLineSize();

    /**
     * Get the pawn from a position
     *
     * @param pos the position
     * @return the pawn from the position
     */
    IPawn getPawnFromPosition(Position pos);

    /**
     * Set the pawn to a position
     * @param bitCells the bit cells
     */
    void setCellFromDecString(String[] bitCells);

    /**
     * Set the last enemy move
     * @param lastEnemyMove the last enemy move
     */
    void setLastEnemyMove(Move lastEnemyMove);

    /**
     * Get the last enemy move
     * @return the last enemy move
     */
    boolean IsMoveValid(IMove move);

    /**
     * Get the last enemy move
     * @return the last enemy move
     */
    boolean IsMoveValid(EvaluatedMove move);

    /**
     * Get the last enemy move
     * @return the last enemy move
     */
    Move getLastEnemyMove();
}
