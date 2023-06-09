package scorecard.repo.impl;

import scorecard.repo.Match;
import scorecard.repo.Score;
import scorecard.repo.Teams;

public class TennisScore
        implements Score {
    @Override
    public void updateScore(Teams teams, Object score, Match match) {
        /*
        Here Match can be an instance of TennisMatch object
        Here Tennis Scoring can be a Complex object having structure of Set having Game, each Game having Points.
         */
    }
}
