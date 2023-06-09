package scorecard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import scorecard.Factory.MatchFactory;
import scorecard.ScoreBoard;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
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

public class MatchServiceTest {
    private final TeamsService teamsService = TeamsServiceImpl.getInstance();
    @Spy
    private final ScoreBoard scoreBoard = new ScoreBoard(FOOTBALL);
    @InjectMocks
    MatchServiceImpl matchService;

    @Mock
    MatchFactory matchfactory;

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
        Teams mexico = teamsService.createTeams(MEXICO);
        Teams canada = teamsService.createTeams(CANADA);
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

    private Match mockMatch(String team1, String team2, Teams teams1, Teams teams2) {
        ArgumentMatcher<String> gameMatcher = gamename -> FOOTBALL.equals(gamename);
        ArgumentMatcher<LinkedHashSet<Teams>> teamNameMatcher =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        teams.stream().anyMatch(t -> team1.equals(t))
                        && teams.stream().anyMatch(t -> team2.equals(t));
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                s -> s != null && s instanceof ScoreBoard;


        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(teams1, teams2));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(teams1);
        footballMatch.setAwayTeam(teams2);

        doReturn(footballMatch).when(
                matchfactory).createMatch(argThat(gameMatcher), argThat(scoreBoardArgumentMatcher),
                                          argThat(teamNameMatcher)
                                         );
        return footballMatch;
    }

    @Test
    void When_CreateMatch_Invoked_Should_With_A_Game_Not_Handled_Throws_Runtime_Exception() {
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        assertThrows(RuntimeException.class, () -> matchService.createMatch("TENNIS", scoreBoard, teamnames));

    }

}
