package Engine.Board;

import Engine.Heuristics.DistanceToEnemy;
import Engine.Heuristics.HeuristicPipeline;
import Engine.IA.AlphaBetaAlgorithm;
import Engine.IA.EvaluatedMove;
import Engine.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Board implements IBoard {
    // region Constants
    protected static final int PAWN_SIZE = 4;
    protected static final int CELL_SIZE = 2;
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
    private Move _lastEnemyMove = null;
    private int _boardLineSize = 6;
    // endregion

    // region Constructors
    public Board(int size, HeuristicPipeline heuristicPipeline) {
        _boardLineSize = size;
        _bitBoard = new int[size];
        _bitCells = new short[size];
        _heuristicPipeline = heuristicPipeline;

        // Init bits to 0
        for (int i = 0; i < _bitBoard.length; i++)
            _bitCells[i] = EMPTY_CELL;
    }

    public Board(HeuristicPipeline heuristicPipeline) {
        this(6, heuristicPipeline);
    }

    public Board(Board board) {
        _boardLineSize = board._boardLineSize;
        _bitBoard = board._bitBoard.clone();
        _bitCells = board._bitCells.clone();
        _heuristicPipeline = board._heuristicPipeline;
        if (board._lastEnemyMove == null)
            _lastEnemyMove = null;
        else
            _lastEnemyMove = board._lastEnemyMove.clone();
    }
    // endregion

    // region Methods

    public void setBoardFromHexStrings(String[] bitBoard) {
        for (int i = 0; i < bitBoard.length; i++) {
            String inverseLine = new StringBuilder(bitBoard[i]).reverse().toString();
            _bitBoard[i] = Utils.HexToInt(inverseLine);
        }
    }

    public void setCellFromDecString(String[] bitCells) {
        // Iterate over each line of the bitCells, then iterate over each cell of the line and set the corresponding bits in the bitCells
        for (int i = 0; i < bitCells.length; i++) {
            short line = 0;
            String inverseLine = new StringBuilder(bitCells[i]).reverse().toString();
            for (int j = 0; j < bitCells[i].length(); j++) {
                int cell = (inverseLine.charAt(j) - '0');
                line <<= CELL_SIZE;
                line |= (short) cell;
            }
            _bitCells[i] = line;
        }
    }

    @Override
    public String getString() {
        //Iterate over each bit in the bitboard.
        //Determine the piece type (paladin or licorne) and player color based on the bit positions.
        //Construct the string representation accordingly.
        String header = "% ABCDEF";

        StringBuilder output = new StringBuilder(header + "\n");

        int row = 0;
        for (int i = 0; i < this._bitBoard.length; i++) {
            int line = this._bitBoard[i];
            output.append((i + 1)).append(" ");

            for (int j = 0; j < _bitBoard.length; j++) {
                int cell = line & 0b111;

                if ((cell & 0b001) != 0) {
                    switch (cell) {
                        case BLACK_PALADIN_CELL:
                            output.append("n");
                            break;
                        case BLACK_UNICORN_CELL:
                            output.append("N");
                            break;
                        case WHITE_PALADIN_CELL:
                            output.append("b");
                            break;
                        case WHITE_UNICORN_CELL:
                            output.append("B");
                            break;
                        default:
                            break;
                    }
                } else {
                    output.append("-");
                }
                line = line >> PAWN_SIZE;
            }
            output.append("\n");
        }

        return output.toString();
    }

    @Override
    public Move[] getPossibleMoves(boolean isWhite) {
        ArrayList<Move> moves = new ArrayList<>();

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
                int bits = (line >> (columnNumber * PAWN_SIZE)) & 0xF;
                Pawn pawn = new Pawn(bits, lineNumber, columnNumber);

                // If the pawn is not occupied, skip it
                if (!pawn._isOccupied) continue;

                // If the pawn is not of the current player, skip it
                if (pawn._isWhite != isWhite) continue;

                //System.out.println(pawn + " or " + pawn.getPosition().getBoardString());

                // Get the cell type of the pawn
                short cell = getCellFromPosition(pawn.getPosition());
                //System.out.println("Cell type is " + cell + " for pawn at " + pawn);

                // If the cell is an error, skip it
                if (cell == 0) continue;

                // If the cell doesn't correspond to last enemy move, skip it
                if (_lastEnemyMove != null) {
                    short enemyLastCell = getCellFromPosition(_lastEnemyMove.getEndPosition());
                    //System.out.println("Enemy last cell is " + enemyLastCell + " and pawn cell is " + cell);
                    if (cell != enemyLastCell) continue;
                }

                // Get the possible moves of the pawn
                Position pawnPos = pawn.getPosition();
                Position[] posToCheck;
                switch (cell) {
                    case 1: // Simple cell
                        posToCheck = getPosToCheckFor1(pawnPos);
                        break;
                    case 2: // Double cell
                        posToCheck = getPosToCheckFor2(pawnPos);
                        break;

                    case 3: // Triple cell
                        posToCheck = GetPosToCheckFor3(pawnPos);
                        break;

                    default:
                        posToCheck = new Position[0];
                        break;
                }

                //System.out.println("Possible positions to check : " + Arrays.toString(posToCheck));

                for (Position pos : posToCheck) {

                    //System.out.println("Checking position : " + pos.getBoardString() + " or " + pos);

                    // Check if the position is valid
                    if (!pos.isInBounds(_boardLineSize)) continue;

                    // Check if the cell at the new position is empty from bitboard
                    IPawn cellPawn = getPawnFromPosition(pos);
                    if (cellPawn.getIsOccupied()) continue;
                    //System.out.println(cellPawn + " is not occupied at pos " + pos.getBoardString());

                    // Check if the move is not already in the list
                    Move move = new Move(pawn.getPosition(), pos);
                    if (!moves.contains(move) && IsMoveValid(move)) {
                        //System.out.println("Adding move : " + move + " for pawn at " + pawn + " with cell type " + cell);
                        moves.add(move.clone());
                    }
                }
            }
        }
        if (!moves.isEmpty())
            System.out.println("Possible moves : " + Arrays.toString(moves.toArray()));
        return moves.toArray(new Move[0]);
    }

    private static Position[] getPosToCheckFor1(Position pawnPos) {
        return new Position[]{
                Utils.AddPosition(pawnPos, new Position(0, 1)),
                Utils.AddPosition(pawnPos, new Position(1, 0)),
                Utils.AddPosition(pawnPos, new Position(0, -1)),
                Utils.AddPosition(pawnPos, new Position(-1, 0))
        };
    }

    private static Position[] getPosToCheckFor2(Position pawnPos) {
        return new Position[]{
                Utils.AddPosition(pawnPos, new Position(0, 2)),
                Utils.AddPosition(pawnPos, new Position(1, 1)),
                Utils.AddPosition(pawnPos, new Position(2, 0)),
                Utils.AddPosition(pawnPos, new Position(1, -1)),
                Utils.AddPosition(pawnPos, new Position(0, -2)),
                Utils.AddPosition(pawnPos, new Position(-1, -1)),
                Utils.AddPosition(pawnPos, new Position(-2, 0)),
                Utils.AddPosition(pawnPos, new Position(-1, 1))
        };
    }

    private static Position[] GetPosToCheckFor3(Position pawnPos) {
        return new Position[]{
                Utils.AddPosition(pawnPos, new Position(0, 3)),
                Utils.AddPosition(pawnPos, new Position(1, 2)),
                Utils.AddPosition(pawnPos, new Position(2, 1)),
                Utils.AddPosition(pawnPos, new Position(3, 0)),
                Utils.AddPosition(pawnPos, new Position(2, -1)),
                Utils.AddPosition(pawnPos, new Position(1, -2)),
                Utils.AddPosition(pawnPos, new Position(0, -3)),
                Utils.AddPosition(pawnPos, new Position(-1, -2)),
                Utils.AddPosition(pawnPos, new Position(-2, -1)),
                Utils.AddPosition(pawnPos, new Position(-3, 0)),
                Utils.AddPosition(pawnPos, new Position(-2, 1)),
                Utils.AddPosition(pawnPos, new Position(-1, 2))
        };
    }

    public Pawn getPawnFromPosition(Position position) {
        // Check if the position is valid
        if (position.getLine() < 0 || position.getLine() >= _bitBoard.length || position.getColumn() < 0 || position.getColumn() >= _boardLineSize)
            return new Pawn(false, false, true);

        // Get the line of the pawn
        int line = _bitBoard[position.getLine()];

        // Get the bits of the pawn
        int bits = (line >> (position.getColumn() * PAWN_SIZE)) & 0xF;

        return new Pawn(bits, position.getLine(), position.getColumn());
    }

    private boolean containsSpecificPiece(int line, int piece) {
        // The mask consist of 6 times the piece value, for example if the piece is a paladin, the mask will be 0x111111
        int mask = piece * 0x111111;
        return containsMask(line, mask);
    }

    private boolean containsMask(int line, int mask) {
        // The mask consist of 6 times the piece value, for example if the piece is a paladin, the mask will be 0x111111
        return (line & mask) != 0;
    }

    private short getCellFromPosition(Position position) {
        // Check if the position is valid
        if (position.getLine() < 0 || position.getLine() >= _bitCells.length || position.getColumn() < 0 || position.getColumn() >= _boardLineSize)
            return 0;

        short cellLine = _bitCells[position.getLine()];
        int temp = cellLine >> (position.getColumn() * CELL_SIZE);
        return (short) (temp & 0b11);
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
        // Check if the board contains a unicorn for each player
        boolean whiteUnicorn = false;
        boolean blackUnicorn = false;
        for (int line : _bitBoard) {
            for (int j = 0; j < _boardLineSize; j++) {
                int bits = (line >> (j * PAWN_SIZE)) & 0xF;
                if ((bits & 0x3) == 0x3) {
                    if ((bits & 0x4) == 0x4) {
                        whiteUnicorn = true;
                    } else {
                        blackUnicorn = true;
                    }
                }
            }
        }
        if (!whiteUnicorn || !blackUnicorn) return true;

        // Check if both player are blocked
        boolean whiteBlocked = true;
        boolean blackBlocked = true;
        for (int i = 0; i < _bitBoard.length; i++) {
            int line = _bitBoard[i];
            for (int j = 0; j < _boardLineSize; j++) {
                int bits = (line >> (j * PAWN_SIZE)) & 0xF;
                if ((bits & 0x1) == 0x1) {
                    if (bits == 0x1) {
                        whiteBlocked = false;
                    } else {
                        blackBlocked = false;
                    }
                }
            }
        }

        if (whiteBlocked || blackBlocked) return true;

        return false;
    }

    @Override
    public boolean isEmpty() {
        for (int j : _bitBoard)
            if (j != 0) return false;

        return true;
    }

    @Override
    public void applyMove(IMove move, boolean bypassChecks) {
        //Apply the move to the board.
        if (!bypassChecks && !IsMoveValid(move)) return;

        int startBinaryLine = this._bitBoard[move.getStartPosition().getLine()];
        int startColumn = move.getStartPosition().getColumn();
        int endBinaryLine = this._bitBoard[move.getEndPosition().getLine()];
        int endColumn = move.getEndPosition().getColumn();

        System.out.println("currently applying move : " + move + " on startBinaryLine " + Utils.IntToHex(startBinaryLine) + " and endBinaryLine " + Utils.IntToHex(endBinaryLine));// + " to the board " + this);

        int extractMask = (1 << PAWN_SIZE) - 1;

        if (move.isInline()) {
            // Extract the bits of the start and end position
            int block1 = (startBinaryLine >> (startColumn * PAWN_SIZE)) & extractMask;
            int block2 = (startBinaryLine >> (endColumn * PAWN_SIZE)) & extractMask;

            System.out.println("block1 : " + Utils.IntToBinary(block1, 4) + " block2 : " + Utils.IntToBinary(block2,4));
            System.out.println(" get from (" + startColumn + " to " + endColumn);

            // Clear the bits of the start and end position
            startBinaryLine &= ~(extractMask << (startColumn * PAWN_SIZE));
            startBinaryLine &= ~(extractMask << (endColumn * PAWN_SIZE));

            System.out.println("startBinaryLine after clearing : " + Utils.IntToHex(startBinaryLine) + " or " + Utils.IntToBinary(startBinaryLine));

            // Swap the bits of the start and end position
            startBinaryLine |= (block2 << (startColumn * PAWN_SIZE));
            System.out.println("startBinaryLine 1 after swapping : " + Utils.IntToHex(startBinaryLine) + " or " + Utils.IntToBinary(startBinaryLine));

            startBinaryLine |= (block1 << (endColumn * PAWN_SIZE));
            System.out.println("startBinaryLine 2 after swapping : " + Utils.IntToHex(startBinaryLine) + " or " + Utils.IntToBinary(startBinaryLine));

            // Update the bitboard
            this._bitBoard[move.getStartPosition().getLine()] = startBinaryLine;

        } else {
            // Extract the bits of the start and end position
            int block1 = (startBinaryLine >> (startColumn * PAWN_SIZE)) & extractMask;
            int block2 = (endBinaryLine >> (endColumn * PAWN_SIZE)) & extractMask;

            // Clear the bits of the start and end position
            startBinaryLine &= ~(extractMask << (startColumn * PAWN_SIZE));
            endBinaryLine &= ~(extractMask << (endColumn * PAWN_SIZE));
            // Swap the bits of the start and end position
            startBinaryLine |= (block2 << (startColumn * PAWN_SIZE));
            endBinaryLine |= (block1 << (endColumn * PAWN_SIZE));

            this._bitBoard[move.getStartPosition().getLine()] = startBinaryLine;
            this._bitBoard[move.getEndPosition().getLine()] = endBinaryLine;
        }
        System.out.println("after applying move : " + move + " with startBinaryLine " + Utils.IntToHex(startBinaryLine) + " and endBinaryLine " + Utils.IntToHex(endBinaryLine) + " to the board \n" + this);


        // count the verify that there is still 6 pawns ( 1 unicorn + 5 paladins) of each player
        int whiteUnicornCount = 0;
        int blackUnicornCount = 0;
        int whitePaladinCount = 0;
        int blackPaladinCount = 0;
        for (int line : _bitBoard) {
            for (int j = 0; j < _boardLineSize; j++) {
                int bits = (line >> (j * PAWN_SIZE)) & 0xF;

                if (BLACK_PALADIN_CELL == bits)
                    blackPaladinCount++;
                else if (BLACK_UNICORN_CELL == bits)
                    blackUnicornCount++;
                else if (WHITE_PALADIN_CELL == bits)
                    whitePaladinCount++;
                else if (WHITE_UNICORN_CELL == bits)
                    whiteUnicornCount++;
            }
        }
        System.out.println("White unicorn count : " + whiteUnicornCount + " Black unicorn count : " + blackUnicornCount + " White paladin count : " + whitePaladinCount + " Black paladin count : " + blackPaladinCount);
        if (whiteUnicornCount != 1 || blackUnicornCount != 1 || whitePaladinCount != 5 || blackPaladinCount != 5)
            throw new RuntimeException("Invalid number of pawns");

    }

    @Override
    public void applyMoveWithChecks(IMove move) {
        applyMove(move, false);
    }

    @Override
    public void undoMove(IMove move) {
        applyMove(Utils.GetInverseMove(move), true);
    }

    public boolean IsMoveValid(EvaluatedMove move) {
        return IsMoveValid(new Move(move.getStartPosition(), move.getEndPosition()));
    }

    public boolean IsMoveValid(IMove move) {
        final boolean DEBUG = false; // Set to true to enable debug messages

        //Check if the start position is valid.
        if (move.getStartPosition().getLine() < 0 || move.getStartPosition().getLine() >= _bitBoard.length || move.getStartPosition().getColumn() < 0 || move.getStartPosition().getColumn() >= _boardLineSize)
            return false;
        //System.out.println("Start position is valid");

        //Check if the end position is valid.
        if (move.getEndPosition().getLine() < 0 || move.getEndPosition().getLine() >= _bitBoard.length || move.getEndPosition().getColumn() < 0 || move.getEndPosition().getColumn() >= _boardLineSize)
            return false;
        //System.out.println("End position is valid");

        //Check if the start position is occupied.
        int startLine = this._bitBoard[move.getStartPosition().getLine()];
        int startColumn = move.getStartPosition().getColumn();
        int startBits = (startLine >> (startColumn * PAWN_SIZE)) & 0xF;
        if ((startBits & 0x1) == 0)
            return false;
        //System.out.println("Start position is occupied");

        //Check if the end position is not occupied.
        int endLine = this._bitBoard[move.getEndPosition().getLine()];
        int endColumn = move.getEndPosition().getColumn();
        int endBits = (endLine >> (endColumn * PAWN_SIZE)) & 0xF;
        if ((endBits & 0x1) == 1)
            return false;
        //System.out.println("End position is not occupied");

        // Check if a path exist between the start and end position not occupied using pathfinding
        short cell = getCellFromPosition(move.getStartPosition());
        if (cell == 2 || cell == 3) {
            if (DEBUG)
                System.out.println("Checking path between " + move.getStartPosition().getBoardString() + " and " + move.getEndPosition().getBoardString());

            int xDir = move.getEndPosition().getColumn() - move.getStartPosition().getColumn();
            int yDir = move.getEndPosition().getLine() - move.getStartPosition().getLine();

            int xDirAbs = Math.abs(xDir);
            int yDirAbs = Math.abs(yDir);

            int xDirSign = (xDirAbs != 0) ? (xDir / xDirAbs) : 0;
            int yDirSign = (yDirAbs != 0) ? (yDir / yDirAbs) : 0;

            int x = move.getStartPosition().getColumn();
            int x1 = x;
            int y = move.getStartPosition().getLine();
            int y1 = y;

            if (xDirAbs + yDirAbs == 0) return false;
            if (xDirAbs + yDirAbs > cell) return false;

            // Check horizontal then vertical path
            boolean isHVPathBlocked = false;
            for (int i = 0; i < xDirAbs; i++) {
                x += xDirSign;
                Position pos = new Position(y, x);
                if (DEBUG)
                    System.out.println("X(HV) - Checking path at " + pos.getBoardString());
                Pawn p;
                if ((p = getPawnFromPosition(pos)).getIsOccupied()) {
                    if (DEBUG)
                        System.out.println("Path is blocked by " + p);
                    isHVPathBlocked = true;
                }
            }

            for (int i = 0; i < yDirAbs; i++) {
                y += yDirSign;
                Position pos = new Position(y, x);
                if (DEBUG)
                    System.out.println("Y(HV) - Checking path at " + pos.getBoardString());
                Pawn p;
                if ((p = getPawnFromPosition(pos)).getIsOccupied()) {
                    if (DEBUG)
                        System.out.println("Path is blocked by " + p);
                    isHVPathBlocked = true;
                }
            }

            // Check vertical then horizontal path
            x = x1;
            y = y1;

            boolean isVHPathBlocked = false;
            for (int i = 0; i < yDirAbs; i++) {
                y += yDirSign;
                Position pos = new Position(y, x);
                if (DEBUG)
                    System.out.println("Y(VH) - Checking path at " + pos.getBoardString());
                Pawn p;
                if ((p = getPawnFromPosition(pos)).getIsOccupied()) {
                    if (DEBUG)
                        System.out.println("Path is blocked by " + p);
                    isVHPathBlocked = true;
                }
            }

            for (int i = 0; i < xDirAbs; i++) {
                x += xDirSign;
                Position pos = new Position(y, x);
                if (DEBUG)
                    System.out.println("X(VH) - Checking path at " + pos.getBoardString());
                Pawn p;
                if ((p = getPawnFromPosition(pos)).getIsOccupied()) {
                    if (DEBUG)
                        System.out.println("Path is blocked by " + p);
                    isVHPathBlocked = true;
                }
            }

            if (isHVPathBlocked && isVHPathBlocked) {
                if (DEBUG)
                    System.out.println("Path is blocked");
                return false;
            } else {
                if (DEBUG)
                    System.out.println("Path is not blocked");
            }
        }


        if (_lastEnemyMove != null) {
            //Check if the start position corresponds to the last enemy move.
            short enemyLastCell = getCellFromPosition(_lastEnemyMove.getEndPosition());
            return cell == enemyLastCell;
            //System.out.println("Start position corresponds to the last enemy move");
        }

        return true;
    }

    @Override
    public String getInitialisationMove(boolean isWhite) {
        if (isWhite) {
            return "A1/A2/B1/B2/E2/D1";
        } else {
            return "A6/A5/B6/B5/E5/D6";
        }
    }

    @Override
    public void applyInitialisationMove(String initMove, boolean isWhite) {
        //Apply the initialisation move to the board.
        String[] poss = initMove.split("/");
        boolean isUnicorn = true;
        for (String pos : poss) {
            Position position = Position.getPositionFromString(pos);
            int cellType = isUnicorn ? (isWhite ? WHITE_UNICORN_CELL : BLACK_UNICORN_CELL) : (isWhite ? WHITE_PALADIN_CELL : BLACK_PALADIN_CELL);
            _bitBoard[position.getLine()] |= cellType << (position.getColumn() * PAWN_SIZE);

            isUnicorn = false;
        }
    }

    @Override
    public IPawn getPlayerUnicorn() {
        for (int i = 0; i < _bitBoard.length; i++) {
            int line = _bitBoard[i];
            for (int j = 0; j < _boardLineSize; j++) {
                int bits = (line >> (j * PAWN_SIZE)) & 0xF;
                if ((bits & 0x3) == 0x3) {
                    return new Pawn(bits, i, j);
                }
            }
        }

        return Utils.NullPawn();
    }

    // endregion

    // region Getters & Setters
    public Move getLastEnemyMove() {
        return _lastEnemyMove;
    }

    public void setLastEnemyMove(Move lastEnemyMove) {
        _lastEnemyMove = lastEnemyMove;
    }

    public int getBoardLineSize() {
        return _boardLineSize;
    }
    // endregion

    @Override
    public String toString() {
        /*StringBuilder sb = new StringBuilder();

        for (int i = 0, bitBoardLength = _bitBoard.length; i < bitBoardLength; i++) {
            int line = _bitBoard[i];
            sb.append(i + 1)
              .append(" : ");

            // If the line is not boardSize, add the missing 0 at the beginning
            String hexLine = Utils.IntToHex(line);
            if (hexLine.length() < _boardLineSize)
                hexLine = "0".repeat(_boardLineSize - hexLine.length()) + hexLine;

            sb.append(hexLine);
            if (i != bitBoardLength - 1)
                sb.append("\n");
        }

        return sb.toString();*/
        return getString();
    }

    public static void main(String[] args) {
        // Create a hex strings representing the board (during a game)
        String[] bitBoard = new String[]{
                //"300000",
                //"101100",
                //"557050",
                "000000",
                "000000",
                "000000",
                "005000",
                //"500110",
                "000000",
                "000000"
        };

        // Create a hex strings representing the cells of the board
        String[] bitCells = new String[]{
                "122312",
                "313132",
                "231213",
                "213231",
                "131312",
                "322132"
        };

        // Create HeuristicPipeline
        DistanceToEnemy dte = new DistanceToEnemy();
        HeuristicPipeline heuristicPipeline = new HeuristicPipeline(new HashMap<>() {
            {
                put(dte, 1f);
            }
        });

        // Create fake last enemy move
        Move lastEnemyMove = new Move(new Position(0, 0), new Position(0, 0));

        // Create a new board
        Board board = new Board(heuristicPipeline);
        board.setCellFromDecString(bitCells);
        board.setBoardFromHexStrings(bitBoard);
        board.setLastEnemyMove(lastEnemyMove);

        // Print the board
        System.out.println(board);

        // Print the cells
        for (int i = 0; i < board._bitCells.length; i++) {
            System.out.println(Utils.IntToBinary(board._bitCells[i]));
        }

        System.out.println();

        // Test getPossibleMoves
        IMove[] possibleMoves = board.getPossibleMoves(false);
        System.out.println("Possible moves : ");
        for (IMove move : possibleMoves) {
            System.out.println(move);
        }

        // Test initialisation move
        /*System.out.println(board.getInitialisationMove(true));
        System.out.println(board);
        board.applyInitialisationMove(board.getInitialisationMove(true), true);
        System.out.println(board);
        board.applyInitialisationMove(board.getInitialisationMove(false), false);
        System.out.println(board);
*/
        // Test AI move
       /* System.out.println("AI Move with last enemy move : " + lastEnemyMove);
        AlphaBetaAlgorithm ab = new AlphaBetaAlgorithm(3);
        Move aiMove = ab.getBestMove(board, true);
        System.out.println(aiMove);
        board.applyMoveWithChecks(aiMove);

        System.out.println("Moves");
        // Apply a move
        board.setLastEnemyMove(new Move(new Position(0, 0), new Position(0, 1)));
        System.out.println(board.getPawnFromPosition(new Position(0, 0)));
        board.applyMoveWithChecks(new Move(new Position(0, 0), new Position(2, 0)));
        System.out.println(board);*/
        board.setLastEnemyMove(new Move(new Position(0, 1), new Position(0, 0)));
        board.applyMoveWithChecks(new Move(new Position(2, 3), new Position(3, 1)));
        System.out.println(board);
    }
}
