package Engine;

import java.util.BitSet;

public class Pawn implements  IBitPawn
{
    protected int _bitPawn;

    public Pawn()
    {
        // Set the last bit to 1 as a pawn always occupy the cell
        _bitPawn = 0b001;
    }
}
