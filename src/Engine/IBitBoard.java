package Engine;

import java.util.BitSet;
import java.util.List;

public interface IBitBoard {


    public List<IBitPawn> getPawns();
    public List<IBitPawn> getPawns(boolean playerColor);

    public IBitBoard initFromString(String s);

    public String getString();
}
