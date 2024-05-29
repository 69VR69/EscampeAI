package Interface.Player;


import Engine.Board.Board;
import Engine.Board.IBoard;
import Engine.Board.Move;
import Engine.Heuristics.DistanceToEnemy;
import Engine.Heuristics.HeuristicPipeline;
import Engine.Heuristics.IHeuristics;
import Engine.Heuristics.PawnMobility;
import Engine.IA.AlphaBetaAlgorithm;

import java.util.HashMap;

public class AIPlayer implements IJoueur {
    // region Constants
    private static final int MAX_DEPTH = 3;
    // endregion

    private int nbMoves = 0;
    protected boolean isWhite = false;
    protected boolean enemyStartFromTop = false;
    protected IBoard board;
    protected AlphaBetaAlgorithm alphaBetaAlgorithm;

    /**
     * Initialize the player
     *
     * @param mycolour the player's colour (-1 = White, 1 = Black)
     */
    @Override
    public void initJoueur(int mycolour) {
        isWhite = mycolour == -1;

        HeuristicPipeline heuristics = new HeuristicPipeline(new HashMap<IHeuristics, Float>() {
            {
                put(new DistanceToEnemy(), 0.5f);
                put(new PawnMobility(), 0.5f);
            }
        });

        // Create a hex strings representing the cells of the board
        String[] bitCells = new String[]{
                "122312",
                "313132",
                "231213",
                "213231",
                "131312",
                "322132"
        };

        board = new Board(heuristics);
        board.setCellFromDecString(bitCells);

        alphaBetaAlgorithm = new AlphaBetaAlgorithm(MAX_DEPTH);
    }

    /**
     * Return the player's number
     *
     * @return the player's number
     */
    @Override
    public int getNumJoueur() {
        return isWhite ? -1 : 1;
    }

    /**
     * Choose the best move to play
     *
     * @return the move to play
     */
    @Override
    public String choixMouvement() {
        String moveString;
        if (nbMoves < 2) {
            moveString = board.getInitialisationMove(enemyStartFromTop);
            board.applyInitialisationMove(moveString,isWhite);
        } else {
            Move move = alphaBetaAlgorithm.getBestMove(board, isWhite);
            board.applyMoveWithChecks(move);
            moveString = move.toString();
        }

        nbMoves++;
        System.out.println(board + "\n^-> After move : " + moveString + " and last enemy move : " + board.getLastEnemyMove() + "\n");

        return moveString;
    }

    /**
     * Declare the winner of the game
     *
     * @param colour the colour of the winner (White = -1, Black = 1)
     */
    @Override
    public void declareLeVainqueur(int colour) {
        if (colour == getNumJoueur())
            System.out.println("I won !");
        else
            System.out.println("I lost !");
    }

    /**
     * Update the board with the opponent's move
     *
     * @param coup the opponent's move.
     *             Can be "PASSE" if the opponent passes is turn,
     *             a move in the form "A1-B2" or
     *             an initialisation move in the form "D6/.../B6"
     */
    @Override
    public void mouvementEnnemi(String coup) {
        if(coup.equals("PASSE")) {
            System.out.println("The opponent passed his turn");
            return;
        }

        if (nbMoves < 2) {
            enemyStartFromTop = isStartingFromTop(coup);
            board.applyInitialisationMove(coup, !isWhite);
        } else {
            Move enemyMove = new Move(coup);
            board.applyMoveWithChecks(enemyMove);
            board.setLastEnemyMove(enemyMove);
        }
        nbMoves++;
        System.out.println(board + "\n^-> After opponent move : " + coup + "\n");
    }

    private boolean isStartingFromTop(String initMove) {
        return initMove.contains("A6") || initMove.contains("B6") || initMove.contains("C6") || initMove.contains("D6") || initMove.contains("E6") || initMove.contains("F6")
                || initMove.contains("A5") || initMove.contains("B5") || initMove.contains("C5") || initMove.contains("D5") || initMove.contains("E5") || initMove.contains("F5");
    }

    /**
     * Return the name of the players
     *
     * @return the name of the players
     */
    @Override
    public String binoName() {
        return "Roulia et PiouPiou" + (isWhite ? " (Blanc)" : " (Noir)");
    }
}
