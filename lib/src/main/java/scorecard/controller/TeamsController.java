package scorecard.controller;

import scorecard.repo.Teams;
import scorecard.service.TeamsService;
import scorecard.service.impl.TeamsServiceImpl;

public class TeamsController {
    private static final TeamsController teamsController = new TeamsController();
    final TeamsService teamsService;

    private TeamsController() {
        teamsService = TeamsServiceImpl.getInstance();
    }

    public static TeamsController getInstance() {
        return teamsController;
    }

    public Teams createTeams(String teamName) {
        return teamsService.createTeams(teamName);
    }
}
