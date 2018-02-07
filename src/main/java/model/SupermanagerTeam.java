package model;

import java.util.ArrayList;
import java.util.List;

public class SupermanagerTeam {

  private long id;
  private String name;
  private List<Player> guards = new ArrayList<>();
  private List<Player> forwards = new ArrayList<>();
  private List<Player> centers = new ArrayList<>();
  private long money;

  private SupermanagerTeam(Builder builder) {
    id = builder.id;
    name = builder.name;
    guards = builder.guards;
    forwards = builder.forwards;
    centers = builder.centers;
    money = builder.money;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Player> getGuards() {
    return guards;
  }

  public List<Player> getForwards() {
    return forwards;
  }

  public List<Player> getCenters() {
    return centers;
  }

  public long getMoney() {
    return money;
  }

  public static Builder createTeam() {
    return new Builder();
  }

  public static Builder createTeam(SupermanagerTeam copy) {
    Builder builder = new Builder();
    builder.id = copy.id;
    builder.name = copy.name;
    builder.guards = copy.guards;
    builder.forwards = copy.forwards;
    builder.centers = copy.centers;
    builder.money = copy.money;
    return builder;
  }


  public long getTotalPrice () {
    long totalPrice = 0;
    List<Player> players = new ArrayList<>();
    players.addAll(guards);
    players.addAll(forwards);
    players.addAll(centers);
    for (Player player: players) {
      totalPrice = totalPrice + player.getPrice();
    }
    return totalPrice;
  }

  public boolean isValid() {
    long difference = money - getTotalPrice();
    return /*difference > 0 && difference < 6000000
        && */guards.size() == 3
        && forwards.size() == 4
        && centers.size() == 4
        && getPlayersFrom(PlayerNationalityEnum.SPANISH) >= 4
        && getPlayersFrom(PlayerNationalityEnum.AMERICAN) <= 2;
  }

  private int getPlayersFrom(PlayerNationalityEnum nationality) {
    int fromNationality = 0;

    for (Player guard: guards) {
      if (guard.getNationality() == nationality) {
        fromNationality++;
      }
    }

    for (Player forward: forwards) {
      if (forward.getNationality() == nationality) {
        fromNationality++;
      }
    }

    for (Player center: centers) {
      if (center.getNationality() == nationality) {
        fromNationality++;
      }
    }

    return fromNationality;
  }

  @Override
  public String toString() {
    return "SupermanagerTeam{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", guards=" + guards +
        ", forwards=" + forwards +
        ", centers=" + centers +
        ", money=" + money +
        ", totalPrice=" + getTotalPrice() +
        '}';
  }


  public float calculateActualExpectedEfficiency() {

    float expectedEff = 0;

    for (Player guard: guards) {
      expectedEff = expectedEff + guard.getPredictedEff();
    }

    for (Player forward: forwards) {
      expectedEff = expectedEff + forward.getPredictedEff();
    }

    for (Player center: centers) {
      expectedEff = expectedEff + center.getPredictedEff();
    }

    return expectedEff;
  }

  public List<Player> getAllTeamPlayers() {
    List<Player> playerList = new ArrayList<>();
    for (Player guard: guards) {
      playerList.add(guard);
    }
    for (Player forward: forwards) {
      playerList.add(forward);
    }
    for (Player center: centers) {
      playerList.add(center);
    }
    return playerList;
  }

  public static final class Builder {

    private long id;
    private String name;
    private List<Player> guards;
    private List<Player> forwards;
    private List<Player> centers;
    private long money;

    private Builder() {
    }

    public Builder withId(long val) {
      id = val;
      return this;
    }

    public Builder withName(String val) {
      name = val;
      return this;
    }

    public Builder withGuards(List<Player> val) {
      guards = val;
      return this;
    }

    public Builder withForwards(List<Player> val) {
      forwards = val;
      return this;
    }

    public Builder withCenters(List<Player> val) {
      centers = val;
      return this;
    }

    public Builder withMoney(long val) {
      money = val;
      return this;
    }

    public SupermanagerTeam build() {
      return new SupermanagerTeam(this);
    }
  }
}
