package Engine.Board;

public class Position implements IPosition {
	// region Properties
	private int _line;
	private int _column;
    // endregion
	
    // region Constructors
	public Position() {
		_line = -1;
		_column = -1;
	}
	
	public Position(int line, int column) {
		this._line = line;
		this._column = column;
	}

	public Position(Position position) {
		_line = position.getLine();
		_column = position.getColumn();
	}
	// endregion

	// region Methods
	public void add(IPosition position) {
		_line += position.getLine();
		_column += position.getColumn();
	}

	@Override
	public String getBoardString() {
		char letter = (char) ('A' + _column);
		return letter + Integer.toString(_line + 1);
	}

	public static Position getPositionFromString(String pos)
	{
		int line = Integer.parseInt(pos.substring(1)) - 1;
		int column = pos.charAt(0) - 'A';
		return new Position(line, column);
	}

	@Override
	public boolean isInBounds(int size) {
		return _line >= 0 && _line < size && _column >= 0 && _column < size;
	}
	// endregion
	
    // region Getters & Setters
	@Override
	public int getLine() {
		return _line;
	}

	@Override
	public int getColumn() {
		return _column;
	}

	@Override
	public void setTo(int line, int column) {
		_line = line;
		_column = column;
	}
	// endregion

	@Override
	public String toString() {
		return "(" + _line + ", " + _column + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Position other)) {
			return false;
		}

        return _line == other.getLine() && _column == other.getColumn();
	}

	public static void main(String[] args) {
		String test = "F6";
		Position pos = Position.getPositionFromString(test);
		System.out.println(pos);
	}
}
