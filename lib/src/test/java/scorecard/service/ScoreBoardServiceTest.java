package scorecard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import scorecard.ScoreBoard;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.Teams;
import scorecard.service.impl.MatchServiceImpl;
import scorecard.service.impl.ScoreBoardService;
import scorecard.service.impl.TeamsServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static scorecard.Constants.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ScoreBoardServiceTest {

    private final TeamsService teamsService = TeamsServiceImpl.getInstance();
    @Spy
    private final ScoreBoard scoreBoard = new ScoreBoard(FOOTBALL);
    @InjectMocks
    ScoreBoardService scoreBoardService;
    @Mock
    MatchService matchService = MatchServiceImpl.getInstance();

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void When_GetInstance_Invoked_Should_Create_ScoreBoard_Instance() {
        ScoreBoardService scoreBoardService = ScoreBoardService.getInstance();
        assertInstanceOf(ScoreBoardService.class, scoreBoardService);
    }

    @Test
    void When_CreateMatch_Invoked_Should_CreateMatchInstance_Based_On_Game() {
        ArgumentMatcher<String> gameMatcher = FOOTBALL::equals;
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull);
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                Objects::nonNull;

        Teams mexico = teamsService.createTeams(MEXICO);
        Teams canada = teamsService.createTeams(CANADA);

        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);
        doReturn(footballMatch).when(
                matchService).createMatch(argThat(gameMatcher), argThat(scoreBoardArgumentMatcher),
                                          argThat(teamNameMatcher)
                                         );

        Match match = scoreBoardService.createMatch(FOOTBALL, new LinkedHashSet<>(Set.of(MEXICO,
                                                                                         CANADA)),
                                                    scoreBoard);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

        assertNotNull(match.getScore());
        FootballMatch footballMatchActual = (FootballMatch) match;
        assertEquals(footballMatchActual.getHomeTeam().getName(), MEXICO);
        assertEquals(footballMatchActual.getAwayTeam().getName(), CANADA);
    }


    @Test
    void When_CreateMatch_Invoked_Should_Create_Match_Instance_With_Initial_Score_Zero() {
        ArgumentMatcher<String> gameMatcher = FOOTBALL::equals;
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull);
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                Objects::nonNull;

        Teams mexico = teamsService.createTeams(MEXICO);
        Teams canada = teamsService.createTeams(CANADA);

        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);
        doReturn(footballMatch).when(
                matchService).createMatch(argThat(gameMatcher), argThat(scoreBoardArgumentMatcher),
                                          argThat(teamNameMatcher)
                                         );

        Match match = scoreBoardService.createMatch(FOOTBALL, new LinkedHashSet<>(Set.of(MEXICO,
                                                                                         CANADA)),
                                                    scoreBoard);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

        assertNotNull(match.getScore());
        FootballMatch footballMatchActual = (FootballMatch) match;
        assertEquals(footballMatchActual.getHomeTeam().getName(), MEXICO);
        assertEquals(footballMatchActual.getAwayTeam().getName(), CANADA);

        assertEquals(footballMatchActual.getScore().get(footballMatchActual.getHomeTeam()), 0);
        assertEquals(footballMatchActual.getScore().get(footballMatchActual.getAwayTeam()), 0);
    }

    @Test
    void When_CreateMatch_Invoked_Should_Throw_Exception_If_Validation_Fails() {
        assertThrows(RuntimeException.class, () -> scoreBoardService.createMatch(FOOTBALL, new LinkedHashSet<>(),
                                                                                 scoreBoard));
        assertThrows(RuntimeException.class,
                     () -> scoreBoardService.createMatch(FOOTBALL, new LinkedHashSet<>(Set.of(MEXICO,
                                                                                              CANADA)),
                                                         null));
        assertThrows(RuntimeException.class,
                     () -> scoreBoardService.createMatch(FOOTBALL, null,
                                                         scoreBoard));
    }

    @Test
    void When_UpdateScore_Invoked_Should_Update_Score_Of_A_Match() {
        Teams mexico = teamsService.createTeams(MEXICO);
        Teams canada = teamsService.createTeams(CANADA);

        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);

        Match matchMockUpdate = new FootballMatch(scoreBoard, teams);


        matchMockUpdate.getScore().put(mexico, 0);
        matchMockUpdate.getScore().put(canada, 5);

        //Update Score

        doReturn(matchMockUpdate).when(matchService).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                         Set.of(mexico.getName(),
                                                                                canada.getName())).contains(team.getName())),
                                                                 argThat(score -> {
                                                                     if (score instanceof Integer &&
                                                                             (Integer) score == 0) return true;
                                                                     assert score instanceof Integer;
                                                                     return (Integer) score == 5;
                                                                 }),
                                                                 argThat(matchArg -> matchArg instanceof FootballMatch));

        Map<String, Object> scoreUpdate = new HashMap<>();
        scoreUpdate.put(MEXICO, 0);
        scoreUpdate.put(CANADA, 5);
        scoreBoardService.updateScore(mexico, 0, footballMatch);
        assertEquals(0, footballMatch.getScore().get(footballMatch.getHomeTeam()));
        scoreBoardService.updateScore(canada, 5, footballMatch);
        assertEquals(5, footballMatch.getScore().get(footballMatch.getAwayTeam()));
    }
}
