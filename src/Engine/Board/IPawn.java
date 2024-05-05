package Engine.Board;

public interface IPawn
{
    // region Properties
    /**
     * Get the player color
     * @return true if the player is white, false otherwise
     */
    boolean getIsWhite();
    /**
     * Get the pawn type
     * @return true if the pawn is a unicorn, false otherwise
     */
    boolean getIsUnicorn();
    /**
     * Get the cell occupation
     * @return true if the cell is occupied, false otherwise
     */
    boolean getIsOccupied();
    // endregion
}
