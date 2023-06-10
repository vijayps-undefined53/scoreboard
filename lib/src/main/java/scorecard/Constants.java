package scorecard;

public class Constants {
    public static final String FOOTBALL = "football";
    public static final String RUGBY = "rugby";
    public static final String MEXICO = "MEXICO";
    public static final String CANADA = "CANADA";
    public static final String SPAIN = "SPAIN";
    public static final String BRAZIL = "BRAZIL";

    public static final String VALIDATION_ERROR_ON_UPDATING_SCORE_WITH_TEAM_NOT_IN_MATCH =
            "Only score of Teams in this match can be updated, one or any of team name provided is not name of team " +
                    "in this match";
    public static final String VALID_TEAM_NAME_EXPECTED =
            "Valid teams names expected, team names is a string and cannot be null or " +
                    "blank";
}
