/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package scorecard;

import scorecard.repo.Match;
import scorecard.repo.Teams;
import scorecard.service.impl.ScoreBoardService;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreBoard {
    List<Match> matches = new ArrayList<>();
    String game;
    ScoreBoardService scoreBoardService;

    {
    }

    public ScoreBoard(String game) {
        this.game = game;
        scoreBoardService = ScoreBoardService.getInstance();
    }

    public Match createMatch(LinkedHashSet<String> teams) {
        if (teams == null || teams.isEmpty() || teams.stream().anyMatch(Objects::isNull)) {
            throw new RuntimeException("Valid teams names expected");
        }
        if (matches != null && !matches.isEmpty()) {
            for (int i = 0; i < matches.size(); i++) {
                Set<Teams> teamSet = matches.get(i).getScore().keySet();
                List<String> teamNames = teamSet.stream().map(teams1 -> teams1.getName()).collect(Collectors.toList());
                if (teamNames.stream().anyMatch(teams::contains)) {
                    throw new RuntimeException("A match is already going on with the team names provided");
                }
            }
        }
        Match match = scoreBoardService.createMatch(this.game, teams, this);
        // adding it to the scoreboard
        this.matches.add(match);
        return match;
    }

    public List<Match> getMatches() {
        return this.matches;
    }

    public Match updateScore(Teams team, Object score,
                             Match match) {
        return null;
    }
}
