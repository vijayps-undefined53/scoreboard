package scorecard.service;

import scorecard.ScoreBoard;
import scorecard.repo.Match;
import scorecard.repo.Teams;

import java.util.LinkedHashSet;

public interface MatchService {

    Match createMatch(String game, ScoreBoard scoreBoard,
                      LinkedHashSet<String> teams);

    Match updateScore(Teams team, Object score,
                      Match match);
}

