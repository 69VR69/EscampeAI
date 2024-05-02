package Engine;

import java.util.BitSet;
import java.util.List;

public class Board implements IBitBoard
{
    // region Properties
    protected long _bitBoard;
    protected long _bitCells;
    // endregion

    // region Constructors
    public Board(int width, int height)
    {
        _bitBoard = new BitSet(width * height);
    }
    // endregion

    // region Methods
    @Override
    public List<IBitPawn> getPawns() {
        return List.of();
    }

    @Override
    public List<IBitPawn> getPawns(boolean playerColor) {
        return List.of();
    }

    @Override
    public IBitBoard initFromString(String s) {
        //Iterate over each character in the string representation.
        //Determine the corresponding bit positions based on the piece type, player color, and whether the cell is occupied or not.
        //Set the corresponding bits in the bitboard.
        return null;
    }

    @Override
    public String getString() {
        //Iterate over each bit in the bitboard.
        //Determine the piece type (paladin or licorne) and player color based on the bit positions.
        //Construct the string representation accordingly.
        return null;
    }
    // endregion
}
