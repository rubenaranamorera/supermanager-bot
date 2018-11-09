import model.ACBTeam;
import model.Player;
import model.PlayerGame;

import java.util.ArrayList;
import java.util.List;

public class Predictor {

  private InMemoryDB inMemoryDB;

  private float GLOBAL_EFF = 81.75f;

  private float GLOBAL_EFF_HOME = 87.93f;

  private float GLOBAL_EFF_AWAY = 75.57f;


  public Predictor(InMemoryDB inMemoryDB) {
    this.inMemoryDB = inMemoryDB;
  }


  public List<Player> createPredictions(List<Player> players) {

    List<Player> playerPredictions = new ArrayList();

    initializeGlobals();

    for (Player player : players) {
      List<PlayerGame> games = inMemoryDB.getGamesForPlayer(player.getName());
      if (games.size() == 0) {
        System.out.println(player.getName() + " with NO GAMES ");
      }
      try {
        Player completePlayer = Player.newPlayer(player).withPlayerGames(games).build();
        playerPredictions.add(predict(completePlayer));

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return playerPredictions;
  }

  private void initializeGlobals() {
    float games = 0;
    float homeGames = 0;
    float awayGames = 0;
    float eff = 0;
    float homeEff = 0;
    float awayEff = 0;
    for (ACBTeam team : inMemoryDB.getTeams()) {
      //System.out.println(team);
      eff = eff + team.getEfficiency();
      homeEff = homeEff + team.getHomeEfficiency();
      awayEff = awayEff + team.getAwayEfficiency();
      games = games + team.getGamesPlayed();
      homeGames = homeGames + team.getGamesPlayedHome();
      awayGames = awayGames + team.getGamesPlayedAway();
    }

    if (games > 0) {
      GLOBAL_EFF = eff / games;
      GLOBAL_EFF_HOME = homeEff / homeGames;
      GLOBAL_EFF_AWAY = awayEff / awayGames;
    }
  }


  private Player predict(Player player) {
    ACBTeam playerACBTeam = inMemoryDB.getACBTeam(player.getTeamName());
    ACBTeam oppositeACBTeam = inMemoryDB.getACBTeam(player.getTeamAgainst());

    boolean isAtHome = player.isHome();

    float eff_factor = calculateEffFactor(player, isAtHome);
    float opp_team_factor = calculateOppositeTeamFactor(oppositeACBTeam, isAtHome);
    float home_away_factor = calculateHomeAwayFactor(isAtHome);
    float win_percentage_factor = calculateWinPercentage(playerACBTeam, oppositeACBTeam, isAtHome);

    System.out.println("-----------------------------");
    System.out.println(player.getTeamName());
    System.out.println(player.getTeamAgainst());

    //TODO:Standard desviation to be applied later
    float predictedEFF = eff_factor * opp_team_factor * home_away_factor * (1 + (0.2f * win_percentage_factor));

    if (predictedEFF == 0) {
      predictedEFF = (float) player.getPrice() / (float) 70000;
    }

    System.out.println("eff_factor: " + eff_factor);
    System.out.println("opp_team_factor: " + opp_team_factor);
    System.out.println("home_away_factor: " + home_away_factor);
    System.out.println("win_percentage_factor: " + win_percentage_factor);
    System.out.println(player.getName() + "\n" + predictedEFF + "\n");
    System.out.println("games: " + player.getGamesPlayed());

    inMemoryDB.savePrediction(player.getName(), predictedEFF);

    return Player.newPlayer(player).withPredictedEff(predictedEFF).build();
  }

  private float calculateWinPercentage(ACBTeam playerACBTeam, ACBTeam oppositeACBTeam, boolean isAtHome) {
    if (oppositeACBTeam == null || oppositeACBTeam.getGamesPlayed() == 0
        || playerACBTeam == null || playerACBTeam.getGamesPlayed() == 0
        || playerACBTeam.getWins() == 0 && oppositeACBTeam.getWins() == 0) {
      return 0.5f;
    }
    float playerTeamWinPercentage = (float) playerACBTeam.getWins() / (float) playerACBTeam.getGamesPlayed();
    float oppositeTeamWinPercentage = (float) oppositeACBTeam.getWins() / (float) oppositeACBTeam.getGamesPlayed();

    float globalPercentage = playerTeamWinPercentage / (playerTeamWinPercentage + oppositeTeamWinPercentage);

    globalPercentage = Math.min(globalPercentage, 0.60f);
    globalPercentage = Math.max(globalPercentage, 0.40f);

    if (isAtHome) {
      if (playerACBTeam.getGamesPlayedHome() == 0 || oppositeACBTeam.getGamesPlayedAway() == 0 ||
          playerACBTeam.getHomeWins() == 0 && oppositeACBTeam.getAwayWins() == 0) {
        return globalPercentage;
      }

      float playerTeamWinPercentatgeHome =
          (float) playerACBTeam.getHomeWins() / (float) playerACBTeam.getGamesPlayedHome();
      float oppositeTeamWinPercentageAway =
          (float) oppositeACBTeam.getAwayWins() / (float) oppositeACBTeam.getGamesPlayedAway();
      float localPercentatge =
          playerTeamWinPercentatgeHome / (playerTeamWinPercentatgeHome + oppositeTeamWinPercentageAway);
      return (globalPercentage + localPercentatge) / 2;
    } else {
      if (playerACBTeam.getGamesPlayedAway() == 0 || oppositeACBTeam.getGamesPlayedHome() == 0
          || playerACBTeam.getAwayWins() == 0 && oppositeACBTeam.getHomeWins() == 0) {
        return globalPercentage;
      }
      float playerTeamWinPercentatgeAway =
          (float) playerACBTeam.getAwayWins() / (float) playerACBTeam.getGamesPlayedAway();
      float oppositeTeamWinPercentageHome =
          (float) oppositeACBTeam.getHomeWins() / (float) oppositeACBTeam.getGamesPlayedHome();

      float localPercentatge =
          playerTeamWinPercentatgeAway / (playerTeamWinPercentatgeAway + oppositeTeamWinPercentageHome);
      return (globalPercentage + localPercentatge) / 2;
    }
  }

  private float calculateHomeAwayFactor(boolean isAtHome) {
    if (isAtHome) {
      return GLOBAL_EFF_HOME / GLOBAL_EFF;
    }
    return GLOBAL_EFF_AWAY / GLOBAL_EFF;
  }

  private float calculateOppositeTeamFactor(ACBTeam oppositeACBTeam, boolean isAtHome) {
    //TODO add RECEIVED EFFICIENCY BY POSITION
    if (oppositeACBTeam == null || oppositeACBTeam.getGamesPlayed() == 0) {
      return 1;
    }


    float receivedEff = (float) oppositeACBTeam.getEfficiencyReceived() / (float) oppositeACBTeam.getGamesPlayed();

    if (oppositeACBTeam.getGamesPlayed() < 3) {
      float returnvalue = receivedEff / GLOBAL_EFF;
      returnvalue = Math.min(returnvalue, 1.1f);
      returnvalue = Math.max(returnvalue, 0.9f);
      return returnvalue;
    }

    if (!isAtHome) {
      if (oppositeACBTeam.getGamesPlayedHome() == 0) {
        return receivedEff / GLOBAL_EFF;
      }
      float homeReceivedEff =
          (float) oppositeACBTeam.getHomeEfficiencyReceived() / (float) oppositeACBTeam.getGamesPlayedHome();
      return ((receivedEff / GLOBAL_EFF) + (homeReceivedEff / GLOBAL_EFF_AWAY)) / 2;
    } else {
      if (oppositeACBTeam.getGamesPlayedAway() == 0) {
        return receivedEff / GLOBAL_EFF;
      }
      float awayReceivedEff =
          (float) oppositeACBTeam.getAwayEfficiencyReceived() / (float) oppositeACBTeam.getGamesPlayedAway();
      return ((receivedEff / GLOBAL_EFF) + (awayReceivedEff / GLOBAL_EFF_HOME)) / 2;
    }
  }

  private float calculateEffFactor(Player player, boolean isAtHome) {
    if (player.isInjured()) {
      return -999;
    }

    if (player.getGamesPlayed() == 0) {
      return 0;
      //return (float) player.getPrice() / 70000;
    }

    float multiplier = 1;
    float minsPerGame = player.getSecondsPlayed() / (60 * player.getGamesPlayed());
    if (minsPerGame < 12) {
      multiplier = 0.8f;
    } else if (minsPerGame < 14) {
      multiplier = 0.9f;
    }

    float eff = player.getEfficiency() / player.getGamesPlayed();
    if (player.getGamesPlayed() < 3) {
      return eff * multiplier;
    }
    float effLast3 = player.getEfficiencyLast3Games() / 3;
    //TODO add HOME/AWAY efficiency if gamesPlayed >= 6 aprox
    return (float) (multiplier * (0.7 * eff + 0.3 * effLast3));
  }

}
