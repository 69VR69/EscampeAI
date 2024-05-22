package Engine.Board;

import Engine.Heuristics.HeuristicPipeline;
import Engine.Utils;

import java.util.ArrayList;

public class Board implements IBoard
{
    // region Constants
    protected static final int PAWN_SIZE = 4;
    protected static final int EMPTY_CELL = 0x0;
    protected static final int BLACK_PALADIN_CELL = 0x1;
    protected static final int BLACK_UNICORN_CELL = 0x3;
    protected static final int WHITE_PALADIN_CELL = 0x5;
    protected static final int WHITE_UNICORN_CELL = 0x7;
    // endregion

    // region Properties
    // Board representing the lines of the board, each line contains 6 pawns represented as 3 bits (IsWhite, IsUnicorn, IsOccupied)
    protected int[] _bitBoard;
    // Board representing the cells of the board by type (simple, double, triple, error)
    protected short[] _bitCells;
    private final HeuristicPipeline _heuristicPipeline;
    // Last move made by the enemy
    private IMove _lastEnemyMove = Utils.NothingMove();
    private int _boardLineSize = 6;
    // endregion

    // region Constructors
    public Board(int size, HeuristicPipeline heuristicPipeline)
    {
        _boardLineSize = size;
        _bitBoard = new int[size];
        _bitCells = new short[size];
        _heuristicPipeline = heuristicPipeline;
    }

    public Board(HeuristicPipeline heuristicPipeline)
    {
        this(6, heuristicPipeline);
    }

    public Board(Board board)
    {
        _boardLineSize = board._boardLineSize;
        _bitBoard = board._bitBoard.clone();
        _bitCells = board._bitCells.clone();
        _heuristicPipeline = board._heuristicPipeline;
        _lastEnemyMove = board._lastEnemyMove;
    }
    // endregion

    // region Methods

    public void setBoardFromHexStrings(String[] bitBoard)
    {
        for (int i = 0; i < bitBoard.length; i++) {
            _bitBoard[i] = Utils.HexToInt(bitBoard[i]);
        }
    }

    public void setCellFromHexStrings(String[] bitCells)
    {
        for (int i = 0; i < bitCells.length; i++) {
            _bitCells[i] = (short) Utils.HexToInt(bitCells[i]);
        }
    }

    @Override
    public void setPawnsOnBoard(IPawn[] pawns) {
        //Iterate over each pawn in the list.
        //Determine the corresponding bit positions based on the piece type, player color, and whether the cell is occupied or not.
        //Set the corresponding bits in the bitboard.
    	
    	// Init to blank
    	for (int i = 0; i<this._bitBoard.length; i++) {
    		this._bitBoard[i] = 0;
		} 
    	
    	// Place pawns
        for (IPawn pawn : pawns) {          
            this._bitBoard[pawn.getLineNumber()] |= pawn.getLine();
        }
    }

    @Override
    public String getString() {
        //Iterate over each bit in the bitboard.
        //Determine the piece type (paladin or licorne) and player color based on the bit positions.
        //Construct the string representation accordingly.
        return null;
    }

    @Override
    public IMove[] getPossibleMoves(boolean isWhite) {
        ArrayList<IMove> moves = new ArrayList<>();

        int unicorn = isWhite ? WHITE_UNICORN_CELL : BLACK_UNICORN_CELL;
        int paladin = isWhite ? WHITE_PALADIN_CELL : BLACK_PALADIN_CELL;

        //Iterate over each bit in the bitboard.
        for (int lineNumber = 0; lineNumber < _bitBoard.length; lineNumber++) {
            int line = _bitBoard[lineNumber];

            // If the line is empty, skip it
            if (line == 0) continue;

            // If the line does not contain a pawn of the current player, skip it
            if (!containsSpecificPiece(line, unicorn) && !containsSpecificPiece(line, paladin)) continue;

            // Iterate of each pawns in the line
            for (int columnNumber = 0; columnNumber < _boardLineSize; columnNumber++) {
                // Extract the PAWN_SIZE bits from the line
                int bits = (line >> (columnNumber * PAWN_SIZE)) & 0x7;
                Pawn pawn = new Pawn(bits, lineNumber, columnNumber);
                if(!pawn.__isOccupied) continue;

                System.out.println("Line " + lineNumber);
                System.out.println("Bits : " + Utils.IntToBinary(bits) + " -> " + Utils.IntToBinary(line) + " >> " + (columnNumber * PAWN_SIZE) + " & " + Utils.IntToBinary(0x7));
                System.out.println("Pawn : " + pawn);
            }
        }

        return moves.toArray(new IMove[0]);
    }

    private boolean containsSpecificPiece(int line, int piece) {
        // The mask consist of 6 times the piece value, for example if the piece is a paladin, the mask will be 0x111111
        int mask = piece * 0x111111;
        return  containsMask(line, mask);
    }
    private boolean containsMask(int line, int mask) {
        // The mask consist of 6 times the piece value, for example if the piece is a paladin, the mask will be 0x111111
        return (line & mask) != 0;
    }

    private boolean containsWhitePaladin(int line) {
        return (line & 0x555555) != 0;
    }

    private boolean containsWhiteUnicorn(int line) {
        return (line & 0x777777) != 0;
    }

    private boolean containsBlackPaladin(int line) {
        return (line & 0x1111111) != 0;
    }

    private boolean containsBlackUnicorn(int line) {
        return (line & 0x3333333) != 0;
    }

    @Override
    public IBoard Clone() {
        return new Board(this);
    }

    @Override
    public int evaluate() {
        return _heuristicPipeline.evaluate(this);
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public void applyMove(IMove move, boolean bypassChecks) {
        //Apply the move to the board.
        if(bypassChecks || IsMoveValid(move))
        {
            int tmp = this._bitBoard[move.getEndPosition()];
    		this._bitBoard[move.getEndPosition()] = this._bitBoard[move.getStartPosition()];
    		this._bitBoard[move.getStartPosition()] = tmp;
        }
    }

    @Override
    public void applyMoveWithChecks(IMove move) {
        applyMove(move, false);
    }

    @Override
    public void undoMove(IMove move) {
        applyMove(Utils.GetInverseMove(move), true);
    }

    private boolean IsMoveValid(IMove move) {
        //Check if the move is valid.
        return false;
    }

    // endregion

    // region Getters & Setters
    public IMove getLastEnemyMove() {
        return _lastEnemyMove;
    }
    public void setLastEnemyMove(IMove lastEnnemyMove) {
        _lastEnemyMove = lastEnnemyMove;
    }
    // endregion

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, bitBoardLength = _bitBoard.length; i < bitBoardLength; i++) {
            int line = _bitBoard[i];
            sb.
                    append(i+1)
                    .append(" : ");

            // If the line is not boardSize, add the missing 0 at the beginning
            String hexLine = Utils.IntToHex(line);
            if(hexLine.length() < _boardLineSize)
                hexLine = "0".repeat(_boardLineSize - hexLine.length()) + hexLine;

            sb
                    .append(hexLine)
                    .append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        // Create a hex strings representing the board (during a game)
        String[] bitBoard = new String[] {
                "311000",
                "101100",
                "557050",
                "005000",
                "500000",
                "000000"
        };

        // Create a new board
        Board board = new Board((HeuristicPipeline) null);
        board.setBoardFromHexStrings(bitBoard);

        // Print the board
        System.out.println(board);

        // Test getPossibleMoves
        board.getPossibleMoves(true);
    }
}
