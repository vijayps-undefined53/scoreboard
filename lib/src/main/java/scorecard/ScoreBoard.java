/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package scorecard;

import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.Teams;
import scorecard.service.impl.ScoreBoardService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static scorecard.Constants.*;

public class ScoreBoard {
    private final List<Match> matches = new ArrayList<>();
    String game;
    ScoreBoardService scoreBoardService;

    public ScoreBoard(String game) {
        if (game.equals(FOOTBALL) || game.equals(RUGBY)) {
            this.game = game;
            scoreBoardService = ScoreBoardService.getInstance();
        } else {
            throw new RuntimeException(
                    "Pass football or rugby as game type, other games need to have scoring strategy " +
                            "and match type class extended to be supported");
        }
    }

    //Convenience method to use Football game as Scoreboard game without passing game name.
    public ScoreBoard() {
        this.game = FOOTBALL;
        scoreBoardService = ScoreBoardService.getInstance();
    }

    // This is a generic createMatch method for all games, this can be used to create match based on game name passed
    // to ScoreBoard, default game type is football.
    public Match createMatch(LinkedHashSet<String> teams) {
        validationsCreatingMatch(teams);
        Match match = scoreBoardService.createMatch(this.game, teams, this);
        if (match == null) {
            throw new RuntimeException("Match cannot be created");
        }
        // adding it to the scoreboard
        this.matches.add(match);
        return match;
    }

    private void validationsCreatingMatch(LinkedHashSet<String> teams) {
        if (teams == null || teams.isEmpty() || teams.stream().anyMatch(String::isBlank)) {
            throw new RuntimeException(VALID_TEAM_NAME_EXPECTED);
        }
        Map<String, Teams> teamInMatch = findTeamsInScoreBoardBasedOnName(teams);
        if (teamInMatch != null && !teamInMatch.isEmpty()) {
            throw new RuntimeException("A match is already going on, for one or both of team names provided");
        }
    }

    private Map<String, Teams> findTeamsInScoreBoardBasedOnName(LinkedHashSet<String> teams) {
        if (!matches.isEmpty()) {
            for (Match match : matches) {
                Map<String, Teams> teamsList = findTeamsObjectInMatchBasedOnTeamNames(teams, match);
                if (teamsList != null) return teamsList;
            }
        }
        return null;
    }

    private Map<String, Teams> findTeamsObjectInMatchBasedOnTeamNames(LinkedHashSet<String> teams, Match match) {
        if (match != null && match.getScore() != null) {
            return match.getScore().keySet().stream().filter(
                    teamObject -> teams.contains(teamObject.getName())).collect(
                    Collectors.toMap(Teams::getName, teams1 -> teams1));
        }
        return null;
    }

    public Match updateScore(Map<String, Object> score) {
        Match match = getMatchBasedOnTeamName(score.keySet());
        Map<String, Teams> teamInMatch =
                findTeamsObjectInMatchBasedOnTeamNames(new LinkedHashSet<>(score.keySet()), match);
        if (teamInMatch == null || !(teamInMatch.size() == score.keySet().size())) {
            throw new RuntimeException(VALIDATION_ERROR_ON_UPDATING_SCORE_WITH_TEAM_NOT_IN_MATCH);
        }
        for (Map.Entry<String, Object> entry : score.entrySet()) {
            match = scoreBoardService.updateScore(teamInMatch.get(entry.getKey()), entry.getValue(), match);
        }
        return match;
    }

    private Match getMatchBasedOnTeamName(Set<String> teams) {
        if (!matches.isEmpty()) {
            Optional<Match> optionalMatch = this.matches.stream().filter(match -> {
                Set<Teams> teamsInMatch = match.getScore().keySet();
                Set<String> teamsNamesInMatch = teamsInMatch.stream().map(Teams::getName).collect(Collectors.toSet());
                return teamsNamesInMatch.containsAll(teams);
            }).findAny();
            if (optionalMatch.isPresent()) {
                return optionalMatch.get();
            } else {
                throw new RuntimeException("Found no Match associated to this score board, with given teams, all " +
                                                   "teams in input should be belonging to same match");
            }
        }
        throw new RuntimeException("No matches in progress in this scoreboard");
    }

    // This is a convenience method for football game, Scoreboard should have game type football and this method can
    // only be used to create football match, use generic createMatch method for other games
    public Match createFootballMatch(String homeTeam, String awayTeam) {
        if (!FOOTBALL.equals(this.game)) {
            throw new RuntimeException("Scoreboard should have game type football, this method can only be used to " +
                                               "create football match, use generic createMatch for other games");
        }
        LinkedHashSet<String> teams = new LinkedHashSet<>(Set.of(homeTeam, awayTeam));
        validationsCreatingMatch(teams);
        FootballMatch match = (FootballMatch) scoreBoardService.createMatch(FOOTBALL, teams, this);
        if (match == null) {
            throw new RuntimeException("Match cannot be created");
        }
        // adding it to the scoreboard
        this.matches.add(match);
        return match;
    }

