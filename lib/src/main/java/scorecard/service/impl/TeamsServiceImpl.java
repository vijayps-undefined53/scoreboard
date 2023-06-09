package scorecard.service.impl;

import scorecard.repo.Sequence;
import scorecard.repo.Teams;
import scorecard.service.TeamsService;

import java.io.Serializable;

public class TeamsServiceImpl
        implements Serializable, TeamsService {
    private static final TeamsService teamsService = new TeamsServiceImpl();

    private TeamsServiceImpl() {
    }

    public static TeamsService getInstance() {
        return teamsService;
    }

    @Override
    public Teams createTeams(String team) {
        return new Teams(Sequence.getSequence(), team);
    }


}
