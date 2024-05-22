package Engine.Board;

public class Pawn implements IPawn {
    // region Constants
    protected static final int PAWN_SIZE = 4;
    // endregion
    
	protected boolean __isWhite;
	protected boolean __isUnicorn;
	protected boolean __isOccupied;
	protected int __column;
	protected int __line;
	
	public Pawn(boolean b, boolean c, boolean d) {
		__isWhite = b;
		__isUnicorn = c;
		__isOccupied = d;
	}
	
	public Pawn(int bits) {
		__isWhite = ((bits & 0b0100) == 0b0100);
		__isUnicorn = ((bits & 0b0010) == 0b0010);
		__isOccupied = ((bits & 0b0001) == 0b0001);
	}
	
	private boolean checkPosition(String a) {
		return  (__column > 5 || __column < 0 || __line > 5 || __line < 0);
	}

	@Override
	public boolean getIsWhite() {
		return __isWhite;
	}

	@Override
	public boolean getIsUnicorn() {
		return __isUnicorn;
	}

	@Override
	public boolean getIsOccupied() {
		return __isOccupied;
	}

	@Override
	public int getLineNumber() {
		return __line;
	}

	@Override
	public int getColumnNumber() {
		return __column;
	}

	@Override
	public int getLine() {
		int representation = 0;
		representation = representation | (__isWhite ? 0b100 : 0b000);
		representation = representation | (__isUnicorn ? 0b010 : 0b000);
        representation = representation | (__isOccupied ? 0b001 : 0b000);
        
		return representation << __column * PAWN_SIZE;
	}

}
