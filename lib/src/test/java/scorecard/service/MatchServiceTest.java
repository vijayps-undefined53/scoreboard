package scorecard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

public class MatchServiceTest {
    @Spy
    private final ScoreBoard scoreBoard = new ScoreBoard(FOOTBALL);
    @Mock
    TeamsService teamsService = TeamsServiceImpl.getInstance();
    @InjectMocks
    MatchServiceImpl matchService;

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
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamNames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));

        Match match = matchService.createMatch(FOOTBALL, scoreBoard, teamNames);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);
        assertNotNull(match.getScore());
        assertNotNull(match.getScore().get(mexico));
        assertNotNull(match.getScore().get(canada));
        assertEquals(match.getScore().get(canada), 0);
        assertEquals(match.getScore().get(mexico), 0);

        //Update Score
        matchService.updateScore(mexico, 0, match);
        matchService.updateScore(canada, 5, match);

        assertNotNull(match.getScore());
        assertNotNull(match.getScore().get(mexico));
        assertNotNull(match.getScore().get(canada));
        assertEquals(5, match.getScore().get(canada), String.format("Score not updated for %s", canada));
        assertEquals(0, match.getScore().get(mexico), String.format("Score not updated for %s", mexico));

        mockMatch(SPAIN, BRAZIL, spain, brazil);
        LinkedHashSet<String> spainAndBrazil = new LinkedHashSet<>(Set.of(SPAIN, BRAZIL));

        Match match2 = matchService.createMatch(FOOTBALL, scoreBoard, spainAndBrazil);

        //Update Score
        matchService.updateScore(spain, 10, match2);
        matchService.updateScore(brazil, 2, match2);

        assertNotNull(match2.getScore());
        assertNotNull(match2.getScore().get(spain));
        assertNotNull(match2.getScore().get(brazil));
        assertEquals(10, match2.getScore().get(spain), String.format("Score not updated for %s", spain));
        assertEquals(2, match2.getScore().get(brazil), String.format("Score not updated for %s", brazil));

    }

}
