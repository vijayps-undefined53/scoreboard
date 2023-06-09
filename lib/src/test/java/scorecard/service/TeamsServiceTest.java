package scorecard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import scorecard.repo.Teams;
import scorecard.service.impl.TeamsServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static scorecard.Constants.MEXICO;

public class TeamsServiceTest {

    private final TeamsService teamsService = TeamsServiceImpl.getInstance();

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void When_GetInstance_Invoked_Should_Create_ScoreCard_Instance() {
        TeamsService teamsService = TeamsServiceImpl.getInstance();
        assertInstanceOf(TeamsService.class, teamsService);
    }

    @Test
    void When_CreateMatch_Invoked_Should_Create_Match_Instance_Based_On_Game() {
        Teams mexico = teamsService.createTeams(MEXICO);
        assertInstanceOf(Teams.class, mexico);
        assertEquals(mexico.getName(), MEXICO);
    }
}
