package Engine;

import Engine.Board.Move;

public class Utils
{
    /**
     * Get the nothing move
     * @return the nothing move
     */
    public static Move NothingMove()
    {
        if(_nothingMove == null)
        {
            _nothingMove = new Move() {};
        }
        return _nothingMove;
    }
    private static Move _nothingMove;
}
