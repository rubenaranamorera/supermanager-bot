import model.ACBPlayer;
import model.ACBTeam;
import model.PlayerGame;
import model.TeamGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDB {

  private HashMap<String, List<TeamGame>> teamGamesList = new HashMap<>();
  private HashMap<String, ACBTeam> teams = new HashMap<>();
  private HashMap<String, ACBPlayer> players = new HashMap<>();
  private HashMap<String, Float> playerPredictions = new HashMap<>();


  public void addTeamGame(TeamGame teamGame) {

    if (!teamGamesList.containsKey(teamGame.getTeam1Name())) {
      teamGamesList.put(teamGame.getTeam1Name(), new ArrayList<>());
    }

    if (!teamGamesList.containsKey(teamGame.getTeam2Name())) {
      teamGamesList.put(teamGame.getTeam2Name(), new ArrayList<>());
    }

    List<TeamGame> team1List = teamGamesList.get(teamGame.getTeam1Name());
    team1List.add(teamGame);
    teamGamesList.put(teamGame.getTeam1Name(), team1List);

    List<TeamGame> team2List = teamGamesList.get(teamGame.getTeam2Name());
    team2List.add(teamGame);
    teamGamesList.put(teamGame.getTeam2Name(), team2List);

  }

  public void createPlayerGamesList() {
    for (Map.Entry<String, List<TeamGame>> entry : teamGamesList.entrySet()) {
      for (TeamGame game : entry.getValue()) {
        if (game.getTeam1Name().equals(entry.getKey())) {
          addPlayerGames(game.getTeam1PlayerGames());
        } else {
          addPlayerGames(game.getTeam2PlayerGames());
        }
      }
    }
  }

  private void addPlayerGames(List<PlayerGame> playerGames) {
    for (PlayerGame playerGame : playerGames) {
      ACBPlayer acbPlayer = ACBPlayer.newBuilder()
          .withTeamName(playerGame.getTeamName())
          .withName(playerGame.getName())
          .withPlayerGames(new ArrayList<>())
          .build();
      if (players.containsKey(playerGame.getName())) {
        acbPlayer = players.get(playerGame.getName());
      }
      List<PlayerGame> playerGamesList = acbPlayer.getPlayerGames();
      playerGamesList.add(playerGame);
      players.put(playerGame.getName(), ACBPlayer.newBuilder()
          .withTeamName(playerGame.getTeamName())
          .withName(playerGame.getName())
          .withPlayerGames(playerGamesList)
          .build());

    }
  }

  public void createTeamStatistics() {
    for (Map.Entry<String, List<TeamGame>> entry : teamGamesList.entrySet()) {
      teams.put(entry.getKey(), createTeam(entry.getKey(), entry.getValue()));
    }
  }

  private ACBTeam createTeam(String name, List<TeamGame> teamGames) {

    int wins = 0;
    int loses = 0;

    int points = 0;
    int threes = 0;
    int rebounds = 0;
    int assists = 0;
    int efficiency = 0;

    int pointsReceived = 0;
    int threesReceived = 0;
    int reboundsReceived = 0;
    int assistsReceived = 0;
    int efficiencyReceived = 0;

    int homeEfficiency = 0;
    int awayEfficiency = 0;
    int homeReceivedEfficiency = 0;
    int awayReceivedEfficiency = 0;

    int homeWins = 0;
    int homeLoses = 0;
    int awayWins = 0;
    int awayLoses = 0;

    for (TeamGame game : teamGames) {
      ACBTeam acbTeam;
      boolean isAtHome = game.getTeam1Name().equals(name);

      if (isAtHome) {
        acbTeam = createTeamAfterAGame(game.getTeam1PlayerGames(), game.getTeam2PlayerGames(), isAtHome);
        homeEfficiency = homeEfficiency + acbTeam.getHomeEfficiency();
        homeReceivedEfficiency = homeReceivedEfficiency + acbTeam.getHomeEfficiencyReceived();
        homeWins = homeWins + acbTeam.getHomeWins();
        homeLoses = homeLoses + acbTeam.getHomeLoses();
      } else {
        acbTeam = createTeamAfterAGame(game.getTeam2PlayerGames(), game.getTeam1PlayerGames(), isAtHome);
        awayEfficiency = awayEfficiency + acbTeam.getAwayEfficiency();
        awayReceivedEfficiency = awayReceivedEfficiency + acbTeam.getAwayEfficiencyReceived();
        awayWins = awayWins + acbTeam.getAwayWins();
        awayLoses = awayLoses + acbTeam.getAwayLoses();
      }

      points = points + acbTeam.getPoints();
      threes = threes + acbTeam.getThrees();
      assists = assists + acbTeam.getAssists();
      rebounds = rebounds + acbTeam.getRebounds();
      efficiency = efficiency + acbTeam.getEfficiency();
      pointsReceived = pointsReceived + acbTeam.getPointsReceived();
      threesReceived = threesReceived + acbTeam.getThreesReceived();
      assistsReceived = assistsReceived + acbTeam.getAssistsReceived();
      reboundsReceived = reboundsReceived + acbTeam.getReboundsReceived();
      efficiencyReceived = efficiencyReceived + acbTeam.getEfficiencyReceived();
      wins = wins + acbTeam.getWins();
      loses = loses + acbTeam.getLoses();
    }

    return ACBTeam.newBuilder()
        .withName(name)
        .withWins(wins)
        .withLoses(loses)
        .withPoints(points)
        .withThrees(threes)
        .withRebounds(rebounds)
        .withAssists(assists)
        .withEfficiency(efficiency)
        .withPointsReceived(pointsReceived)
        .withThreesReceived(threesReceived)
        .withReboundsReceived(reboundsReceived)
        .withAssistsReceived(assistsReceived)
        .withEfficiencyReceived(efficiencyReceived)
        .withHomeEfficiency(homeEfficiency)
        .withAwayEfficiency(awayEfficiency)
        .withHomeEfficiencyReceived(homeReceivedEfficiency)
        .withAwayEfficiencyReceived(awayReceivedEfficiency)
        .withHomeWins(homeWins)
        .withHomeLoses(homeLoses)
        .withAwayWins(awayWins)
        .withAwayLoses(awayLoses)
        .build();
  }

  private ACBTeam createTeamAfterAGame(List<PlayerGame> team1PlayerGames, List<PlayerGame> team2PlayerGames, boolean isAtHome) {
    int wins = 0;
    int loses = 0;

    int points = 0;
    int threes = 0;
    int rebounds = 0;
    int assists = 0;
    int efficiency = 0;

    int pointsReceived = 0;
    int threesReceived = 0;
    int reboundsReceived = 0;
    int assistsReceived = 0;
    int efficiencyReceived = 0;

    int homeEfficiency = 0;
    int homeEfficiencyReceived = 0;
    int awayEfficiency = 0;
    int awayEfficiencyReceived = 0;

    int homeWins = 0;
    int homeLoses = 0;
    int awayWins = 0;
    int awayLoses = 0;

    for (PlayerGame playerGame : team1PlayerGames) {
      points = points + playerGame.getPoints();
      threes = threes + playerGame.getThrees();
      assists = assists + playerGame.getAssists();
      rebounds = rebounds + playerGame.getRebounds();
      efficiency = efficiency + playerGame.getEfficiency();
      if (isAtHome){
        homeEfficiency = homeEfficiency + playerGame.getEfficiency();
      } else{
        awayEfficiency = awayEfficiency + playerGame.getEfficiency();
      }
    }

    for (PlayerGame playerGame : team2PlayerGames) {
      pointsReceived = pointsReceived + playerGame.getPoints();
      threesReceived = threesReceived + playerGame.getThrees();
      assistsReceived = assistsReceived + playerGame.getAssists();
      reboundsReceived = reboundsReceived + playerGame.getRebounds();
      efficiencyReceived = efficiencyReceived + playerGame.getEfficiency();
      if (isAtHome){
        homeEfficiencyReceived = homeEfficiencyReceived + playerGame.getEfficiency();
      } else{
        awayEfficiencyReceived = awayEfficiencyReceived + playerGame.getEfficiency();
      }
    }

    if (points > pointsReceived) {
      if (isAtHome){
        homeWins = 1;
      } else {
        awayWins = 1;
      }
      wins = 1;
    } else {
      if (isAtHome){
        homeLoses = 1;
      } else {
        awayLoses = 1;
      }
      loses = 1;
    }

    return ACBTeam.newBuilder()
        .withWins(wins)
        .withLoses(loses)
        .withPoints(points)
        .withThrees(threes)
        .withRebounds(rebounds)
        .withAssists(assists)
        .withEfficiency(efficiency)
        .withPointsReceived(pointsReceived)
        .withThreesReceived(threesReceived)
        .withReboundsReceived(reboundsReceived)
        .withAssistsReceived(assistsReceived)
        .withEfficiencyReceived(efficiencyReceived)
        .withHomeEfficiency(homeEfficiency)
        .withHomeEfficiencyReceived(homeEfficiencyReceived)
        .withAwayEfficiency(awayEfficiency)
        .withAwayEfficiencyReceived(awayEfficiencyReceived)
        .withHomeWins(homeWins)
        .withHomeLoses(homeLoses)
        .withAwayWins(awayWins)
        .withAwayLoses(awayLoses)
        .build();
  }

  public Collection<ACBPlayer> getPlayers() {
    return players.values();
  }

  public ACBTeam getACBTeam(String teamName) {
    return teams.get(teamName);
  }

  public Collection<ACBTeam> getTeams() {
    return teams.values();
  }

  public void savePrediction(String playerName, float predictedEFF) {
    playerPredictions.put(playerName, predictedEFF);
  }

  public List<PlayerGame> getGamesForPlayer(String name) {
    if (players.containsKey(name)) {
      return players.get(name).getPlayerGames();
    }
    return new ArrayList<>();
  }
}
