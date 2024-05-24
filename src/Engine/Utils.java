package Engine;

import Engine.Board.IMove;
import Engine.Board.Move;

public class Utils {
    /**
     * Get the nothing move
     *
     * @return the nothing move
     */
    public static Move NothingMove() {
        if (_nothingMove == null) {
            _nothingMove = new Move() {
            };
        }
        return _nothingMove;
    }

    private static Move _nothingMove;


    /**
     * Get the inverse of a move
     *
     * @param move the move
     * @return the inverse of the move
     */
    public static Move GetInverseMove(IMove move) {
        return new Move(move.getEndPosition(), move.getStartPosition());
    }

    /**
     * Convert an int into a hexadecimal string
     *
     * @return the hexadecimal string
     * @value the int to convert
     */
    public static String IntToHex(int value) {
        return Integer.toHexString(value);
    }

    /**
     * Convert a hexadecimal string into an int
     *
     * @return the int
     * @value the hexadecimal string to convert
     */
    public static int HexToInt(String value) {
        return Integer.parseInt(value, 16);
    }

    /**
     * Convert a int into a binary string
     *
     * @return the binary string
     * @value the int to convert
     */
    public static String IntToBinary(int value) {
        return Integer.toBinaryString(value);
    }

    /**
     * Convert a binary string into an int
     *
     * @return the int
     * @value the binary string to convert
     */
    public static int BinaryToInt(String value) {
        return Integer.parseInt(value, 2);
    }
}
