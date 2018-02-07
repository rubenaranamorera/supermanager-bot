package model;

import java.util.List;

public class Player implements Comparable<Player>{

  private final PositionEnum position;
  private final String id;
  private final long price;
  private final String name;
  private final Float predictedEff;
  private final String teamName;
  private final String teamAgainst;
  private final boolean home;
  private final boolean injured;
  private final List<PlayerGame> playerGames;
  private final PlayerNationalityEnum nationality;


  private Player(Builder builder) {
    position = builder.position;
    id = builder.id;
    price = builder.price;
    name = builder.name;
    predictedEff = builder.predictedEff;
    teamName = builder.teamName;
    teamAgainst = builder.teamAgainst;
    home = builder.home;
    injured = builder.injured;
    playerGames = builder.playerGames;
    nationality = builder.nationality;
  }

  public PositionEnum getPosition() {
    return position;
  }

  public String getId() {
    return id;
  }

  public long getPrice() {
    return price;
  }

  public String getName() {
    return name;
  }

  public Float getPredictedEff() {
    return predictedEff;
  }

  public String getTeamName() {
    return teamName;
  }

  public String getTeamAgainst() {
    return teamAgainst;
  }

  public boolean isHome() {
    return home;
  }

  public boolean isInjured() {
    return injured;
  }

  public List<PlayerGame> getPlayerGames() {
    return playerGames;
  }

  public PlayerNationalityEnum getNationality() {
    return nationality;
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


  public static Builder newPlayer() {
    return new Builder();
  }

  public static Builder newPlayer(Player copy) {
    Builder builder = new Builder();
    builder.position = copy.position;
    builder.id = copy.id;
    builder.price = copy.price;
    builder.name = copy.name;
    builder.predictedEff = copy.predictedEff;
    builder.teamName = copy.teamName;
    builder.teamAgainst = copy.teamAgainst;
    builder.home = copy.home;
    builder.injured = copy.injured;
    builder.playerGames = copy.playerGames;
    builder.nationality = copy.nationality;
    return builder;
  }

  @Override
  public int compareTo(Player player) {

    float effForEuro = this.predictedEff / this.getPrice();
    float effForEuroOther = player.getPredictedEff() / player.getPrice();

    if (effForEuroOther - effForEuro > 0) {
      return 1;
    } else if (effForEuroOther - effForEuro < 0) {
      return -1;
    } else {
      return 0;
    }
  }

  public static final class Builder {

    private PositionEnum position;
    private String id;
    private long price;
    private String name;
    private Float predictedEff;
    private String teamName;
    private String teamAgainst;
    private boolean home;
    private boolean injured;
    private List<PlayerGame> playerGames;
    private PlayerNationalityEnum nationality;

    private Builder() {
    }

    public Builder withPosition(PositionEnum val) {
      position = val;
      return this;
    }

    public Builder withId(String val) {
      id = val;
      return this;
    }

    public Builder withPrice(long val) {
      price = val;
      return this;
    }

    public Builder withName(String val) {
      name = val;
      return this;
    }

    public Builder withPredictedEff(Float val) {
      predictedEff = val;
      return this;
    }

    public Builder withTeamName(String val) {
      teamName = val;
      return this;
    }

    public Builder withTeamAgainst(String val) {
      teamAgainst = val;
      return this;
    }

    public Builder withHome(boolean val) {
      home = val;
      return this;
    }

    public Builder withInjured(boolean val) {
      injured = val;
      return this;
    }

    public Builder withPlayerGames(List<PlayerGame> val) {
      playerGames = val;
      return this;
    }

    public Builder withNationality(PlayerNationalityEnum val) {
      nationality = val;
      return this;
    }

    public Player build() {
      return new Player(this);
    }
  }

  @Override
  public String toString() {
    return name + " (" + predictedEff + ") [" + this.predictedEff * 70000/ this.getPrice() + "] '"+ price + "'";
  }
}