    // This is a convenience method for football game, Scoreboard should have game type football and this method can
    // only be used to update football score, use generic updateScore method for other games
    public Match updateFootballMatchScore(Integer homeTeamScore, Integer awayTeamScore, String homeTeamName,
                                          String awayTeamName) {
        if (!FOOTBALL.equals(this.game)) {
            throw new RuntimeException("Scoreboard should have game type football, this method can only be used to " +
                                               "update football match score, use generic updateScore for other games");
        }
        if (homeTeamName == null ||
                awayTeamName == null) {
            throw new RuntimeException("Home Team and Away Team cannot be null for a football match");
        }
        LinkedHashSet<String> teamsNames = new LinkedHashSet<>(Set.of(homeTeamName, awayTeamName));
        FootballMatch footballMatch = (FootballMatch) getMatchBasedOnTeamName(teamsNames);
        if (!this.matches.contains(footballMatch)) {
            throw new RuntimeException("Match not associated to this score board");
        }
        Match updatedMatch = scoreBoardService.updateScore(footballMatch.getHomeTeam(), homeTeamScore,
                                                           footballMatch);
        if (updatedMatch == null) {
            throw new RuntimeException("Error in Updating score");
        }
        updatedMatch = scoreBoardService.updateScore(footballMatch.getAwayTeam(), awayTeamScore,
                                                     footballMatch);

        return updatedMatch;
    }

    public boolean finishMatch(Match match) {
        if (!this.matches.contains(match)) {
            throw new RuntimeException("Match not associated to this score board");
        }
        return this.matches.remove(match);
    }

    /*
    The pattern for summary of matches is compatible with only two player with zero or positive integer score game like
    football,
    so this method is only supported for Football.
     */
    public String getSummary() {
        if (!game.equals(FOOTBALL)) {
            throw new RuntimeException(
                    "The string pattern for summary of matches is compatible with only two player with zero or " +
                            "positive " +
                            "integer " +
                            "score game like football, so this method is only supported for Football.");
        }
        Comparator<Match> byScore =
                (match, match1) -> match.getScore().values().stream().map(v -> (Integer) v).reduce(0,
                                                                                                   Integer::sum).compareTo(
                        match1.getScore().values().stream().map(v -> (Integer) v).reduce(0,
                                                                                         Integer::sum));
        List<Match> getSummaryOfMatchesList = new ArrayList<>(getMatches());
        getSummaryOfMatchesList.sort(byScore);
        Collections.reverse(getSummaryOfMatchesList);
        AtomicInteger i = new AtomicInteger(1);
        return getSummaryOfMatchesList.stream().map(match -> {
            FootballMatch mat = (FootballMatch) match;
            Map<Teams, Object> score = match.getScore();
            StringBuilder response = new StringBuilder();
            if (score != null && !score.entrySet().isEmpty()) {
                response.append(i.getAndIncrement()).append(".").append(" ");
                StringBuilder homeTeam = new StringBuilder();
                homeTeam.append(mat.getHomeTeam().getName());
                StringBuilder awayTeam = new StringBuilder();
                awayTeam.append(mat.getAwayTeam().getName());

                for (Map.Entry<Teams, Object> entry : score.entrySet()) {
                    if (mat.getHomeTeam().getName().equals(entry.getKey().getName())) {
                        homeTeam.append(" ").append(entry.getValue().toString());
                    }
                    if (mat.getAwayTeam().getName().equals(entry.getKey().getName())) {
                        awayTeam.append(" ").append(entry.getValue().toString());
                    }
                }
                response.append(homeTeam).append(" ").append("-").append(" ");
                response.append(awayTeam);
            }
            return response;
        }).collect(Collectors.joining("\n"));
    }

    public List<Match> getMatches() {
        return this.matches;
    }

    /*
        This is another method to get summary of matches as list of match objects.
 */
    public List<Match> getSummaryOfMatchObjects() {
        Comparator<Match> byScore =
                (match, match1) -> match.getScore().values().stream().map(v -> (Integer) v).reduce(0,
                                                                                                   Integer::sum).compareTo(
                        match1.getScore().values().stream().map(v -> (Integer) v).reduce(0,
                                                                                         Integer::sum));
        List<Match> getSummaryOfMatchesList = new ArrayList<>(getMatches());
        getSummaryOfMatchesList.sort(byScore);
        Collections.reverse(getSummaryOfMatchesList);
        return getSummaryOfMatchesList;
    }

    public void finishMatchByTeamName(String teamName) {
        Match match = getMatchBasedOnTeamName(Set.of(teamName));
        if (!this.matches.contains(match)) {
            throw new RuntimeException("Match not associated to this score board");
        }
        this.matches.remove(match);
    }
}
