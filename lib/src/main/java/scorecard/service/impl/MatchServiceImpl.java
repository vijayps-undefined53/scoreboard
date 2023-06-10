package scorecard.service.impl;

import scorecard.Factory.MatchFactory;
import scorecard.ScoreBoard;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.RugbyMatch;
import scorecard.repo.Teams;
import scorecard.service.MatchService;
import scorecard.service.TeamsService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static scorecard.Constants.FOOTBALL;
import static scorecard.Constants.RUGBY;

public class MatchServiceImpl
        implements MatchService {
    private static final MatchService matchService = new MatchServiceImpl();
    TeamsService teamsService;

    private MatchServiceImpl() {
        this.teamsService = TeamsServiceImpl.getInstance();
    }

    public static MatchService getInstance() {
        return matchService;
    }


    @Override
    public Match createMatch(String game, ScoreBoard scoreBoard,
                             LinkedHashSet<String> teams) {
        if (teams == null || teams.size() == 0 || scoreBoard == null) {
            throw new RuntimeException("Teams or Score card is null");
        }
        if (FOOTBALL.equals(game)) {
            List<String> listOfTeamNames = new ArrayList<>(teams);
            Teams homeTeam = teamsService.createTeams(listOfTeamNames.get(0));
            Teams awayTeam = teamsService.createTeams(listOfTeamNames.get(1));
            MatchFactory matchFactory = new MatchFactory();
            Match match =
                    matchFactory.createMatch(game, scoreBoard,
                                             new LinkedHashSet<>(Set.of(homeTeam,
                                                                        awayTeam)));

            // Set Game Specific Behavior based on match instance type
            if (match instanceof FootballMatch) {
                FootballMatch footballMatch = (FootballMatch) match;
                footballMatch.setHomeTeam(homeTeam);
                footballMatch.setAwayTeam(awayTeam);
                return footballMatch;
            }
        }
        if (RUGBY.equals(game)) {
            List<String> listOfTeamNames = new ArrayList<>(teams);
            Set<Teams> teamSet =
                    listOfTeamNames.stream().map(s -> teamsService.createTeams(s)).collect(
                            Collectors.toSet());
            MatchFactory matchFactory = new MatchFactory();
            Match match =
                    matchFactory.createMatch(game, scoreBoard,
                                             new LinkedHashSet<>(teamSet));

            // Set Game Specific Behavior based on match instance type
            if (match instanceof RugbyMatch) {
                return match;
                //
            }

        }
        throw new RuntimeException("Unsupported Game Type");
    }

    @Override
    public Match updateScore(Teams team, Object score, Match match) {
        match.updateScore(team, score, match);
        return match;
    }
}
