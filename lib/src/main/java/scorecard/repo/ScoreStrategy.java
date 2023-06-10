package scorecard.repo;

public class ScoreStrategy {
    Score score;

    public ScoreStrategy(Score score) {
        this.score = score;
    }

    public Match updateScore(Teams teams, Object scores, Match match) {
        return this.score.updateScore(teams, scores, match);
    }
}
