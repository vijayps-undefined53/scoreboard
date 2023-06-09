package scorecard.Factory;

import scorecard.ScoreBoard;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.RugbyMatch;
import scorecard.repo.Teams;

import java.util.LinkedHashSet;

import static scorecard.Constants.FOOTBALL;
import static scorecard.Constants.RUGBY;

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
