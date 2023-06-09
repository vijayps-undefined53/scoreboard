package scorecard.service.impl;

import scorecard.Factory.MatchFactory;
import scorecard.ScoreBoard;
import scorecard.controller.TeamsController;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.RugbyMatch;
import scorecard.repo.Teams;
import scorecard.service.MatchService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static scorecard.Constants.FOOTBALL;
import static scorecard.Constants.RUGBY;

public class MatchServiceImpl
        implements MatchService {
    private static final MatchService matchService = new MatchServiceImpl();
    final TeamsController teamsController;

    private MatchServiceImpl() {
        this.teamsController = TeamsController.getInstance();
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
            List<String> listOfTeamNames = teams.stream().collect(Collectors.toList());
            Teams homeTeam = teamsController.createTeams(listOfTeamNames.get(0));
            Teams awayTeam = teamsController.createTeams(listOfTeamNames.get(1));
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
            List<String> listOfTeamNames = teams.stream().collect(Collectors.toList());
            Set<Teams> teamSet =
                    listOfTeamNames.stream().map(s -> teamsController.createTeams(s)).collect(
                            Collectors.toSet());
            MatchFactory matchFactory = new MatchFactory();
            Match match =
                    matchFactory.createMatch(game, scoreBoard,
                                             new LinkedHashSet<>(teamSet));

            // Set Game Specific Behavior based on match instance type
            if (match instanceof RugbyMatch) {
                RugbyMatch rugbyMatch = (RugbyMatch) match;
                return rugbyMatch;
                //
            }

        }
        throw new RuntimeException("Unsupported Game Type");
    }
}
