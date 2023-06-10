package scorecard.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import scorecard.ScoreBoard;
import scorecard.repo.impl.FootballScore;
import scorecard.service.TeamsService;
import scorecard.service.impl.TeamsServiceImpl;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static scorecard.Constants.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class FootballScoreTest {
    @Spy
    private final ScoreBoard scoreBoard = new ScoreBoard(FOOTBALL);
    @InjectMocks
    Score score = new FootballScore();
    TeamsService teamsService = TeamsServiceImpl.getInstance();
    Teams mexico = teamsService.createTeams(MEXICO);
    Teams canada = teamsService.createTeams(CANADA);

    @Test
    void When_UpdateScore_Invoked_Should_Validate_If_Score_Is_A_Positive_Integer() {
        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        assertThrows(RuntimeException.class, () -> score.updateScore(mexico, null, footballMatch));
        assertThrows(RuntimeException.class, () -> score.updateScore(mexico, new Object(), footballMatch));
        assertThrows(RuntimeException.class, () -> score.updateScore(mexico, -1, footballMatch));
        assertDoesNotThrow(() -> score.updateScore(canada, 5, footballMatch));
    }

    @Test
    void When_UpdateScore_Invoked_Should_Update_Score_Of_The_Match() {
        LinkedHashSet<Teams> teams = new LinkedHashSet<>(Set.of(mexico, canada));
        FootballMatch footballMatch = new FootballMatch(new ScoreBoard(FOOTBALL), teams);
        Match matchOutput = score.updateScore(mexico, 1, footballMatch);
        assertEquals(1, matchOutput.getScore().get(mexico));
        matchOutput = score.updateScore(canada, 5, footballMatch);
        assertEquals(5, matchOutput.getScore().get(canada));
    }
}
