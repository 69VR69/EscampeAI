package Engine.Board;

public interface IPosition {
    /**
     * Get the line number
     * 
     */
    int getLine();

    /**
     * Get the column number
     * 
     */
    int getColumn();

    /**
     * Set the line and column number
     *
     * @param line the line number
     * @param column the column number
     */
    void setTo(int line, int column);

    /**
     * Get the string representation of the position like "A1"
     * @return the string representation of the position
     */
    String getBoardString();

    /**
     * Check if the position is in bounds of the board
     * @param size the size of the board
     */
    boolean isInBounds(int size);

    /**
     * Clone the position
     */
    Position clone();
}
