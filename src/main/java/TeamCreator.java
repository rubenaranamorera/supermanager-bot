import model.Player;
import model.PlayerNationalityEnum;
import model.PositionEnum;
import model.SupermanagerTeam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TeamCreator {

  private static final int MIN_EFFICIENCY = 10;
  private static final int BEST_GUARDS_NUMBER = 9;
  private static final int BEST_FORWARDS_NUMBER = 10;
  private static final int BEST_CENTERS_NUMBER = 10;
  private static final int EXTRA_PLAYERS_MARGIN = 0;
  private static final int ALWAYS_IN_EFF_THRESHOLD = 15;

  private final TeamNameGenerator teamNameGenerator;
  private final SupermanagerUIService supermanagerUIService;
  private final PlayerEffComparator playerEffComparator;

  private static final int ITERATIONS_PER_TEAM = 1000;

  private List<Player> bestGuards = new ArrayList<>();
  private List<Player> bestForwards = new ArrayList<>();
  private List<Player> bestCenters = new ArrayList<>();

  private int TEAMS_TO_BE_CREATED = 800;
  private int CHANGES_TO_BE_DONE = 3;

  private List<Long> teamIds = new ArrayList<>();


  public TeamCreator(TeamNameGenerator teamNameGenerator, SupermanagerUIService supermanagerUIService, PlayerEffComparator playerEffComparator) {
    this.teamNameGenerator = teamNameGenerator;
    this.supermanagerUIService = supermanagerUIService;
    this.playerEffComparator = playerEffComparator;
  }


  private static HashMap<String, Float> PLAYERS_TO_BUY = new HashMap<>();

  private List<Player> playerList = new ArrayList<>();


  public static final float OFTEN = 1.1f;
  public static final float SOME = 0.8f;
  public static final float FEW = 0.6f;
  public static final float NEVER = 0f;


  static {

    PLAYERS_TO_BUY.put("LAPROVITTOLA, NICO", SOME);
    PLAYERS_TO_BUY.put("VILDOZA, LUCA", SOME);
    PLAYERS_TO_BUY.put("BEIRAN, JAVIER", FEW);

  /*PLAYERS_TO_BUY.put("VAN LACKE, FEDE", SOME);*/

  }

  public void initializePlayersLists(List<Player> playerPredictions) {

    //Manual efficiency factor
    for (Player player : playerPredictions) {
      System.out.println(player);
      if (PLAYERS_TO_BUY.containsKey(player.getName())) {
        float eff = player.getPredictedEff();
        float multiplier = PLAYERS_TO_BUY.get(player.getName());
        playerList.add(Player.newPlayer(player).withPredictedEff(eff * multiplier).build());
      } else {
        playerList.add(player);
      }
    }

    Collections.sort(playerList);

    int spanishGuards = 0;
    int spanishForwards = 0;
    int spanishCenters = 0;

    for (Player player : playerList) {
      if (player.getPredictedEff() < MIN_EFFICIENCY) {
        continue;
      }

      if (player.getPosition() == PositionEnum.GUARD) {
        if (bestGuards.size() < BEST_GUARDS_NUMBER
            || (spanishGuards < 2 && player.getNationality() == PlayerNationalityEnum.SPANISH)) {
          bestGuards.add(player);
          if (player.getNationality() == PlayerNationalityEnum.SPANISH) {
            spanishGuards++;
          }
        }
      } else if (player.getPosition() == PositionEnum.FORWARD) {
        if (bestForwards.size() < BEST_FORWARDS_NUMBER
            || (spanishForwards < 2 && player.getNationality() == PlayerNationalityEnum.SPANISH)) {
          bestForwards.add(player);
          if (player.getNationality() == PlayerNationalityEnum.SPANISH) {
            spanishForwards++;
          }
        }
      } else {
        if (bestCenters.size() < BEST_CENTERS_NUMBER
            || (spanishCenters < 2 && player.getNationality() == PlayerNationalityEnum.SPANISH)) {
          bestCenters.add(player);
          if (player.getNationality() == PlayerNationalityEnum.SPANISH) {
            spanishCenters++;
          }
        }
      }
    }

    for (Player player : playerList) {

      if (player.getPredictedEff() > ALWAYS_IN_EFF_THRESHOLD)

        if (player.getPosition() == PositionEnum.GUARD) {
          if (!bestGuards.contains(player)) {
            bestGuards.add(player);
          }
        } else if (player.getPosition() == PositionEnum.FORWARD) {
          if (!bestForwards.contains(player)) {
            bestForwards.add(player);
          }
        } else {
          if (!bestCenters.contains(player)) {
            bestCenters.add(player);
          }
        }
    }


    System.out.println("---------------------------------");
    for (Player p : bestGuards) {
      System.out.println(p);
    }
    System.out.println("---------------------------------");
    for (Player p : bestForwards) {
      System.out.println(p);
    }
    System.out.println("---------------------------------");
    for (Player p : bestCenters) {
      System.out.println(p);
    }

  }

  public void createTeams() {
    int createdTeams = 0;
    while (createdTeams < TEAMS_TO_BE_CREATED) {
      SupermanagerTeam supermanagerTeam = generateTeam(teamNameGenerator.generateName());
      if (supermanagerTeam.isValid()) {
        supermanagerUIService.createTeam(supermanagerTeam);
        createdTeams++;
      }
    }

    System.out.println("ALL " + TEAMS_TO_BE_CREATED + " teams created");
  }

  private SupermanagerTeam generateTeam(String name) {
    return SupermanagerTeam.createTeam()
        .withName(name)
        .withMoney(6500000)
        .withGuards(getRandomGuards())
        .withForwards(getRandomForwards())
        .withCenters(getRandomCenters())
        .build();
  }


  private List<Player> getRandomGuards() {
    List<Player> guards = new ArrayList<>();
    ArrayList boughtGuards = new ArrayList();


    for (Player player : bestGuards) {
      float eff = player.getPredictedEff();
      float multiplier = (float) Math.random() * 0.6f + 0.7f;
      guards.add(Player.newPlayer(player).withPredictedEff(eff * multiplier).build());
    }

    Collections.sort(guards);
    List<Player> finalGuards = guards.subList(0, 12);

    Player guard1 = RandomPicker.pickOnePlayer(finalGuards);
    finalGuards.remove(guard1);
    Player guard2 = RandomPicker.pickOnePlayer(finalGuards);
    finalGuards.remove(guard2);
    Player guard3 = RandomPicker.pickOnePlayer(finalGuards);

    boughtGuards.add(guard1);
    boughtGuards.add(guard2);
    boughtGuards.add(guard3);

    return boughtGuards;
  }

  private List<Player> getRandomForwards() {
    List<Player> forwards = new ArrayList<>();
    List<Player> boughtForwards = new ArrayList();

    for (Player player : bestForwards) {
      float eff = player.getPredictedEff();
      float multiplier = (float) Math.random() * 0.6f + 0.7f;
      forwards.add(Player.newPlayer(player).withPredictedEff(eff * multiplier).build());
    }

    Collections.sort(forwards);
    List<Player> finalForwards = forwards.subList(0, 18);

    Player forward1 = RandomPicker.pickOnePlayer(finalForwards);
    finalForwards.remove(forward1);
    Player forward2 = RandomPicker.pickOnePlayer(finalForwards);
    finalForwards.remove(forward2);
    Player forward3 = RandomPicker.pickOnePlayer(finalForwards);
    finalForwards.remove(forward3);
    Player forward4 = RandomPicker.pickOnePlayer(finalForwards);

    boughtForwards.add(forward1);
    boughtForwards.add(forward2);
    boughtForwards.add(forward3);
    boughtForwards.add(forward4);

    return boughtForwards;
  }

  private List<Player> getRandomCenters() {
    List<Player> centers = new ArrayList<>();
    ArrayList boughtCenters = new ArrayList();

    for (Player player : bestCenters) {
      float eff = player.getPredictedEff();
      float multiplier = (float) Math.random() * 0.6f + 0.7f;
      centers.add(Player.newPlayer(player).withPredictedEff(eff * multiplier).build());
    }

    Collections.sort(centers);
    List<Player> finalCenters = centers.subList(0, 18);

    Player center1 = RandomPicker.pickOnePlayer(finalCenters);
    finalCenters.remove(center1);
    Player center2 = RandomPicker.pickOnePlayer(finalCenters);
    finalCenters.remove(center2);
    Player center3 = RandomPicker.pickOnePlayer(finalCenters);
    finalCenters.remove(center3);
    Player center4 = RandomPicker.pickOnePlayer(finalCenters);

    boughtCenters.add(center1);
    boughtCenters.add(center2);
    boughtCenters.add(center3);
    boughtCenters.add(center4);

    return boughtCenters;
  }

  private void initializeTeamIds() {
    try {
      File input = new File("/Users/ruben.arana/git/SuperManagerBot/src/main/resources/teams.html");
      Document doc = Jsoup.parse(input, "UTF-8");

      Elements teamElements = doc.getElementsByClass("equipo");

      for (Element teamNode : teamElements) {
        try {
          String uri = teamNode.getElementsByTag("a").attr("href");
          String id = uri.substring(uri.lastIndexOf("/") + 1);

          //if (teamNode.parent().getElementsByAttribute("src").size() > 0 ) {
          teamIds.add(Long.parseLong(id));
          //}
        } catch (Exception e) {
          //
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SupermanagerTeam generateTeamWithChanges(SupermanagerTeam team) {

    float actualExpectedEff = team.calculateActualExpectedEfficiency();
    System.out.println("--------------------------------");
    System.out.println("PREVIOUS: " + actualExpectedEff);

    SupermanagerTeam bestCombination = null;


    for (int i = 0; i < ITERATIONS_PER_TEAM; i++) {

      SupermanagerTeam teamWithRemovedPlayers = removePlayersWithLessExpectedEfficiency(team);

      //buscar la millor combinació per pressupost
      SupermanagerTeam tempTeam = fillTeam(teamWithRemovedPlayers);

      if (tempTeam.isValid()) {
        float eff = tempTeam.calculateActualExpectedEfficiency();
        if (eff > actualExpectedEff) {
          bestCombination = SupermanagerTeam.createTeam(tempTeam).build();
          actualExpectedEff = eff;
        }
      }
    }

    System.out.println("AFTER: " + actualExpectedEff);


    return bestCombination;
  }

  private SupermanagerTeam fillTeam(SupermanagerTeam teamWithRemovedPlayers) {

    List<Player> guardsList = new ArrayList<>();
    List<Player> forwardsList = new ArrayList<>();
    List<Player> centersList = new ArrayList<>();

    for (Player p : teamWithRemovedPlayers.getGuards()) {
      guardsList.add(p);
    }
    for (Player p : teamWithRemovedPlayers.getForwards()) {
      forwardsList.add(p);
    }
    for (Player p : teamWithRemovedPlayers.getCenters()) {
      centersList.add(p);
    }

    for (int num = guardsList.size(); num < 3; num++) {
      Player player = RandomPicker.pickOnePlayer(bestGuards);
      if (!guardsList.contains(player)) {
        guardsList.add(player);
      } else {
        num--;
      }
    }

    for (int num = forwardsList.size(); num < 4; num++) {
      Player player = RandomPicker.pickOnePlayer(bestForwards);
      if (!forwardsList.contains(player)) {
        forwardsList.add(player);
      } else {
        num--;
      }
    }

    for (int num = centersList.size(); num < 4; num++) {
      Player player = RandomPicker.pickOnePlayer(bestCenters);
      if (!centersList.contains(player)) {
        centersList.add(player);
      } else {
        num--;
      }
    }

    return SupermanagerTeam.createTeam()
        .withId(teamWithRemovedPlayers.getId())
        .withMoney(teamWithRemovedPlayers.getMoney())
        .withGuards(guardsList)
        .withForwards(forwardsList)
        .withCenters(centersList)
        .build();
  }

  private SupermanagerTeam removePlayersWithLessExpectedEfficiency(SupermanagerTeam team) {
    List<Player> teamPlayers = team.getAllTeamPlayers();
    Collections.sort(teamPlayers);

    List<Player> playersToAdd = new ArrayList<>();
    List<Player> playersToAddFinal = new ArrayList<>();

    List<Player> playersToRemove = new ArrayList<>();

    if (teamPlayers.size() - CHANGES_TO_BE_DONE - EXTRA_PLAYERS_MARGIN > 0) {
      for (Player player : teamPlayers.subList(0, teamPlayers.size() - CHANGES_TO_BE_DONE - EXTRA_PLAYERS_MARGIN)) {
        playersToAdd.add(player);
      }
      for (Player player : teamPlayers.subList(teamPlayers.size() - CHANGES_TO_BE_DONE - EXTRA_PLAYERS_MARGIN, teamPlayers.size())) {
        playersToRemove.add(player);
      }


      Collections.sort(playersToAdd, playerEffComparator);
      for (Player player : playersToAdd.subList(0, playersToAdd.size() - EXTRA_PLAYERS_MARGIN)) {
        playersToAddFinal.add(player);
      }
      for (Player player : playersToAdd.subList(playersToAdd.size() - EXTRA_PLAYERS_MARGIN, playersToAdd.size())) {
        playersToRemove.add(player);
      }

      for (int i = 0; i < EXTRA_PLAYERS_MARGIN * 2; i++) {
        Player p = RandomPicker.pickOnePlayer(playersToRemove);
        playersToRemove.remove(p);
        playersToAddFinal.add(p);
      }

    }

    List<Player> guardsList = new ArrayList<>();
    List<Player> forwardsList = new ArrayList<>();
    List<Player> centersList = new ArrayList<>();

    for (Player player : playersToAddFinal) {
      if (player.getPosition() == PositionEnum.GUARD) {
        guardsList.add(player);
      }
      if (player.getPosition() == PositionEnum.FORWARD) {
        forwardsList.add(player);
      }
      if (player.getPosition() == PositionEnum.CENTER) {
        centersList.add(player);
      }
    }

    return SupermanagerTeam.createTeam()
        .withMoney(team.getMoney())
        .withId(team.getId())
        .withGuards(guardsList)
        .withForwards(forwardsList)
        .withCenters(centersList)
        .build();
  }


  public void updateTeams() {
    initializeTeamIds();

    int updatedTeams = 0;
    boolean start = false;
    for (Long teamId : teamIds) {
      if (start) {
        //supermanagerUIService.resetTeam(teamId);
        //supermanagerUIService.removeAllSustituir(teamId);
        long preTime = System.currentTimeMillis();

        SupermanagerTeam initialSupermanagerTeam = initializeTeam(teamId);
        if (initialSupermanagerTeam != null) {

          SupermanagerTeam newSupermanagerTeam = generateTeamWithChanges(initialSupermanagerTeam);

          if (newSupermanagerTeam == null) {
            System.out.println("Unable to change team: " + initialSupermanagerTeam.getId());
          } else {
            supermanagerUIService.updateTeam(initialSupermanagerTeam, newSupermanagerTeam);
            updatedTeams++;
            System.out.println(updatedTeams + "/" + teamIds.size() + ": Updated team: " + initialSupermanagerTeam.getId() + " (" + (System.currentTimeMillis() - preTime) + ")");
          }
        }
      } else {
        updatedTeams++;
        System.out.println("model.SupermanagerTeam " + updatedTeams);

        if (teamId == 1044779) {
          start = true;
        }
      }
    }
  }

  public SupermanagerTeam initializeTeam(Long teamId) {
    try {
      if (supermanagerUIService.changesAlreadyDone(teamId)) {
        return null;
      }
      long totalMoney = supermanagerUIService.getTeamMoney(teamId);

      List<Player> players = findPlayersByName(supermanagerUIService.getTeamPlayerNames(teamId));

      return SupermanagerTeam.createTeam()
          .withId(teamId)
          .withMoney(totalMoney)
          .withGuards(players.stream().filter(player -> player.getPosition().equals(PositionEnum.GUARD)).collect(toList()))
          .withForwards(players.stream().filter(player -> player.getPosition().equals(PositionEnum.FORWARD)).collect(toList()))
          .withCenters(players.stream().filter(player -> player.getPosition().equals(PositionEnum.CENTER)).collect(toList()))
          .build();
    } catch (Exception e) {
      return null;
    }
  }

  public List<Player> findPlayersByName(List<String> playerNames) {
    List<Player> players = new ArrayList<>();
    for (Player player : playerList) {
      if (playerNames.contains(player.getName())) {
        players.add(player);
      }
    }
    return players;
  }

}
