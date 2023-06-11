# Scoreboard
 Scoreboard library that shows all the ongoing matches and their scores.
 This library uses gradle as build tool and ./gradlew clean build to build jar, add this library as dependency by 	
 {
  implementation files('{directory location}/lib/build/libs/lib-0.1.0.jar').
 }
 
 Score Board can initialised by default constructor as follows this will configure scoreboard with game type as football:
 {
  ScoreBoard scoreBoard = new ScoreBoard(); 
 }
 
 Score Board can initialised by passing in name String constructor as follows this will configure scoreboard with game type as name: 
  {
   ScoreBoard scoreBoard = new ScoreBoard("football"); 
   ScoreBoard scoreBoardRugby = new ScoreBoard("rugby"); // Note: Football game is the fully supported and default game type  
  }
  
  The scoreboard supports the following operations:
  1. Start a new match (any game or Football), initialise score 0 – 0 and add it to the scoreboard, it has two implementations.
            
            1. public Match createMatch(LinkedHashSet<String> teams) method: Start a new football match and passing in home and away team name.            
               A generic match (can be a football match, it will depend on game string passed to ScoreBoard constructor) can be created as follows,
               { 
                  LinkedHashSet<String> teamNames = new LinkedHashSet<>(Set.of("SPAIN", "BRAZIL"));
                  scoreBoard.createMatch(teamNames); 
               }
                 
            2. public Match createFootballMatch(String homeTeam, String awayTeam) method: Start a new match for other games and passing in teams names as LinkedHashSet.
               A football match can be created as follows by passing in home team and away team name.
               { 
                 FootballMatch footballMatch = (FootballMatch) scoreBoard.createFootballMatch("MEXICO", "CANADA");
               }   


           
    2. Update score:It has two implementations one is generic and other as a convenience method for football
            
            1. public Match updateFootballMatchScore(Integer homeTeamScore, Integer awayTeamScore, String homeTeamName, String awayTeamName) method: Update a football match score and passing in home and away
               team goals(expecting zero or positive integer) and name, this method can be used only if scoreboard is
               based on Football game for other games use generic method mentioned next
               
               Important:All teams in input should be belonging to SAME Match and all teams in match should be given in input, else runtime exception is thrown
               This implementation finds Match object from scoreboard based on the team name in input and updates it.
               
               The input receives Integer homeTeamScore, Integer awayTeamScore, String homeTeamName,String awayTeamName
               {
                  scoreBoard.updateFootballMatchScore(0,5,"MEXICO","CANADA");
               }
                
            2. public Match updateScore(Map<String, Object> score) method: Update a match (any game - football or any other game) score.
               The input receives a Map of team names and an object representing score.
               
               Important:All teams in input MAP should be belonging to SAME Match (Cannot update more than one match in same input) and all teams in match should be given in input MAP, else runtime exception is thrown.
               This implementation finds Match object from scoreboard based on the team name in input and updates it.
                              
               A match score can be updated as follows,
               The input receives a Map of team names and an object representing score.
               The team names in map should have all teams in match and should be part of same match.
               { 
                  Map<String, Object> score = new HashMap<>();
                  score.put("MEXICO",5);
                  score.put("CANADA",6);
                  scoreBoard.updateScore(score);  
               }
               
    3. Finish match currently in progress. This removes a match from the scoreboard, it has two implementations.

            1. public void finishMatchByTeamName(String teamName) method removes a match from Scoreboard associated to this team name in input.
               The input receives String team name in match.
               {
                 scoreBoard.finishMatchByTeamName("SPAIN");
                }
                
            2. finishMatch method removes a match from Scoreboard by taking in Match object as input.   
               The input receives an object of Match type (can be any implementations of Match like FootballMatch) .  
               {
                 scoreBoard.finishMatch(footballMatch);
               }
  
    4. Get a summary of matches in progress ordered by their total score. The matches with the same total score will
       be returned ordered by the most recently started match in the scoreboard, it has two implementations.
       
            1. getSummaryAsAString method Gets a String which gives Summary of Matches, as described in order above.
               {
                String summary = scoreBoard.getSummaryAsAString(); 
               }
               
              This implementation gives a string summary as following example pattern:
              1. Uruguay 6 - Italy 6
              2. Spain 10 - Brazil 2
              3. Mexico 0 - Canada 5
              4. Argentina 3 - Australia 1
              5. Germany 2 - France 2
            
            2. getSummaryOfMatchObjects method Gets a List of Match objects which gives Summary of Matches, as described in order above.
               {
                 List<Match> list = scoreBoard.getSummaryOfMatchObjects();
                 System.out.println(list.stream().map(m->m.toString()).collect(Collectors.joining(",")));
               }
    5. Get a summary of matches in progress ordered by their created date, it has one implementation.
            1. getMatchesOrderedByCreatedDate method Gets a List of Match objects which gives Summary of Matches in
               progress ordered by their creation date.  
               {
                 List<Match> listOrderedByCreatedDate = scoreBoard.getMatchesOrderedByCreatedDate();
               }
  
    This ScoreBoard supports football by default, other games are supported by explicitly passing in game type.
    To use this scoreboard for a new game type implement the abstract Match class and ScoringStrategy interface for
    that game. 
    
    A factory pattern(MatchFactory class) for selecting Match interface implementation based on game type(string game name)
    and a ScoringStrategy class takes as constructor param Score interface type, it picks the scoring strategy, using which different scoring operation .
    
    Currently, football and Rugby related Scoring strategy and Match class type is supported.
        1. By default, it supports football as it's game type.
        2. Scoreboard has a constructor that takes in a string as game type.


