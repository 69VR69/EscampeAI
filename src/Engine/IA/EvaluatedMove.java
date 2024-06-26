package Engine.IA;

import Engine.Board.Move;

public class EvaluatedMove extends Move
{
    private int score;

    public EvaluatedMove(Move move, int score)
    {
        super(move);
        this.score = score;
    }

    public EvaluatedMove(Move move)
    {
        super(move);
        this.score = 0;
    }

    public int getScore() {
        return score;
    }
    public  void setScore(int score) {
        this.score = score;
    }
}
