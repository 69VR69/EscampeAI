package Engine.Board;

public class Move implements IMove
{
    // region Properties
    protected int _startPosition;
    protected int _endPosition;
    // endregion

    // region Constructors
    public Move()
    {
        _startPosition = -1;
        _endPosition = -1;
    }
    public Move(int startPosition, int endPosition)
    {
        _startPosition = startPosition;
        _endPosition = endPosition;
    }
    public Move(Move move)
    {
        _startPosition = move._startPosition;
        _endPosition = move._endPosition;
    }
    // endregion

    // region Methods
    @Override
    public String getString() {
        return "";
    }

    @Override
    public int getStartPosition() {
        return _startPosition;
    }

    @Override
    public int getEndPosition() {
        return _endPosition;
    }
    // endregion
}
