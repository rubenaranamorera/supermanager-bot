package model;

import java.util.List;

public class ACBPlayer {

  private final String name;
  private final String teamName;
  private final List<PlayerGame> playerGames;

  private ACBPlayer(Builder builder) {
    name = builder.name;
    teamName = builder.teamName;
    playerGames = builder.playerGames;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(ACBPlayer copy) {
    Builder builder = new Builder();
    builder.name = copy.name;
    builder.teamName = copy.teamName;
    builder.playerGames = copy.playerGames;
    return builder;
  }

  @Override
  public String toString() {
    return "ACBPlayer{" +
        "name='" + name + '\'' +
        ", teamName=" + teamName +
        ", points=" + getPoints() +
        ", threes=" + getThrees() +
        ", assists=" + getAssists() +
        ", rebounds=" + getRebounds() +
        ", efficiency=" + getEfficiency() +
        ", efficiencyLast3Games=" + getEfficiencyLast3Games() +
        ", games=" + getGamesPlayed() +
        '}';
  }

  public String getName() {
    return name;
  }

  public String getTeamName() {
    return teamName;
  }

  public List<PlayerGame> getPlayerGames() {
    return playerGames;
  }

  public int getPoints(){
    int points = 0;
    for (PlayerGame playerGame: playerGames) {
      points = points + playerGame.getPoints();
    }
    return points;
  }

  public int getThrees(){
    int threes = 0;
    for (PlayerGame playerGame: playerGames) {
      threes = threes + playerGame.getThrees();
    }
    return threes;
  }

  public int getRebounds(){
    int rebounds = 0;
    for (PlayerGame playerGame: playerGames) {
      rebounds = rebounds + playerGame.getRebounds();
    }
    return rebounds;
  }

  public int getAssists(){
    int assists = 0;
    for (PlayerGame playerGame: playerGames) {
      assists = assists + playerGame.getAssists();
    }
    return assists;
  }

  public int getEfficiency(){
    int efficiency = 0;
    for (PlayerGame playerGame: playerGames) {
      efficiency = efficiency + playerGame.getEfficiency();
    }
    return efficiency;
  }

  public int getEfficiencyLast3Games(){
    int efficiency = 0;
    int index = playerGames.size() - 1;
    int games = 0;

    while (games < 3 && index >= 0) {
      PlayerGame playerGame = playerGames.get(index);
      if (playerGame.getSecondsPlayed() > 0) {
        efficiency = efficiency + playerGame.getEfficiency();
        games++;
      }
      index--;
    }
    return efficiency;
  }

  public int getGamesPlayed() {
    int games = 0;
    for (PlayerGame playerGame: playerGames) {
      if (playerGame.getSecondsPlayed() > 0){
        games = games + 1;
      }
    }
    return games;
  }

  public int getSecondsPlayed() {
    int seconds = 0;
    for (PlayerGame playerGame: playerGames) {
      if (playerGame.getSecondsPlayed() > 0){
        seconds = seconds + playerGame.getSecondsPlayed();
      }
    }
    return seconds;
  }

  public static final class Builder {

    private String name;
    private String teamName;
    private List<PlayerGame> playerGames;

    private Builder() {
    }

    public Builder withTeamName(String val) {
      teamName = val;
      return this;
    }

    public Builder withName(String val) {
      name = val;
      return this;
    }

    public Builder withPlayerGames(List<PlayerGame> val) {
      playerGames = val;
      return this;
    }

    public ACBPlayer build() {
      return new ACBPlayer(this);
    }
  }
}
