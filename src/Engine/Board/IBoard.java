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
    IMove[] getPossibleMoves(boolean isWhite);

    /**
     * Clone the board
     * @return the cloned board
     */
    IBoard Clone();

    void evaluate();

    boolean isGameOver();

    void applyMove(IMove move);

    void undoMove(IMove move);
}
