package Engine.Heuristics;

import Engine.Board.IBoard;

public class DistanceToEnnemy implements IHeuristics
{
    @Override
    public String getName() {
        return "Distance to ennemy";
    }

    @Override
    public int evaluate(IBoard board) {
        return 0;
    }
}
