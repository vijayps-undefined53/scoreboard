package scorecard.repo;

public class ScoreStrategy {
    private final Score score;

    public ScoreStrategy(Score score) {
        this.score = score;
    }

    public void updateScore(Teams teams, Object scores, Match match) {
        this.score.updateScore(teams, scores, match);
    }
}
