package Engine.Heuristics;

import Engine.Board.IBoard;

import java.util.Map;

public class HeuristicPipeline {
    // The heuristics with their respective weights
    protected Map<IHeuristics, Float> heuristics;

    public HeuristicPipeline(Map<IHeuristics, Float> heuristics) {
        this.heuristics = heuristics;
    }

    public int evaluate(IBoard board) {
        int evaluation = 0;

        for (Map.Entry<IHeuristics, Float> entry : heuristics.entrySet()) {
            evaluation += (int) Math.ceil(entry.getKey().evaluate(board) * entry.getValue());
        }

        return evaluation;
    }
}
