package scorecard.Factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import scorecard.ScoreBoard;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.Teams;
import scorecard.service.TeamsService;
import scorecard.service.impl.TeamsServiceImpl;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static scorecard.Constants.*;

public class MatchFactoryTest {
    private final TeamsService teamsService = TeamsServiceImpl.getInstance();

    @Spy
    private final ScoreBoard scoreBoard = new ScoreBoard(FOOTBALL);
    @InjectMocks
    MatchFactory matchfactory;
    Teams mexico = teamsService.createTeams(MEXICO);
    Teams canada = teamsService.createTeams(CANADA);

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void When_CreateMatch_Invoked_Should_CreateMatchInstance_Based_On_Game_And_Set_Of_Team() {
        LinkedHashSet<Teams> teams = new LinkedHashSet<>();
        teams.add(mexico);
        teams.add(canada);
        Match match = matchfactory.createMatch(FOOTBALL, scoreBoard, teams);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);
    }

}
