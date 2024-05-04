package Engine;

import java.util.List;

public interface IBoard {


    public List<IPawn> getPawns();
    public List<IPawn> getPawns(boolean playerColor);

    public IBoard initFromString(String s);

    public String getString();
}
