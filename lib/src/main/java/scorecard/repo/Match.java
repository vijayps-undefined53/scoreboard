package scorecard.repo;

import scorecard.ScoreBoard;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;

public abstract class Match {
    final Integer id;
    final LinkedHashSet<Teams> teams;
    final ScoreBoard scoreBoard;
    ScoreStrategy scoreStrategy;
    Map<Teams, Object> score;

    public Match(ScoreBoard scorecard, LinkedHashSet<Teams> teams) {
        this.id = Sequence.getSequence();
        this.teams = teams;
        this.scoreBoard = scorecard;
    }

    ScoreBoard getScoreCard() {
        return scoreBoard;
    }

    LinkedHashSet<Teams> getTeams() {
        return teams;
    }

    long getNumberOfTeams() {
        return this.teams.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;
        Match match = (Match) o;
        return Objects.equals(getId(), match.getId());
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        if (score != null && !score.entrySet().isEmpty()) {
            for (Map.Entry<Teams, Object> entry : score.entrySet()) {
                response.append(entry.getKey().getName()).append(" ").append(entry.getValue().toString());
            }
        }
        return response.toString();
    }

    public Integer getId() {
        return id;
    }

    public Map<Teams, Object> getScore() {
        return score;
    }

    public void setScore(Map<Teams, Object> score) {
        this.score = score;
    }

    public abstract Match updateScore(Teams teams, Object score, Match match);
}
