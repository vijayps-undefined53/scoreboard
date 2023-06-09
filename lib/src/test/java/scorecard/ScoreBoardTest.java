/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package scorecard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.RugbyMatch;
import scorecard.repo.Teams;
import scorecard.service.MatchService;
import scorecard.service.TeamsService;
import scorecard.service.impl.ScoreBoardService;
import scorecard.service.impl.TeamsServiceImpl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static scorecard.Constants.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ScoreBoardTest {
    private final TeamsService teamsService = TeamsServiceImpl.getInstance();
    @InjectMocks
    ScoreBoard scoreBoard = new ScoreBoard(FOOTBALL);
    @InjectMocks
    ScoreBoard rugbyScoreBoard = new ScoreBoard(RUGBY);
    @Mock
    ScoreBoardService scoreBoardService;
    @Mock
    ScoreBoardService rugbyScoreBoardService;
    @Mock
    MatchService matchService;

    Teams mexico = teamsService.createTeams(MEXICO);
    Teams canada = teamsService.createTeams(CANADA);
    Teams spain = teamsService.createTeams(SPAIN);
    Teams brazil = teamsService.createTeams(BRAZIL);

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void When_CreateMatch_Invoked_Should_CreateMatchInstance_Based_On_Game() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        Match match = scoreBoard.createMatch(teamnames);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

        // Add Another type of game to another score board
        ArgumentMatcher<String> rugbyMatcher = gamename -> RUGBY.equals(gamename);
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher2 =
                teams2 -> teams2 != null && teams2.stream().allMatch(Objects::nonNull);
        ArgumentMatcher<ScoreBoard> scoreCardMatcher2 =
                s -> s != null && s instanceof ScoreBoard;
        Teams spain = teamsService.createTeams(SPAIN);
        Teams brazil = teamsService.createTeams(BRAZIL);

        LinkedHashSet<Teams> rugbyTeams = new LinkedHashSet<>(Set.of(spain, brazil));

        Match rugbyMatch = new RugbyMatch(rugbyScoreBoard, rugbyTeams);
        doReturn(rugbyMatch).when(
                scoreBoardService).createMatch(argThat(rugbyMatcher), argThat(teamNameMatcher2),
                                               argThat(scoreCardMatcher2));
        LinkedHashSet<String> rugbyTeamNames = new LinkedHashSet<>(Set.of(SPAIN, BRAZIL));

        Match match2 = rugbyScoreBoard.createMatch(rugbyTeamNames);
        assertInstanceOf(Match.class, match2);
        assertInstanceOf(RugbyMatch.class, match2);
    }

    private Match mockMatch(String team1, String team2, Teams teams1, Teams teams2) {
        ArgumentMatcher<String> gameMatcher = gamename -> FOOTBALL.equals(gamename);
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher =
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
                scoreBoardService).createMatch(argThat(gameMatcher), argThat(teamNameMatcher),
                                               argThat(scoreBoardArgumentMatcher));
        return footballMatch;
    }

    @Test
    void When_GetMatches_Invoked_Should_Return_Matches_Linked_To_ScoreCard() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        Match match = scoreBoard.createMatch(teamnames);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

        // Add Another type of game to another score board
        ArgumentMatcher<String> rugbyMatcher = gamename -> RUGBY.equals(gamename);
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher2 =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        teams.stream().anyMatch(t -> SPAIN.equals(t))
                        && teams.stream().anyMatch(t -> BRAZIL.equals(t));
        ArgumentMatcher<ScoreBoard> scoreCardMatcher2 =
                s -> s != null && s instanceof ScoreBoard;

        LinkedHashSet<Teams> rugbyTeams = new LinkedHashSet<>(Set.of(spain, brazil));

        Match rugbyMatch = new RugbyMatch(rugbyScoreBoard, rugbyTeams);
        doReturn(rugbyMatch).when(
                scoreBoardService).createMatch(argThat(rugbyMatcher), argThat(teamNameMatcher2),
                                               argThat(scoreCardMatcher2));
        LinkedHashSet<String> rugbyTeamNames = new LinkedHashSet<>(Set.of(SPAIN, BRAZIL));

        Match match2 = rugbyScoreBoard.createMatch(rugbyTeamNames);
        assertInstanceOf(Match.class, match2);
        assertInstanceOf(RugbyMatch.class, match2);


        List<Match> matchListActual = scoreBoard.getMatches();
        assertInstanceOf(List.class, matchListActual);
        assertTrue(matchListActual != null);
        assertEquals(matchListActual.size(), 1);
        assertNotNull(matchListActual.get(0).getScore());

        FootballMatch footballMatchActual = (FootballMatch) matchListActual.get(0);
        assertEquals(footballMatchActual.getHomeTeam().getName(), MEXICO);
        assertEquals(footballMatchActual.getAwayTeam().getName(), CANADA);

        assertEquals(footballMatchActual.getScore().get(mexico), 0);
        assertEquals(footballMatchActual.getScore().get(canada), 0);

        matchListActual = rugbyScoreBoard.getMatches();
        assertInstanceOf(List.class, matchListActual);

        RugbyMatch rugbyMatchActual = (RugbyMatch) matchListActual.get(0);
        assertNotNull(rugbyMatchActual);
    }

    @Test
    void When_CreateMatch_Invoked_WithoutValid_Input_Should_Throw_RuntimeException() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>();
        assertThrows(RuntimeException.class, () -> scoreBoard.createMatch(teamnames));
        assertThrows(RuntimeException.class, () -> scoreBoard.createMatch(null));
    }

    @Test
    void When_UpdateScore_Invoked_Should_Update_Score_Of_A_Match() {
        mockMatch(MEXICO, CANADA, mexico, canada);

        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        Match match = scoreBoard.createMatch(teamnames);


        ArgumentMatcher<String> gameMatcher = gamename -> FOOTBALL.equals(gamename);
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        teams.stream().anyMatch(t -> MEXICO.equals(t))
                        && teams.stream().anyMatch(t -> CANADA.equals(t));
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                s -> s != null && s instanceof ScoreBoard;
        Mockito.verify(scoreBoardService, times(1)).createMatch(argThat(gameMatcher), argThat(teamNameMatcher),
                                                                argThat(scoreBoardArgumentMatcher));

        mockMatch(SPAIN, BRAZIL, spain, brazil);
        teamnames = new LinkedHashSet<>(Set.of(SPAIN, BRAZIL));
        Match match2 = scoreBoard.createMatch(teamnames);
        ArgumentMatcher<String> gameMatcher2 = gamename -> FOOTBALL.equals(gamename);
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher2 =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        teams.stream().anyMatch(t -> SPAIN.equals(t))
                        && teams.stream().anyMatch(t -> BRAZIL.equals(t));
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher2 =
                s -> s != null && s instanceof ScoreBoard;

        Mockito.verify(scoreBoardService, times(1)).createMatch(argThat(gameMatcher2), argThat(teamNameMatcher2),
                                                                argThat(scoreBoardArgumentMatcher2));

        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

        assertInstanceOf(Match.class, match2);
        assertInstanceOf(FootballMatch.class, match2);

        List<Match> matchListActual = scoreBoard.getMatches();
        assertInstanceOf(List.class, matchListActual);
        assertTrue(matchListActual != null);
        assertEquals(matchListActual.size(), 2);
        assertNotNull(matchListActual.get(0).getScore());
        assertNotNull(matchListActual.get(1).getScore());

        FootballMatch footballMatchActual = (FootballMatch) matchListActual.get(0);
        assertEquals(footballMatchActual.getHomeTeam().getName(), MEXICO);
        assertEquals(footballMatchActual.getAwayTeam().getName(), CANADA);

        assertEquals(footballMatchActual.getScore().get(mexico), 0);
        assertEquals(footballMatchActual.getScore().get(canada), 0);


        FootballMatch footballMatchActual2 = (FootballMatch) matchListActual.get(1);
        assertEquals(footballMatchActual2.getHomeTeam().getName(), SPAIN);
        assertEquals(footballMatchActual2.getAwayTeam().getName(), BRAZIL);

        assertEquals(footballMatchActual2.getScore().get(spain), 0);
        assertEquals(footballMatchActual2.getScore().get(brazil), 0);
    }
}
