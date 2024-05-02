package Interface;

public class EscampeBoard implements Partie1
{
    @Override
    public void setFromFile(String fileName) {

    }

    @Override
    public void saveToFile(String fileName) {

    }

    @Override
    public boolean isValidMove(String move, String player) {
        return false;
    }

    @Override
    public String[] possiblesMoves(String player) {
        return new String[0];
    }

    @Override
    public void play(String move, String player) {

    }

    @Override
    public boolean gameOver() {
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Start an Escampe Game :) ?");
    }
}
