package scorecard.repo;

import scorecard.ScoreBoard;

import java.util.LinkedHashSet;

public class RugbyMatch
        extends Match {

    public RugbyMatch(ScoreBoard scoreBoard, LinkedHashSet<Teams> teams) {
        super(scoreBoard, teams);
    }

    @Override
    public void updateScore(Teams teams, Object score, Match match) {
        
    }
}