package Engine.Board;

public interface IMove {
    /**
     * Get the move string representation
     *
     * @return the move string representation
     */
    String getString();

    /**
     * Get the start position of the move
     */
    Position getStartPosition();
    
    /**
     * Get the end position of the move
     */
    Position getEndPosition();
}
