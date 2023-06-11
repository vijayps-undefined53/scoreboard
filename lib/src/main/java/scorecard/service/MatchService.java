package scorecard.service;

import scorecard.ScoreBoard;
import scorecard.repo.Match;
import scorecard.repo.Teams;

import java.util.LinkedHashSet;

public interface MatchService {
    /*
        This method uses factory pattern internally, it uses a factory class based on input game type string to find
        the right Match interface implementation like FootballMatch , RugbyMatch or if needed for Tennis just
        implement Match interface and create a TennisMatch.
     */
    Match createMatch(String game, ScoreBoard scoreBoard,
                      LinkedHashSet<String> teams);

    /*
    This method uses strategy pattern internally, it uses a ScoringStrategy class based on input Score type to it's
    constructor it picks the scoring strategy,like FootballScore , RugbyScore or if needed for Tennis just
    implement Score interface for Tennis Score and pass it as ScoringStrategy to Tennis Match.
     */
    Match updateScore(Teams team, Object score,
                      Match match);
}

