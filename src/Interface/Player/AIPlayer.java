package Interface.Player;


public class AIPlayer implements IJoueur
{
    /**
     * Initialize the player
     * @param mycolour the player's colour (-1 = White, 1 = Black)
     */
    @Override
    public void initJoueur(int mycolour) {
        // TODO : implement this method by initializing the player
    }

    /**
     * Return the player's number
     * @return the player's number
     */
    @Override
    public int getNumJoueur() {
        // TODO : implement this method by returning the player's number
        return 0;
    }

    /**
     * Choose the best move to play
     * @return the move to play
     */
    @Override
    public String choixMouvement() {
        // TODO : implement this method by returning the best move to play
        return "";
    }

    /**
     * Declare the winner of the game
     * @param colour the colour of the winner (White = -1, Black = 1)
     */
    @Override
    public void declareLeVainqueur(int colour) {
        // TODO : implement this method by showing depending if the current player won or not
    }

    /**
     * Update the board with the opponent's move
     * @param coup the opponent's move.
     * Can be "PASSE" if the opponent passes is turn,
     *        a move in the form "A1-B2" or
     *        an initialisation move in the form "D6/.../B6"
     */
    @Override
    public void mouvementEnnemi(String coup) {
        // TODO : implement this method by updating the board with the opponent's move
    }

    /**
     * Return the name of the players
     * @return the name of the players
     */
    @Override
    public String binoName() {
        return "Roulia et PiouPiou";
    }
}
