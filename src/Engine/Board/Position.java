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
	// endregion
	
    // region Methods
	@Override
	public int getLine() {
		return _line;
	}

	@Override
	public int getColumn() {
		return _column;
	}
	// endregion
}
