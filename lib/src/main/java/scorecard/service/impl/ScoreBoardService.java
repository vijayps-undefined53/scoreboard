package scorecard.service.impl;

import scorecard.ScoreBoard;
import scorecard.repo.Match;
import scorecard.service.MatchService;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;

public class ScoreBoardService
        implements Serializable {
    private static final ScoreBoardService SCORE_BOARD_SERVICE = new ScoreBoardService();
    MatchService matchService;

    private ScoreBoardService() {
        matchService = MatchServiceImpl.getInstance();
    }

    public static ScoreBoardService getInstance() {
        return SCORE_BOARD_SERVICE;
    }

    public Match createMatch(String game, LinkedHashSet<String> teams, ScoreBoard scoreBoard) {
        if (teams == null || teams.isEmpty() || teams.stream().anyMatch(Objects::isNull) || scoreBoard == null) {
            throw new RuntimeException("Invalid input for creating match");
        }
        Match match = matchService.createMatch(game, scoreBoard, new LinkedHashSet<>(teams));
        return match;
    }
}
