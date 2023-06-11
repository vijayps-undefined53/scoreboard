/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package scorecard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import org.mockito.internal.matchers.Matches;
import scorecard.repo.FootballMatch;
import scorecard.repo.Match;
import scorecard.repo.RugbyMatch;
import scorecard.repo.Teams;
import scorecard.service.MatchService;
import scorecard.service.TeamsService;
import scorecard.service.impl.ScoreBoardService;
import scorecard.service.impl.TeamsServiceImpl;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
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
    @InjectMocks
    ScoreBoard footballScoreBoard = new ScoreBoard(FOOTBALL);
    @InjectMocks
    ScoreBoard defaultGameScoreBoard = new ScoreBoard();
    @Mock
    ScoreBoardService scoreBoardService = ScoreBoardService.getInstance();
    @Mock
    ScoreBoardService rugbyScoreBoardService;
    @Mock
    MatchService matchService;

    Teams mexico = teamsService.createTeams(MEXICO);
    Teams canada = teamsService.createTeams(CANADA);
    Teams spain = teamsService.createTeams(SPAIN);
    Teams brazil = teamsService.createTeams(BRAZIL);
    Teams germany = teamsService.createTeams(GERMANY);
    Teams france = teamsService.createTeams(FRANCE);
    Teams uruguay = teamsService.createTeams(URUGUAY);
    Teams italy = teamsService.createTeams(ITALY);
    Teams argentina = teamsService.createTeams(ARGENTINA);
    Teams austrailia = teamsService.createTeams(AUSTRALIA);

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

    }

    //Add a convenience method to create Football match without passing game type , this is in addition to the generic
    // match creation method which requires game type to passed
    @Test
    void When_CreateFootballMatch_Invoked_With_HomeTeam_Name_AwayTeam_Name_Should_Create_A_Match_Instance_Of_Game_Type_Football() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        Match match = footballScoreBoard.createFootballMatch(MEXICO, CANADA);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

    }

    private Match mockMatch(String team1, String team2, Teams teams1, Teams teams2) {
        ArgumentMatcher<String> gameMatcher = FOOTBALL::equals;
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        (teams.stream().anyMatch(team1::equals)
                                || teams.stream().anyMatch(team2::equals));
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                Objects::nonNull;


        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(teams1, teams2));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(teams1);
        footballMatch.setAwayTeam(teams2);

        doReturn(footballMatch).when(
                scoreBoardService).createMatch(argThat(gameMatcher), argThat(teamNameMatcher),
                                               argThat(scoreBoardArgumentMatcher));

        return footballMatch;
    }

    //Add a convenience method to create Football match without passing game type , this is in addition to the generic
    // match creation method which requires game type to passed
    @Test
    void When_CreateFootballMatch_Invoked_With_HomeTeam_And_AwayTeam_Should_Create_A_Match_Instance_Of_Game_Type_Football_With_Score_Zero() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        Match match = footballScoreBoard.createFootballMatch(MEXICO, CANADA);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);
        assertEquals(match.getScore().get(mexico), 0);
        assertEquals(match.getScore().get(canada), 0);
    }

    //Add a convenience method to update Football score just passing in home team score ,away team score and the
    // match object
    @Test
    void When_UpdateScore_Invoked_With_HomeTeam_And_AwayTeam_Score_Should_Throw_Validation_Error_If_Home_Or_Away_Team_Is_Null()
            throws
            NoSuchFieldException,
            IllegalAccessException {

        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(footballScoreBoard, teams);
        addMatchToScoreBoard(null, footballMatch, footballScoreBoard);

        FootballMatch matchMock = new FootballMatch(footballScoreBoard, teams);
        matchMock.setHomeTeam(mexico);
        matchMock.setAwayTeam(canada);
        matchMock.updateScore(mexico, 2, matchMock);
        matchMock.updateScore(canada, 10, matchMock);

        doReturn(matchMock).when(scoreBoardService).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                        Set.of(mexico.getName(),
                                                                               canada.getName())).contains(team.getName())),
                                                                argThat(score -> {
                                                                    if (score instanceof Integer &&
                                                                            (Integer) score == 2)
                                                                        return true;
                                                                    assert score instanceof Integer;
                                                                    return (Integer) score == 10;
                                                                }),
                                                                argThat(matchArg -> matchArg instanceof FootballMatch));

        assertThrows(RuntimeException.class, () -> footballScoreBoard.updateFootballMatchScore(2, 10, MEXICO,
                                                                                               CANADA));
    }

    private void addMatchToScoreBoard(FootballMatch original, Match matchMock, ScoreBoard fscoreBoard)
            throws
            NoSuchFieldException,
            IllegalAccessException {
        Field matches
                = ScoreBoard.class.getDeclaredField("matches");

        // Set the accessibility as true
        matches.setAccessible(true);

        List matchSet = (ArrayList<Matches>) matches.get(fscoreBoard);
        if (original != null) {
            fscoreBoard.finishMatch(original);
        }
        matchSet.add(matchMock);
    }

    //Add a convenience method to update Football score just passing in home team score ,away team score and the
    // match object
    @Test
    void When_UpdateScore_Invoked_With_HomeTeam_And_AwayTeam_Score_Should_Throw_Validation_Error_If_Game_Of_ScoreBord_Not_FootBall()
            throws
            NoSuchFieldException,
            IllegalAccessException {

        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(footballScoreBoard, teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);
        addMatchToScoreBoard(null, footballMatch, footballScoreBoard);

        FootballMatch matchMock = new FootballMatch(footballScoreBoard, teams);
        matchMock.setHomeTeam(mexico);
        matchMock.setAwayTeam(canada);
        matchMock.updateScore(mexico, 2, matchMock);
        matchMock.updateScore(canada, 10, matchMock);

        doReturn(matchMock).when(scoreBoardService).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                        Set.of(mexico.getName(),
                                                                               canada.getName())).contains(team.getName())),
                                                                argThat(score -> {
                                                                    if (score instanceof Integer &&
                                                                            (Integer) score == 2)
                                                                        return true;
                                                                    assert score instanceof Integer;
                                                                    return (Integer) score == 10;
                                                                }),
                                                                argThat(matchArg -> matchArg instanceof FootballMatch));

        assertThrows(RuntimeException.class,
                     () -> rugbyScoreBoard.updateFootballMatchScore(2, 10, MEXICO, CANADA));
    }

    @Test
    void When_UpdateScore_Invoked_With_HomeTeam_And_AwayTeam_Score_Should_update_footballmatch_score()
            throws
            NoSuchFieldException,
            IllegalAccessException {

        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(footballScoreBoard, teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);
        addMatchToScoreBoard(null, footballMatch, footballScoreBoard);

        FootballMatch matchMock = new FootballMatch(footballScoreBoard, teams);
        matchMock.setHomeTeam(mexico);
        matchMock.setAwayTeam(canada);
        matchMock.updateScore(mexico, 2, matchMock);
        matchMock.updateScore(canada, 10, matchMock);

        doReturn(matchMock).when(scoreBoardService).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                        Set.of(mexico.getName(),
                                                                               canada.getName())).contains(team.getName())),
                                                                argThat(score -> {
                                                                    if (score instanceof Integer &&
                                                                            (Integer) score == 2)
                                                                        return true;
                                                                    assert score instanceof Integer;
                                                                    return (Integer) score == 10;
                                                                }),
                                                                argThat(matchArg -> matchArg instanceof FootballMatch));

        Match match = footballScoreBoard.updateFootballMatchScore(2, 10, MEXICO, CANADA);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);
        assertEquals(match.getScore().get(mexico), 2);
        assertEquals(match.getScore().get(canada), 10);
    }

    @Test
    void When_CreateMatch_Invoked_WithOut_Passing_GameType_Should_CreateMatchInstance_Of_Default_Game_Football() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        Match match = defaultGameScoreBoard.createMatch(teamnames);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);
    }

    @Test
    void When_CreateMatch_Invoked_Should_CreateMatchInstance_Based_On_Game() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        Match match = scoreBoard.createMatch(teamnames);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

        // Add Another type of game to another score board
        ArgumentMatcher<String> rugbyMatcher = RUGBY::equals;
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher2 =
                teams2 -> teams2 != null && teams2.stream().allMatch(Objects::nonNull);
        ArgumentMatcher<ScoreBoard> scoreCardMatcher2 =
                Objects::nonNull;
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

    @Test
    void When_CreateMatch_Invoked_WithOut_Passing_GameType_Should_Update_Football_Score_Of_Home_Away_To_Zero() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        FootballMatch match = (FootballMatch) defaultGameScoreBoard.createMatch(teamnames);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);
        assertInstanceOf(FootballMatch.class, match);
        assertEquals(match.getHomeTeam().getName(), MEXICO);
        assertEquals(match.getAwayTeam().getName(), CANADA);

        assertEquals(match.getScore().get(mexico), 0);
        assertEquals(match.getScore().get(canada), 0);
    }

    @Test
    void When_CreateMatch_Invoked_Should_Update_Score_Of_Teams_Of_Match_TO_Zero() {
        mockMatch(MEXICO, CANADA, mexico, canada);

        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        Match match = scoreBoard.createMatch(teamnames);


        ArgumentMatcher<String> gameMatcher = FOOTBALL::equals;
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        (teams.stream().anyMatch(MEXICO::equals)
                                || teams.stream().anyMatch(CANADA::equals));
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                Objects::nonNull;
        Mockito.verify(scoreBoardService, times(1)).createMatch(argThat(gameMatcher), argThat(teamNameMatcher),
                                                                argThat(scoreBoardArgumentMatcher));

        mockMatch(SPAIN, BRAZIL, spain, brazil);
        teamnames = new LinkedHashSet<>(Set.of(SPAIN, BRAZIL));
        Match match2 = scoreBoard.createMatch(teamnames);
        ArgumentMatcher<String> gameMatcher2 = FOOTBALL::equals;
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher2 =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        teams.stream().anyMatch(SPAIN::equals)
                        && teams.stream().anyMatch(BRAZIL::equals);
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher2 =
                Objects::nonNull;

        Mockito.verify(scoreBoardService, times(1)).createMatch(argThat(gameMatcher2), argThat(teamNameMatcher2),
                                                                argThat(scoreBoardArgumentMatcher2));

        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

        assertInstanceOf(Match.class, match2);
        assertInstanceOf(FootballMatch.class, match2);

        List<Match> matchListActual = scoreBoard.getMatches();
        assertInstanceOf(List.class, matchListActual);
        assertNotNull(matchListActual);
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

    @Test
    void When_GetMatches_Invoked_Should_Return_Matches_Linked_To_ScoreCard() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        Match match = scoreBoard.createMatch(teamnames);
        assertInstanceOf(Match.class, match);
        assertInstanceOf(FootballMatch.class, match);

        // Add Another type of game to another score board
        ArgumentMatcher<String> rugbyMatcher = RUGBY::equals;
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher2 =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        teams.stream().anyMatch(SPAIN::equals)
                        && teams.stream().anyMatch(BRAZIL::equals);
        ArgumentMatcher<ScoreBoard> scoreCardMatcher2 =
                Objects::nonNull;

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
        assertNotNull(matchListActual);
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
    void When_CreateMatch_Invoked_Without_Valid_Input_Should_Throw_RuntimeException() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamnames = new LinkedHashSet<>();
        assertThrows(RuntimeException.class, () -> scoreBoard.createMatch(teamnames), VALID_TEAM_NAME_EXPECTED);
        assertThrows(RuntimeException.class, () -> scoreBoard.createMatch(null), VALID_TEAM_NAME_EXPECTED);
    }

    @Test
    void When_CreateScore_Invoked_With_Team_Already_Playing_A_Match_Should_Throw_Validation_Exception() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        LinkedHashSet<String> teamNames = new LinkedHashSet<>(Set.of(MEXICO, CANADA));
        Match match = scoreBoard.createMatch(teamNames);
        assertNotNull(match);
        assertInstanceOf(Match.class, match);
        assertThrows(RuntimeException.class, () -> scoreBoard.createMatch(teamNames));
    }

    @Test
    void When_UpdateScore_Invoked_With_A_Team_Not_In_Match_Should_Throw_Exception() {
        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);

        Match matchMockUpdate = new FootballMatch(scoreBoard, teams);


        matchMockUpdate.getScore().put(mexico, 0);
        matchMockUpdate.getScore().put(canada, 5);

        doReturn(matchMockUpdate).when(scoreBoardService).updateScore(argThat(team -> new LinkedHashSet<>(
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
        scoreUpdate.put("USA", 1);
        scoreUpdate.put("CAMBODIA", 1);

        assertThrows(RuntimeException.class, () -> scoreBoard.updateScore(scoreUpdate),
                     VALIDATION_ERROR_ON_UPDATING_SCORE_WITH_TEAM_NOT_IN_MATCH);

    }

    @Test
    void When_UpdateScore_Invoked_With_A_Team_Not_In_ScoreBoard_Should_Throw_Exception() {
        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);
        Match matchMockUpdate = new FootballMatch(scoreBoard, teams);


        matchMockUpdate.getScore().put(mexico, 0);
        matchMockUpdate.getScore().put(canada, 5);

        doReturn(matchMockUpdate).when(scoreBoardService).updateScore(argThat(team -> new LinkedHashSet<>(
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
        scoreUpdate.put(MEXICO, 1);
        scoreUpdate.put(CANADA, 1);

        assertThrows(RuntimeException.class, () -> scoreBoard.updateScore(scoreUpdate),
                     "Match should be associated to that score board");
    }

    @Test
    void When_UpdateScore_Invoked_Should_Update_Score_Of_A_Match()
            throws
            NoSuchFieldException,
            IllegalAccessException {
        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        footballMatch.setHomeTeam(mexico);
        footballMatch.setAwayTeam(canada);
        Match matchMock = new FootballMatch(scoreBoard, teams);

        Match matchMockUpdate = new FootballMatch(scoreBoard, teams);


        matchMockUpdate.getScore().put(mexico, 0);
        matchMockUpdate.getScore().put(canada, 5);

        doReturn(matchMockUpdate).when(scoreBoardService).updateScore(argThat(team -> new LinkedHashSet<>(
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
        addMatchToScoreBoard(null, matchMock, scoreBoard);
        Match matchResponse = scoreBoard.updateScore(scoreUpdate);

        assertNotNull(matchResponse.getScore());
        assertNotNull(matchResponse.getScore().get(mexico));
        assertNotNull(matchResponse.getScore().get(canada));
        assertEquals(5, matchResponse.getScore().get(canada), String.format("Score not updated for %s", canada));
        assertEquals(0, matchResponse.getScore().get(mexico), String.format("Score not updated for %s", mexico));
    }

    @Test
    void When_FinishMatch_Invoked_With_Match_Not_In_ScoreBoard_Should_Throw_An_Exception() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        Match footballMatch = footballScoreBoard.createFootballMatch(MEXICO, CANADA);
        assertInstanceOf(Match.class, footballMatch);
        assertInstanceOf(FootballMatch.class, footballMatch);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatch));


        mockRugbyMatch(SPAIN, BRAZIL, spain, brazil);
        LinkedHashSet<String> teamNames = new LinkedHashSet<>(Set.of(SPAIN, BRAZIL));
        RugbyMatch rugbyScoreBoardMatch = (RugbyMatch) rugbyScoreBoard.createMatch(teamNames);
        assertNotNull(rugbyScoreBoardMatch);
        assertInstanceOf(Match.class, rugbyScoreBoardMatch);
        assertTrue(rugbyScoreBoard.getMatches().contains(rugbyScoreBoardMatch));

        assertThrows(RuntimeException.class, () -> footballScoreBoard.finishMatch(rugbyScoreBoardMatch));
        assertThrows(RuntimeException.class, () -> rugbyScoreBoard.finishMatch(footballMatch));

        assertTrue(footballScoreBoard.getMatches().contains(footballMatch));
        assertTrue(rugbyScoreBoard.getMatches().contains(rugbyScoreBoardMatch));
    }

    private Match mockRugbyMatch(String team1, String team2, Teams teams1, Teams teams2) {
        ArgumentMatcher<String> gameMatcher = RUGBY::equals;
        ArgumentMatcher<LinkedHashSet<String>> teamNameMatcher =
                teams -> teams != null && teams.stream().allMatch(Objects::nonNull) &&
                        (teams.stream().anyMatch(team1::equals)
                                || teams.stream().anyMatch(team2::equals));
        ArgumentMatcher<ScoreBoard> scoreBoardArgumentMatcher =
                Objects::nonNull;


        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(teams1, teams2));
        RugbyMatch rugbyMatch = new RugbyMatch(new ScoreBoard(RUGBY), teams);

        doReturn(rugbyMatch).when(
                scoreBoardService).createMatch(argThat(gameMatcher), argThat(teamNameMatcher),
                                               argThat(scoreBoardArgumentMatcher));
        return rugbyMatch;
    }

    @Test
    void When_FinishMatch_Invoked_Should_Remove_Match_From_ScoreBoard() {
        mockMatch(MEXICO, CANADA, mexico, canada);
        Match footballMatch = footballScoreBoard.createFootballMatch(MEXICO, CANADA);
        assertInstanceOf(Match.class, footballMatch);
        assertInstanceOf(FootballMatch.class, footballMatch);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatch));
        footballScoreBoard.finishMatch(footballMatch);
        assertFalse(footballScoreBoard.getMatches().contains(footballMatch));

        mockRugbyMatch(SPAIN, BRAZIL, spain, brazil);
        LinkedHashSet<String> teamNames = new LinkedHashSet<>(Set.of(SPAIN, BRAZIL));
        RugbyMatch rugbyScoreBoardMatch = (RugbyMatch) rugbyScoreBoard.createMatch(teamNames);
        assertNotNull(rugbyScoreBoardMatch);
        assertInstanceOf(Match.class, rugbyScoreBoardMatch);
        assertTrue(rugbyScoreBoard.getMatches().contains(rugbyScoreBoardMatch));
        rugbyScoreBoard.finishMatch(rugbyScoreBoardMatch);
        assertFalse(rugbyScoreBoard.getMatches().contains(rugbyScoreBoardMatch));
    }

    @Test
    void When_SummaryOfMatches_Invoked_Should_Get_A_Summary_Of_Matches_Order_By_Total_Score_And_Same_Total_Score_By_Recency_TestWithChangeInOrder()
            throws
            NoSuchFieldException,
            IllegalAccessException {
        mockMatch(MEXICO, CANADA, mexico, canada);
        FootballMatch footballMatchMexicoCanada =
                (FootballMatch) footballScoreBoard.createFootballMatch(MEXICO, CANADA);
        footballMatchMexicoCanada.setHomeTeam(mexico);
        footballMatchMexicoCanada.setAwayTeam(canada);
        assertInstanceOf(Match.class, footballMatchMexicoCanada);
        assertInstanceOf(FootballMatch.class, footballMatchMexicoCanada);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchMexicoCanada));
        updateScore(footballMatchMexicoCanada, 0, 5, mexico,
                    canada);


        mockMatch(URUGUAY, ITALY, uruguay, italy);
        Match footballMatchUruguayItaly = footballScoreBoard.createFootballMatch(URUGUAY, ITALY);
        assertInstanceOf(Match.class, footballMatchUruguayItaly);
        assertInstanceOf(FootballMatch.class, footballMatchUruguayItaly);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchUruguayItaly));
        updateScore((FootballMatch) footballMatchUruguayItaly, 6, 6, uruguay, italy);

        mockMatch(ARGENTINA, AUSTRALIA, argentina, austrailia);
        Match footballMatchArgentinaAustrailia = footballScoreBoard.createFootballMatch(ARGENTINA, AUSTRALIA);
        assertInstanceOf(Match.class, footballMatchArgentinaAustrailia);
        assertInstanceOf(FootballMatch.class, footballMatchArgentinaAustrailia);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchArgentinaAustrailia));
        updateScore((FootballMatch) footballMatchArgentinaAustrailia, 3, 1, argentina, austrailia);

        mockMatch(SPAIN, BRAZIL, spain, brazil);
        Match footballMatchSpainBrazil = footballScoreBoard.createFootballMatch(SPAIN, BRAZIL);
        assertInstanceOf(Match.class, footballMatchSpainBrazil);
        assertInstanceOf(FootballMatch.class, footballMatchSpainBrazil);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchSpainBrazil));
        updateScore((FootballMatch) footballMatchSpainBrazil, 10, 2, spain, brazil);

        mockMatch(GERMANY, FRANCE, germany, france);
        Match footballMatchGermanyFrance = footballScoreBoard.createFootballMatch(GERMANY, FRANCE);
        assertInstanceOf(Match.class, footballMatchGermanyFrance);
        assertInstanceOf(FootballMatch.class, footballMatchGermanyFrance);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchGermanyFrance));
        updateScore((FootballMatch) footballMatchGermanyFrance, 2, 2, germany, france);

        String summaryOfMatches = "1. SPAIN 10 - BRAZIL 2\n" +
                "2. URUGUAY 6 - ITALY 6\n" +
                "3. MEXICO 0 - CANADA 5\n" +
                "4. GERMANY 2 - FRANCE 2\n" +
                "5. ARGENTINA 3 - AUSTRALIA 1";
        String actual = footballScoreBoard.getSummaryOfMatches();
        assertNotNull(actual);
        assertEquals(summaryOfMatches, actual);
    }

    private void updateScore(FootballMatch footballMatch, Integer homeTeamScore, Integer awayTeamScore, Teams homeTeam,
                             Teams awayTeam)
            throws
            NoSuchFieldException,
            IllegalAccessException {
        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(homeTeam, awayTeam));
        FootballMatch matchMockUpdate = new FootballMatch(scoreBoard, teams);
        matchMockUpdate.setHomeTeam(homeTeam);
        matchMockUpdate.setAwayTeam(awayTeam);
        matchMockUpdate.getScore().put(homeTeam, homeTeamScore);
        matchMockUpdate.getScore().put(awayTeam, awayTeamScore);
        FootballMatch original = footballMatch;
        doReturn(matchMockUpdate).when(scoreBoardService).updateScore(argThat(team -> new LinkedHashSet<>(
                                                                              Set.of(homeTeam.getName(),
                                                                                     awayTeam.getName())).contains(team.getName())),
                                                                      argThat(score -> {
                                                                          if (score instanceof Integer &&
                                                                                  score == homeTeamScore)
                                                                              return true;
                                                                          assert score instanceof Integer;
                                                                          return score == awayTeamScore;
                                                                      }),
                                                                      argThat(matchArg -> matchArg instanceof FootballMatch));
        footballMatch =
                (FootballMatch) footballScoreBoard.updateFootballMatchScore(homeTeamScore, awayTeamScore,
                                                                            homeTeam.getName(), awayTeam.getName());
        addMatchToScoreBoard(original, footballMatch, footballScoreBoard);
    }

    @Test
    void When_SummaryOfMatches_Invoked_Should_Get_A_Summary_Of_Matches_Order_By_Total_Score_And_Same_Total_Score_By_Recency()
            throws
            NoSuchFieldException,
            IllegalAccessException {
        mockMatch(MEXICO, CANADA, mexico, canada);
        FootballMatch footballMatchMexicoCanada =
                (FootballMatch) footballScoreBoard.createFootballMatch(MEXICO, CANADA);
        footballMatchMexicoCanada.setHomeTeam(mexico);
        footballMatchMexicoCanada.setAwayTeam(canada);
        assertInstanceOf(Match.class, footballMatchMexicoCanada);
        assertInstanceOf(FootballMatch.class, footballMatchMexicoCanada);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchMexicoCanada));
        updateScore(footballMatchMexicoCanada, 0, 5, mexico,
                    canada);


        mockMatch(SPAIN, BRAZIL, spain, brazil);
        Match footballMatchSpainBrazil = footballScoreBoard.createFootballMatch(SPAIN, BRAZIL);
        assertInstanceOf(Match.class, footballMatchSpainBrazil);
        assertInstanceOf(FootballMatch.class, footballMatchSpainBrazil);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchSpainBrazil));
        updateScore((FootballMatch) footballMatchSpainBrazil, 10, 2, spain, brazil);


        mockMatch(GERMANY, FRANCE, germany, france);
        Match footballMatchGermanyFrance = footballScoreBoard.createFootballMatch(GERMANY, FRANCE);
        assertInstanceOf(Match.class, footballMatchGermanyFrance);
        assertInstanceOf(FootballMatch.class, footballMatchGermanyFrance);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchGermanyFrance));
        updateScore((FootballMatch) footballMatchGermanyFrance, 2, 2, germany, france);

        mockMatch(URUGUAY, ITALY, uruguay, italy);
        Match footballMatchUruguayItaly = footballScoreBoard.createFootballMatch(URUGUAY, ITALY);
        assertInstanceOf(Match.class, footballMatchUruguayItaly);
        assertInstanceOf(FootballMatch.class, footballMatchUruguayItaly);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchUruguayItaly));
        updateScore((FootballMatch) footballMatchUruguayItaly, 6, 6, uruguay, italy);

        mockMatch(ARGENTINA, AUSTRALIA, argentina, austrailia);
        Match footballMatchArgentinaAustrailia = footballScoreBoard.createFootballMatch(ARGENTINA, AUSTRALIA);
        assertInstanceOf(Match.class, footballMatchArgentinaAustrailia);
        assertInstanceOf(FootballMatch.class, footballMatchArgentinaAustrailia);
        assertTrue(footballScoreBoard.getMatches().contains(footballMatchArgentinaAustrailia));
        updateScore((FootballMatch) footballMatchArgentinaAustrailia, 3, 1, argentina, austrailia);

        String summaryOfMatches = "1. URUGUAY 6 - ITALY 6\n" +
                "2. SPAIN 10 - BRAZIL 2\n" +
                "3. MEXICO 0 - CANADA 5\n" +
                "4. ARGENTINA 3 - AUSTRALIA 1\n" +
                "5. GERMANY 2 - FRANCE 2";
        String actual = footballScoreBoard.getSummaryOfMatches();
        assertNotNull(actual);
        assertEquals(summaryOfMatches, actual);
    }

}
