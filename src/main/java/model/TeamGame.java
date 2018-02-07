package model;

import java.util.List;

public class TeamGame {

  private final List<PlayerGame> team1PlayerGames;
  private final List<PlayerGame> team2PlayerGames;
  private final String team1Name;
  private final String team2Name;

  private TeamGame(Builder builder) {
    team1PlayerGames = builder.team1PlayerGames;
    team2PlayerGames = builder.team2PlayerGames;
    team1Name = builder.team1Name;
    team2Name = builder.team2Name;
  }

  public List<PlayerGame> getTeam1PlayerGames() {
    return team1PlayerGames;
  }

  public List<PlayerGame> getTeam2PlayerGames() {
    return team2PlayerGames;
  }

  public String getTeam1Name() {
    return team1Name;
  }

  public String getTeam2Name() {
    return team2Name;
  }

  public String getOppositeTeam(String name) throws RuntimeException {
    if (team1Name.equals(name)) {
      return team2Name;
    }
    if (team2Name.equals(name)) {
      return team1Name;
    }
    throw new RuntimeException("Team: " + name + " does not play this teamgame. \n" + this.toString());
  }

  public static Builder newBuilder() {
    return new Builder();
  }


  @Override
  public String toString() {
    return "TeamGame{" +
        "team1PlayerGames=" + team1PlayerGames +
        ", team2PlayerGames=" + team2PlayerGames +
        ", team1Name='" + team1Name + '\'' +
        ", team2Name='" + team2Name + '\'' +
        '}';
  }

  public static Builder newBuilder(TeamGame copy) {
    Builder builder = new Builder();
    builder.team1PlayerGames = copy.team1PlayerGames;
    builder.team2PlayerGames = copy.team2PlayerGames;
    builder.team1Name = copy.team1Name;
    builder.team2Name = copy.team2Name;
    return builder;
  }


  public static final class Builder {

    private List<PlayerGame> team1PlayerGames;
    private List<PlayerGame> team2PlayerGames;
    private String team1Name;
    private String team2Name;

    public Builder() {
    }

    public Builder withTeam1PlayerGames(List<PlayerGame> val) {
      team1PlayerGames = val;
      return this;
    }

    public Builder withTeam2PlayerGames(List<PlayerGame> val) {
      team2PlayerGames = val;
      return this;
    }

    public Builder withTeam1Name(String val) {
      team1Name = val;
      return this;
    }

    public Builder withTeam2Name(String val) {
      team2Name = val;
      return this;
    }

    public TeamGame build() {
      return new TeamGame(this);
    }
  }
}
