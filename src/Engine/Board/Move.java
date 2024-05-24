package Engine.Board;

public class Move implements IMove {
    // region Properties
    protected Position _startPosition;
    protected Position _endPosition;
    // endregion

    // region Constructors
    public Move() {
    	_startPosition = new Position();
    	_endPosition = new Position();
    }

    public Move(int startLine, int startColumn, int endLine, int endColumn) {
    	_startPosition = new Position(startLine, startColumn);
    	_endPosition = new Position(endLine, endColumn);
    }

    public Move(Position startPosition, Position endPosition) {
        _startPosition = startPosition;
        _endPosition = endPosition;
    }

    public Move(Move move) {
        _startPosition = move._startPosition;
        _endPosition = move._endPosition;
    }
    // endregion

    // region Methods
    @Override
    public String getString() {
        char letter = (char) ('A' + _startPosition.getColumn());
        return letter + Integer.toString(_startPosition.getLine());
    }

    @Override
    public Position getStartPosition() {
        return _startPosition;
    }

    @Override
    public Position getEndPosition() {
        return _endPosition;
    }
    // endregion
}
