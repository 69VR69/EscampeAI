package Engine.Board;

public class Pawn implements IPawn {
    // region Constants
    protected static final int PAWN_SIZE = 4;
    // endregion

    // region Properties
    protected boolean _isWhite;
    protected boolean _isUnicorn;
    protected boolean _isOccupied;
    protected int _column;
    protected int _line;
    protected Position _position;
    // endregion

    // region Constructors
    public Pawn(boolean isWhite, boolean isUnicorn, boolean isOccupied) {
        _isWhite = isWhite;
        _isUnicorn = isUnicorn;
        _isOccupied = isOccupied;
    }

    public Pawn(int bits, int line, int column) {
        _isWhite = ((bits & 0b0100) == 0b0100);
        _isUnicorn = ((bits & 0b0010) == 0b0010);
        _isOccupied = ((bits & 0b0001) == 0b0001);
        _line = line;
        _column = column;
        _position = new Position(_line, _column);
    }
    // endregion

    // region Methods
    private boolean checkPosition(String a) {
        return (_column > 5 || _column < 0 || _line > 5 || _line < 0);
    }
    // endregion

    // region Getters and Setters
    @Override
    public boolean getIsWhite() {
        return _isWhite;
    }

    @Override
    public boolean getIsUnicorn() {
        return _isUnicorn;
    }

    @Override
    public boolean getIsOccupied() {
        return _isOccupied;
    }

    @Override
    public int getLineNumber() {
        return _line;
    }

    @Override
    public int getColumnNumber() {
        return _column;
    }

    @Override
    public int getLine() {
        int representation = 0;
        representation = representation | (_isWhite ? 0b100 : 0b000);
        representation = representation | (_isUnicorn ? 0b010 : 0b000);
        representation = representation | (_isOccupied ? 0b001 : 0b000);

        return representation << _column * PAWN_SIZE;
    }

    public Position getPosition() {
        return _position;
    }
    // endregion

    @Override
    public String toString() {
        return _isOccupied ?
                (_isWhite ? "White" : "Black") +
                        (_isUnicorn ? " Unicorn" : " Paladin") +
                        " at (" + (_line + 1) + ", " + (_column + 1) + ")" :
                "Empty cell at (" + (_line + 1) + ", " + (_column + 1) + ")";
    }

}
