package Engine;

import Engine.Board.IMove;
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


    /**
     * Get the inverse of a move
     * @param move the move
     * @return the inverse of the move
     */
    public static Move GetInverseMove(IMove move)
    {
        return new Move(move.getEndPosition(), move.getStartPosition());
    }

}
