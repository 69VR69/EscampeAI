package Engine.Board;

public class Board implements IBoard
{
    // region Properties
    // Board representing the lines of the board, by pawns (IsWhite, IsUnicorn, IsOccupied)
    protected int[] _bitBoard;
    // Board representing the cells of the board by type (simple, double, triple, error)
    protected short[] _bitCells;
    // endregion

    // region Constructors
    public Board(int width, int height)
    {
        _bitBoard = new int[width * height];
        _bitCells = new short[width * height];
    }
    // endregion

    // region Methods
    @Override
    public void setPawnsOnBoard(IPawn[] pawns) {
        //Iterate over each pawn in the list.
        //Determine the corresponding bit positions based on the piece type, player color, and whether the cell is occupied or not.
        //Set the corresponding bits in the bitboard.
    }

    @Override
    public String getString() {
        //Iterate over each bit in the bitboard.
        //Determine the piece type (paladin or licorne) and player color based on the bit positions.
        //Construct the string representation accordingly.
        return null;
    }

    @Override
    public IMove[] getPossibleMoves(boolean isWhite) {
        return new IMove[0];
    }
    // endregion
}
