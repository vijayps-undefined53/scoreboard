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

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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
        ArgumentMatcher<String> gameMatcher = gamename -> FOOTBALL.equals(gamename);
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull);
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                s -> s != null && s instanceof ScoreBoard;

        Teams mexico = teamsService.createTeams(MEXICO);
        Teams canada = teamsService.createTeams(CANADA);

        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        Match footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        ((FootballMatch) footballMatch).setHomeTeam(mexico);
        ((FootballMatch) footballMatch).setAwayTeam(canada);
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

        assertEquals(footballMatchActual.getScore().get(mexico), 0);
        assertEquals(footballMatchActual.getScore().get(canada), 0);
    }

}
