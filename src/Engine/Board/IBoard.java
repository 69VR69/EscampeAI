package Engine.Board;

public interface IBoard {

    /**
     * Set up a board from list of pawns
     *
     * @param pawns the list of pawns
     */
    public void setPawnsOnBoard(IPawn[] pawns);

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
}
