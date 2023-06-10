package scorecard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import scorecard.Factory.MatchFactory;
import scorecard.ScoreBoard;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.Sequence;
import scorecard.repo.Teams;
import scorecard.service.impl.MatchServiceImpl;
import scorecard.service.impl.TeamsServiceImpl;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static scorecard.Constants.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MatchServiceTest {
    @Spy
    private final ScoreBoard scoreBoard = new ScoreBoard(FOOTBALL);
    @Mock
    TeamsService teamsService = TeamsServiceImpl.getInstance();
    @InjectMocks
    MatchServiceImpl matchService;

    @Mock
    FootballMatch footballMatchMock;

    @Mock
    MatchFactory matchfactory;
    Teams mexico = new Teams(Sequence.getSequence(), MEXICO);
    Teams canada = new Teams(Sequence.getSequence(), CANADA);
    Teams spain = new Teams(Sequence.getSequence(), SPAIN);
    Teams brazil = new Teams(Sequence.getSequence(), BRAZIL);

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void When_GetInstance_Invoked_Should_Create_Match_Instance() {
        MatchService matchService = MatchServiceImpl.getInstance();
        assertInstanceOf(MatchService.class, matchService);
    }

    @Test
    void When_CreateMatch_Invoked_Should_Create_Match_Instance_Based_On_Game() {

        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));

        Match match = matchService.createMatch(FOOTBALL, scoreBoard, teamnames);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);
        assertNotNull(match.getScore());
        assertNotNull(match.getScore().get(mexico));
        assertNotNull(match.getScore().get(canada));
        assertEquals(match.getScore().get(canada), 0);
        assertEquals(match.getScore().get(mexico), 0);
    }

    private Match mockMatch(String team1, String team2, Teams teamsObj1, Teams teamsObj2) {
        ArgumentMatcher<String> gameMatcher = FOOTBALL::equals;
        ArgumentMatcher<LinkedHashSet<Teams>> teamNameMatcher =
                teams -> {
                    if (teams != null && teams.stream().allMatch(Objects::nonNull) &&
                            teams.stream().anyMatch(teamsObj1::equals)) {
                        return teams.stream().anyMatch(teamsObj2::equals);
                    }
                    return false;
                };
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                Objects::nonNull;


        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(teamsObj1, teamsObj2));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(teamsObj1);
        footballMatch.setAwayTeam(teamsObj2);

        doReturn(footballMatch).when(
                matchfactory).createMatch(argThat(gameMatcher), argThat(scoreBoardArgumentMatcher),
                                          argThat(teamNameMatcher)
                                         );
        doReturn(mexico).when(
                teamsService).createTeams(argThat(MEXICO::equals)
                                         );
        doReturn(canada).when(
                teamsService).createTeams(argThat(CANADA::equals)
                                         );

        doReturn(spain).when(
                teamsService).createTeams(argThat(SPAIN::equals)
                                         );
        doReturn(brazil).when(
                teamsService).createTeams(argThat(BRAZIL::equals));
        return footballMatch;
    }

    @Test
    void When_CreateMatch_Invoked_Should_With_A_Game_Not_Handled_Throws_Runtime_Exception() {
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        assertThrows(RuntimeException.class, () -> matchService.createMatch("TENNIS", scoreBoard, teamnames));

    }

    @Test
    void When_UpdateScore_Invoked_Should_Update_Score_Of_The_Match() {
        doReturn(mexico).when(
                teamsService).createTeams(argThat(MEXICO::equals)
                                         );
        doReturn(canada).when(
                teamsService).createTeams(argThat(CANADA::equals)
                                         );

        doReturn(spain).when(
                teamsService).createTeams(argThat(SPAIN::equals)
                                         );
        doReturn(brazil).when(
                teamsService).createTeams(argThat(BRAZIL::equals));
        Teams mexico = teamsService.createTeams(MEXICO);
        Teams canada = teamsService.createTeams(CANADA);

        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);

        Match matchMockUpdate = new FootballMatch(scoreBoard, teams);


        matchMockUpdate.getScore().put(mexico, 1);

        //Update Score

        doReturn(matchMockUpdate).when(footballMatchMock).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                              Set.of(mexico.getName())).contains(team.getName())),
                                                                      argThat(score ->
                                                                                      (score instanceof Integer &&
                                                                                              (Integer) score == 1)),
                                                                      argThat(matchArg -> matchArg instanceof FootballMatch));

        matchMockUpdate.getScore().put(canada, 5);

        doReturn(matchMockUpdate).when(footballMatchMock).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                              Set.of(canada.getName())).contains(team.getName())),
                                                                      argThat(score ->
                                                                                      (score instanceof Integer &&
                                                                                              (Integer) score == 5)),
                                                                      argThat(matchArg -> matchArg instanceof FootballMatch));

        Match matchOutput = matchService.updateScore(mexico, 1, footballMatch);
        assertEquals(1, matchOutput.getScore().get(mexico));
        matchOutput = matchService.updateScore(canada, 5, footballMatch);
        assertEquals(5, matchOutput.getScore().get(canada));

    }

}
