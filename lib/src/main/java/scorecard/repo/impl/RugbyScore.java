package scorecard.repo.impl;

import scorecard.repo.Match;
import scorecard.repo.Score;
import scorecard.repo.Teams;

import java.util.HashMap;
import java.util.Map;

public class RugbyScore
        implements Score {
    @Override
    public Match updateScore(Teams teams, Object score, Match match) {
        if (match == null) {
            throw new RuntimeException("Match cannot be null, while updating match score");
        }
        if (!(score instanceof Integer) || (Integer) score < 0) {
            throw new RuntimeException("Rugby score is an absolute integer");
        }
        Map<Teams, Object> rugbyScore = new HashMap<>();
        if (match.getScore() != null) {
            rugbyScore.putAll(match.getScore());
        }
        rugbyScore.put(teams, score);
        match.setScore(rugbyScore);
        return match;
    }
}
