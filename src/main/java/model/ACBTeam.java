package model;

public class ACBTeam {

  private final String name;
  private final int wins;
  private final int loses;

  private final int points;
  private final int threes;
  private final int rebounds;
  private final int assists;
  private final int efficiency;

  private final int pointsReceived;
  private final int threesReceived;
  private final int reboundsReceived;
  private final int assistsReceived;
  private final int efficiencyReceived;

  private final int homeEfficiency;
  private final int awayEfficiency;
  private final int homeEfficiencyReceived;
  private final int awayEfficiencyReceived;

  private final int homeWins;
  private final int homeLoses;
  private final int awayWins;
  private final int awayLoses;

  private ACBTeam(Builder builder) {
    name = builder.name;
    wins = builder.wins;
    loses = builder.loses;
    points = builder.points;
    threes = builder.threes;
    rebounds = builder.rebounds;
    assists = builder.assists;
    efficiency = builder.efficiency;
    pointsReceived = builder.pointsReceived;
    threesReceived = builder.threesReceived;
    reboundsReceived = builder.reboundsReceived;
    assistsReceived = builder.assistsReceived;
    efficiencyReceived = builder.efficiencyReceived;

    homeEfficiency = builder.homeEfficiency;
    awayEfficiency = builder.awayEfficiency;
    homeEfficiencyReceived = builder.homeEfficiencyReceived;
    awayEfficiencyReceived = builder.awayEfficiencyReceived;
    homeWins = builder.homeWins;
    homeLoses = builder.homeLoses;
    awayWins = builder.awayWins;
    awayLoses = builder.awayLoses;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(ACBTeam copy) {
    Builder builder = new Builder();
    builder.name = copy.name;
    builder.wins = copy.wins;
    builder.loses = copy.loses;
    builder.points = copy.points;
    builder.threes = copy.threes;
    builder.rebounds = copy.rebounds;
    builder.assists = copy.assists;
    builder.efficiency = copy.efficiency;
    builder.pointsReceived = copy.pointsReceived;
    builder.threesReceived = copy.threesReceived;
    builder.reboundsReceived = copy.reboundsReceived;
    builder.assistsReceived = copy.assistsReceived;
    builder.efficiencyReceived = copy.efficiencyReceived;
    builder.homeEfficiency = copy.homeEfficiency;
    builder.awayEfficiency = copy.awayEfficiency;
    builder.homeEfficiencyReceived = copy.homeEfficiencyReceived;
    builder.awayEfficiencyReceived = copy.awayEfficiencyReceived;
    builder.homeWins = copy.homeWins;
    builder.homeLoses = copy.homeLoses;
    builder.awayWins = copy.awayWins;
    builder.awayLoses = copy.awayLoses;

    return builder;
  }


  public String getName() {
    return name;
  }

  public int getWins() {
    return wins;
  }

  public int getLoses() {
    return loses;
  }

  public int getPoints() {
    return points;
  }

  public int getThrees() {
    return threes;
  }

  public int getRebounds() {
    return rebounds;
  }

  public int getAssists() {
    return assists;
  }

  public int getEfficiency() {
    return efficiency;
  }

  public int getPointsReceived() {
    return pointsReceived;
  }

  public int getThreesReceived() {
    return threesReceived;
  }

  public int getReboundsReceived() {
    return reboundsReceived;
  }

  public int getAssistsReceived() {
    return assistsReceived;
  }

  public int getEfficiencyReceived() {
    return efficiencyReceived;
  }

  public int getGamesPlayed() {
    return wins + loses;
  }

  public int getAwayEfficiency() {
    return awayEfficiency;
  }

  public int getHomeEfficiency() {
    return homeEfficiency;
  }

  public int getGamesPlayedHome() {
    return homeWins + homeLoses;
  }

  public int getGamesPlayedAway() {
    return awayWins + awayLoses;
  }

  public int getHomeEfficiencyReceived() {
    return homeEfficiencyReceived;
  }

  public int getAwayEfficiencyReceived() {
    return awayEfficiencyReceived;
  }

  public int getHomeWins() {
    return homeWins;
  }

  public int getHomeLoses() {
    return homeLoses;
  }

  public int getAwayWins() {
    return awayWins;
  }

  public int getAwayLoses() {
    return awayLoses;
  }

  public static final class Builder {

    private String name;
    private int wins;
    private int loses;
    private int points;
    private int threes;
    private int rebounds;
    private int assists;
    private int efficiency;
    private int pointsReceived;
    private int threesReceived;
    private int reboundsReceived;
    private int assistsReceived;
    private int efficiencyReceived;
    private int homeEfficiency;
    private int awayEfficiency;
    private int homeEfficiencyReceived;
    private int awayEfficiencyReceived;
    private int homeWins;
    private int homeLoses;
    private int awayWins;
    private int awayLoses;

    private Builder() {
    }

    public Builder withName(String val) {
      name = val;
      return this;
    }

    public Builder withWins(int val) {
      wins = val;
      return this;
    }

    public Builder withLoses(int val) {
      loses = val;
      return this;
    }

    public Builder withPoints(int val) {
      points = val;
      return this;
    }

    public Builder withThrees(int val) {
      threes = val;
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

    public Builder withEfficiency(int val) {
      efficiency = val;
      return this;
    }

    public Builder withPointsReceived(int val) {
      pointsReceived = val;
      return this;
    }

    public Builder withThreesReceived(int val) {
      threesReceived = val;
      return this;
    }

    public Builder withReboundsReceived(int val) {
      reboundsReceived = val;
      return this;
    }

    public Builder withAssistsReceived(int val) {
      assistsReceived = val;
      return this;
    }

    public Builder withEfficiencyReceived(int val) {
      efficiencyReceived = val;
      return this;
    }

    public Builder withHomeEfficiencyReceived(int val) {
      homeEfficiencyReceived = val;
      return this;
    }

    public Builder withAwayEfficiencyReceived(int val) {
      awayEfficiencyReceived = val;
      return this;
    }

    public Builder withHomeEfficiency(int val) {
      homeEfficiency = val;
      return this;
    }

    public Builder withAwayEfficiency(int val) {
      awayEfficiency = val;
      return this;
    }

    public Builder withHomeWins(int val) {
      homeWins = val;
      return this;
    }

    public Builder withHomeLoses(int val) {
      homeLoses = val;
      return this;
    }

    public Builder withAwayWins(int val) {
      awayWins = val;
      return this;
    }

    public Builder withAwayLoses(int val) {
      awayLoses = val;
      return this;
    }

    public ACBTeam build() {
      return new ACBTeam(this);
    }
  }

  @Override
  public String toString() {
    return "ACBTeam{" +
        "name='" + name + '\'' +
        ", wins=" + wins +
        ", loses=" + loses +
        ", points=" + points +
        ", threes=" + threes +
        ", rebounds=" + rebounds +
        ", assists=" + assists +
        ", efficiency=" + efficiency +
        ", pointsReceived=" + pointsReceived +
        ", threesReceived=" + threesReceived +
        ", reboundsReceived=" + reboundsReceived +
        ", assistsReceived=" + assistsReceived +
        ", efficiencyReceived=" + efficiencyReceived +
        '}';
  }
}
