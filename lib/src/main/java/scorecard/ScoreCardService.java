package scorecard;

import scorecard.match.FootballMatch;
import scorecard.match.Match;

import java.io.Serializable;

public final class ScoreCardService
        implements Serializable {
    private static ScoreCardService scoreCardService = new ScoreCardService();

    private ScoreCardService() {
    }

    public static ScoreCardService getInstance() {
        return scoreCardService;
    }

    public Match createMatch() {
        return new FootballMatch();
    }
}
