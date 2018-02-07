package model;

public class PlayerGame {

  private final String name;
  private final int secondsPlayed;
  private final int points;
  private final int rebounds;
  private final int assists;
  private final int threes;
  private final int efficiency;
  private final String teamName;
  private final String oppositeTeamName;

  private PlayerGame(Builder builder) {
    name = builder.name;
    secondsPlayed = builder.secondsPlayed;
    points = builder.points;
    rebounds = builder.rebounds;
    assists = builder.assists;
    threes = builder.threes;
    efficiency = builder.efficiency;
    teamName = builder.teamName;
    oppositeTeamName = builder.oppositeTeamName;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(PlayerGame copy) {
    Builder builder = new Builder();
    builder.name = copy.name;
    builder.secondsPlayed = copy.secondsPlayed;
    builder.points = copy.points;
    builder.rebounds = copy.rebounds;
    builder.assists = copy.assists;
    builder.threes = copy.threes;
    builder.efficiency = copy.efficiency;
    builder.teamName = copy.teamName;
    builder.oppositeTeamName = copy.oppositeTeamName;
    return builder;
  }


  public String getName() {
    return name;
  }

  public int getSecondsPlayed() {
    return secondsPlayed;
  }

  public int getPoints() {
    return points;
  }

  public int getRebounds() {
    return rebounds;
  }

  public int getAssists() {
    return assists;
  }

  public int getThrees() {
    return threes;
  }

  public int getEfficiency() {
    return efficiency;
  }

  public String getTeamName() {
    return teamName;
  }

  public String getOppositeTeamName() {
    return oppositeTeamName;
  }

  @Override
  public String toString() {
    return "PlayerGame{" +
        "name='" + name + '\'' +
        ", secondsPlayed=" + secondsPlayed +
        ", points=" + points +
        ", rebounds=" + rebounds +
        ", assists=" + assists +
        ", threes=" + threes +
        ", efficiency=" + efficiency +
        '}';
  }

  public static final class Builder {

    private String name;
    private int secondsPlayed;
    private int points;
    private int rebounds;
    private int assists;
    private int threes;
    private int efficiency;
    private String teamName;
    private String oppositeTeamName;

    public Builder() {
    }

    public Builder withName(String val) {
      name = val;
      return this;
    }

    public Builder withSecondsPlayed(int val) {
      secondsPlayed = val;
      return this;
    }

    public Builder withPoints(int val) {
      points = val;
      return this;
    }

    public Builder withRebounds(int val) {
      rebounds = val;
      return this;
    }

    public Builder withAssists(int val) {
      assists = val;
      return this;
    }

    public Builder withThrees(int val) {
      threes = val;
      return this;
    }

    public Builder withEfficiency(int val) {
      efficiency = val;
      return this;
    }

    public Builder withTeamName(String val) {
      teamName = val;
      return this;
    }

    public Builder withOppositeTeamName(String val) {
      oppositeTeamName = val;
      return this;
    }

    public PlayerGame build() {
      return new PlayerGame(this);
    }
  }
}
