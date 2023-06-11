package scorecard.repo;

import scorecard.ScoreBoard;
import scorecard.repo.impl.RugbyScore;

import java.util.LinkedHashSet;

public class RugbyMatch
        extends Match {

    public RugbyMatch(ScoreBoard scoreBoard, LinkedHashSet<Teams> teams) {
        super(scoreBoard, teams);
        scoreStrategy = new ScoreStrategy(new RugbyScore());
        teams.forEach(team ->
                              scoreStrategy.updateScore(team, 0, this)
                     );
    }

    @Override
    public Match updateScore(Teams teams, Object score, Match match) {
        return scoreStrategy.updateScore(teams, score, this);
    }
}
