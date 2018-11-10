package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class SupermanagerTeam {

  private long id;

  private String name;

  private Map<Integer, Player> playersPositions;

  private long money;

  private Float expectedEfficiency = null;

  private SupermanagerTeam(Builder builder) {
    id = builder.id;
    name = builder.name;
    money = builder.money;
    playersPositions = builder.playerPositions;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Player> getGuards() {
    return playersPositions.keySet().stream().filter(key -> key < 4).map(playersPositions::get).collect(toList());
  }

  public List<Player> getForwards() {
    return playersPositions.keySet().stream().filter(key -> key >= 4 && key <= 7).map(playersPositions::get).collect(toList());
  }

  public List<Player> getCenters() {
    return playersPositions.keySet().stream().filter(key -> key > 7).map(playersPositions::get).collect(toList());
  }


  public Map<Integer, Player> getPlayersPositions() {
    return playersPositions;
  }

  public long getMoney() {
    return money;
  }

  public String getKey() {
    return playersPositions.values()
        .stream()
        .map(player -> player.getId())
        .sorted()
        .collect(joining("#"));
  }

  public static Builder supermanagerTeam() {
    return new Builder();
  }

  public static Builder supermanagerTeam(SupermanagerTeam copy) {
    Builder builder = new Builder();
    builder.id = copy.id;
    builder.name = copy.name;
    builder.playerPositions = copy.playersPositions;
    builder.money = copy.money;
    return builder;
  }


  private long getTotalPrice() {
    return playersPositions.values()
        .stream()
        .map(Player::getPrice)
        .mapToLong(i -> i)
        .sum();
  }

  public boolean isValid() {
    long difference = money - getTotalPrice();
    return difference > 0
        //&& difference < 500000
        && getGuards().size() == 3
        && getForwards().size() == 4
        && getCenters().size() == 4
        && getPlayersFrom(PlayerNationalityEnum.SPANISH) >= 4
        && getPlayersFrom(PlayerNationalityEnum.AMERICAN) <= 2;
  }

  private long getPlayersFrom(PlayerNationalityEnum nationality) {
    return playersPositions
        .values()
        .stream()
        .filter(player -> player.getNationality().equals(nationality))
        .count();
  }

  @Override
  public String toString() {
    return "SupermanagerTeam{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", players=" + playersPositions.values().stream().map(Player::getName).collect(joining(" -- ")) +
        ", money=" + money +
        ", totalPrice=" + getTotalPrice() +
        '}';
  }


  public float getExpectedEfficiency() {
    if (isNull(expectedEfficiency)) {
      expectedEfficiency = playersPositions
          .values()
          .stream()
          .map(Player::getPredictedEff)
          .reduce(0.0f, Float::sum);
    }
    return expectedEfficiency;
  }

  public List<Player> getAllTeamPlayers() {
    return new ArrayList<>(playersPositions.values());
  }

  public static final class Builder {

    private long id;

    private String name;

    private Map<Integer, Player> playerPositions;

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

    public Builder withPlayerPositions(Map<Integer, Player> val) {
      playerPositions = val;
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
