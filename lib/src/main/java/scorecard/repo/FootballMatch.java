package scorecard.repo;

import scorecard.ScoreBoard;
import scorecard.repo.impl.FootballScore;

import java.util.LinkedHashSet;

public class FootballMatch
        extends Match {
    private Teams homeTeam;
    private Teams awayTeam;

    public FootballMatch(ScoreBoard scoreBoard, LinkedHashSet<Teams> teams) {
        super(scoreBoard, teams);
        scoreStrategy = new ScoreStrategy(new FootballScore());
        teams.stream().forEach(team -> {
            scoreStrategy.updateScore(team, 0, this);
        });
    }

    @Override
    public void updateScore(Teams teams, Object score, Match match) {
        scoreStrategy.updateScore(teams, score, this);
    }

    public Teams getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Teams homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Teams getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Teams awayTeam) {
        this.awayTeam = awayTeam;
    }
}
