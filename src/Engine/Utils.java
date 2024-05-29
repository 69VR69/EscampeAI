package Engine;

import Engine.Board.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Utils {
    public static IPawn NullPawn()
    {
        if(_nullPawn == null)
        {
            _nullPawn = new Pawn(false, false, false);
        }
        return _nullPawn;
    }
    private static IPawn _nullPawn;

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
     * Check if the move is a nothing move
     * @param move the move to check
     */
    public static boolean IsNothingMove(IMove move) {
        return move.getStartPosition().equals(_nothingMove.getStartPosition()) && move.getEndPosition().equals(_nothingMove.getEndPosition());
    }

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

    /**
     * Convert a string into a position
     * @param pos the string to convert
     * @param offset the offset to apply
     * @return the position
     */
    public static Position AddPosition(Position pos, Position offset) {
        Position p = pos.clone();
        p.add(offset);
        return p;
    }

    /**
     * Concatenate two arrays
     * @param a the first array
     * @param b the second array
     * @return the concatenated array
     */
    public static  <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}
