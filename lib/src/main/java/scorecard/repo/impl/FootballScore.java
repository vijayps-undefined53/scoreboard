package scorecard.repo.impl;

import scorecard.repo.Match;
import scorecard.repo.Score;
import scorecard.repo.Teams;

import java.util.HashMap;
import java.util.Map;

public class FootballScore
        implements Score {
    @Override
    public Match updateScore(Teams teams, Object score, Match match) {
        if (match == null) {
            throw new RuntimeException("Match cannot be null, while updating match score");
        }
        if (!(score instanceof Integer) || (Integer) score < 0) {
            throw new RuntimeException("Football goal is an absolute integer");
        }
        Map<Teams, Object> footballScore = new HashMap<>();
        if (match.getScore() != null) {
            footballScore.putAll(match.getScore());
        }
        footballScore.put(teams, score);
        match.setScore(footballScore);
        return match;
    }
}
