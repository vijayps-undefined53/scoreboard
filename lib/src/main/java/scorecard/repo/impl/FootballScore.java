package scorecard.repo.impl;

import scorecard.repo.Match;
import scorecard.repo.Score;
import scorecard.repo.Teams;

import java.util.HashMap;
import java.util.Map;

public class FootballScore
        implements Score {
    @Override
    public void updateScore(Teams teams, Object score, Match match) {
        if (!(score instanceof Integer)) {
            throw new RuntimeException("Football goal is an absolute integer");
        }
        Map<Teams, Object> footballScore = new HashMap<>();
        if (match != null && match.getScore() != null) {
            match.getScore().entrySet().forEach(teamsObjectEntry -> {
                footballScore.put(teamsObjectEntry.getKey(), teamsObjectEntry.getValue());
            });
        }
        footballScore.put(teams, score);
        match.setScore(footballScore);
    }
}
