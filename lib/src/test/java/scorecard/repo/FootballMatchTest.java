package scorecard.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import scorecard.ScoreBoard;
import scorecard.service.TeamsService;
import scorecard.service.impl.TeamsServiceImpl;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static scorecard.Constants.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class FootballMatchTest {
    @Spy
    private final ScoreBoard scoreBoard = new ScoreBoard(FOOTBALL);
    @Mock
    ScoreStrategy scoreStrategy;

    TeamsService teamsService = TeamsServiceImpl.getInstance();
    Teams mexico = teamsService.createTeams(MEXICO);
    Teams canada = teamsService.createTeams(CANADA);
    LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));

    @InjectMocks
    Match match = new FootballMatch(new ScoreBoard(FOOTBALL), teams);

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void When_UpdateScore_Invoked_Should_Update_Score_Of_The_Match() {
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);

        Teams mexico = new Teams(Sequence.getSequence(), MEXICO);
        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);

        Match matchMockUpdate = new FootballMatch(scoreBoard, teams);
        matchMockUpdate.getScore().put(mexico, 1);

        //Update Score

        doReturn(matchMockUpdate).when(scoreStrategy).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                          Set.of(mexico.getName())).contains(team.getName())),
                                                                  argThat(score ->
                                                                                  (score instanceof Integer &&
                                                                                          (Integer) score == 1)),
                                                                  argThat(matchArg -> matchArg instanceof FootballMatch));

        matchMockUpdate.getScore().put(canada, 5);

        doReturn(matchMockUpdate).when(scoreStrategy).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                          Set.of(canada.getName())).contains(team.getName())),
                                                                  argThat(score ->
                                                                                  (score instanceof Integer &&
                                                                                          (Integer) score == 5)),
                                                                  argThat(matchArg -> matchArg instanceof FootballMatch));

        Match matchOutput = match.updateScore(mexico, 1, footballMatch);
        assertEquals(1, matchOutput.getScore().get(mexico));
        matchOutput = match.updateScore(canada, 5, footballMatch);
        assertEquals(5, matchOutput.getScore().get(canada));

    }
}
