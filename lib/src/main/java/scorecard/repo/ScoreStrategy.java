package scorecard.repo;

/*
    This uses strategy for score related operations, it uses a ScoringStrategy class based on input
    Score type to its constructor it picks the scoring strategy,like FootballScore , RugbyScore or if needed for
    Tennis just implement Score interface for Tennis Score and pass it as ScoringStrategy to Tennis Match.
 */
public class ScoreStrategy {
    Score score;

    public ScoreStrategy(Score score) {
        this.score = score;
    }

    public Match updateScore(Teams teams, Object scores, Match match) {
        return this.score.updateScore(teams, scores, match);
    }
}
