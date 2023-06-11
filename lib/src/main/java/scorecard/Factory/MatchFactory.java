package scorecard.Factory;

import scorecard.ScoreBoard;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.RugbyMatch;
import scorecard.repo.Teams;

import java.util.LinkedHashSet;

import static scorecard.Constants.FOOTBALL;
import static scorecard.Constants.RUGBY;

/*
     This is uses factory pattern to implement a MatchFactory, it uses a factory class based on input game type string
      to find the right Match interface implementation like FootballMatch , RugbyMatch or if needed for Tennis just
      implement Match interface and create a TennisMatch.
 */
public class MatchFactory {
    public Match createMatch(String game, ScoreBoard scoreBoard,
                             LinkedHashSet<Teams> teams) {
        return switch (game) {
            case FOOTBALL -> new FootballMatch(scoreBoard, teams);
            case RUGBY -> new RugbyMatch(scoreBoard, teams);
            default -> throw new RuntimeException("Type of game needed to create match");
        };
    }
}
