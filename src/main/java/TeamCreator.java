import model.Player;
import model.PositionEnum;
import model.SupermanagerTeam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Streams.concat;
import static java.lang.Math.random;
import static java.util.Collections.reverseOrder;
import static java.util.Collections.shuffle;
import static java.util.Collections.sort;
import static java.util.Map.Entry.comparingByValue;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static model.Player.newPlayer;
import static model.PlayerNationalityEnum.SPANISH;
import static model.PositionEnum.CENTER;
import static model.PositionEnum.FORWARD;
import static model.PositionEnum.GUARD;
import static model.SupermanagerTeam.supermanagerTeam;

public class TeamCreator {

  private static final float MIN_EFFICIENCY = 10;

  private static final int BEST_GUARDS_NUMBER = 10;

  private static final int BEST_FORWARDS_NUMBER = 12;

  private static final int BEST_CENTERS_NUMBER = 10;

  private static final int EXTRA_PLAYERS_TO_BE_REMOVED = 1;

  private static final int ALWAYS_IN_EFF_THRESHOLD = 14;

  private static final Set<String> CREATED_TEAMS = new HashSet<>();

  private static final long MINIMUM_SPANISH_PLAYERS = 2;

  private final TeamNameGenerator teamNameGenerator;

  private final SupermanagerUIService supermanagerUIService;

  private final PlayerEffComparator playerEffComparator;

  private final TeamEffComparator teamEffComparator;


  private static final int ITERATIONS_PER_TEAM = 3000;

  private List<Player> bestGuards = new ArrayList<>();

  private List<Player> bestForwards = new ArrayList<>();

  private List<Player> bestCenters = new ArrayList<>();

  private HashMap<String, Integer> PRE_COUNT_MAP = new HashMap<>();

  private HashMap<String, Integer> POST_COUNT_MAP = new HashMap<>();

  private int TEAMS_TO_BE_CREATED = 1000;

  private int CHANGES_TO_BE_DONE = 3;

  private List<Long> teamIds = new ArrayList<>();


  public TeamCreator(SupermanagerUIService supermanagerUIService) {
    this.teamNameGenerator = new TeamNameGenerator();
    this.supermanagerUIService = supermanagerUIService;
    this.playerEffComparator = new PlayerEffComparator();
    this.teamEffComparator = new TeamEffComparator();
  }


  private static HashMap<String, Float> PLAYERS_TO_BUY = new HashMap<>();

  private List<Player> playerList = new ArrayList<>();

  private Map<String, Player> playerMap = new HashMap<>();


  public static final float SOME = 1.15f;

  public static final float LESS = 0.85f;

  public static final float FEW = 0.7f;

  public static final float NEVER = 0f;


  static {

    PLAYERS_TO_BUY.put("LAPROVITTOLA, NICO", SOME);
    PLAYERS_TO_BUY.put("CAMPAZZO, FACUNDO", 1.5f);
    PLAYERS_TO_BUY.put("HEURTEL, THOMAS", LESS);
    PLAYERS_TO_BUY.put("ERIKSSON, MARCUS", SOME);

    PLAYERS_TO_BUY.put("THOMPKINS, TREY", FEW);
    PLAYERS_TO_BUY.put("SUTTON, DOMINIQUE", FEW);

    PLAYERS_TO_BUY.put("LLULL, SERGIO", NEVER);

  }

  void initializePlayersLists(List<Player> playerPredictions) {
    playerPredictions.stream()
        .peek(System.out::println)
        .map(
            player -> PLAYERS_TO_BUY.containsKey(player.getName())
                ? newPlayer(player)
                .withPredictedEff(player.getPredictedEff() * PLAYERS_TO_BUY.get(player.getName()))
                .build()
                : player
        ).forEach(player -> playerList.add(player));

    playerMap = playerList.stream()
        .filter(player -> !isNull(player.getName()) && !player.getName().isEmpty() && !isNull(player.getPosition()))
        .distinct()
        .collect(toMap(Player::getName, player -> player));

    sort(playerList);

    bestGuards = getBestPlayersByPosition(GUARD, BEST_GUARDS_NUMBER);
    bestForwards = getBestPlayersByPosition(FORWARD, BEST_FORWARDS_NUMBER);
    bestCenters = getBestPlayersByPosition(CENTER, BEST_CENTERS_NUMBER);

    printPlayers(bestGuards);
    printPlayers(bestForwards);
    printPlayers(bestCenters);
  }

  private void printPlayers(final List<Player> bestPlayers) {
    System.out.println("---------------------------------");
    bestPlayers.stream()
        .forEachOrdered(System.out::println);
  }

  private List<Player> getBestPlayersByPosition(PositionEnum position, int playersNumber) {
    return concat(
        playerList.stream()
            .filter(player -> player.getPredictedEff() >= MIN_EFFICIENCY)
            .filter(player -> position.equals(player.getPosition()))
            .limit(playersNumber),
        playerList.stream()
            .filter(player -> player.getPredictedEff() >= MIN_EFFICIENCY)
            .filter(player -> position.equals(player.getPosition()))
            .filter(player -> SPANISH.equals(player.getNationality()))
            .limit(MINIMUM_SPANISH_PLAYERS),
        playerList.stream()
            .filter(player -> position.equals(player.getPosition()))
            .filter(player -> player.getPredictedEff() > ALWAYS_IN_EFF_THRESHOLD)
    )
        .distinct()
        .collect(toList());
  }

  public void createTeams() {
    int createdTeams = 0;
    while (createdTeams < TEAMS_TO_BE_CREATED) {

      SupermanagerTeam supermanagerTeam = supermanagerTeam()
          .withName(teamNameGenerator.generateName())
          .withMoney(6500000)
          .withPlayerPositions(
              getPlayerPositionsFromList(
                  concat(
                      getRandomlyShuffled(bestGuards).stream().limit(3),
                      getRandomlyShuffled(bestForwards).stream().limit(4),
                      getRandomlyShuffled(bestCenters).stream().limit(4)
                  ).collect(toList())))
          .build();

      if (supermanagerTeam.isValid()) {
        supermanagerUIService.createTeam(supermanagerTeam);
        createdTeams++;
      }
    }

    System.out.println("ALL " + TEAMS_TO_BE_CREATED + " teams created");
  }


  private HashMap<Integer, Player> getPlayerPositionsFromList(final List<Player> players) {
    AtomicInteger index = new AtomicInteger();
    HashMap<Integer, Player> playersPosition = new HashMap<>();
    players.forEach(player -> playersPosition.put(index.incrementAndGet(), player));
    return playersPosition;
  }

  private List<Player> getRandomlyShuffled(final List<Player> bestPlayers) {
    List<Player> players = bestPlayers.stream()
        .map(player -> newPlayer(player)
            .withPredictedEff(player.getPredictedEff() * ((float) random() * 0.6f + 0.7f))
            .build())
        .collect(toList());

    shuffle(players);
    return players;
  }


  private void initializeTeamIds(boolean onlyAlert) {
    try {
      File input = new File("/Users/ruben.arana/git/SuperManagerBot/src/main/resources/teams.html");
      Document doc = Jsoup.parse(input, "UTF-8");

      Elements teamElements = doc.getElementsByClass("equipo");

      for (Element teamNode : teamElements) {
        try {
          String uri = teamNode.getElementsByTag("a").attr("href");
          String id = uri.substring(uri.lastIndexOf("/") + 1);

          boolean alertTeam = isAlertedTeam(teamNode);

          if (!onlyAlert || alertTeam) {
            teamIds.add(Long.parseLong(id));
          }
        } catch (Exception e) {
          //
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean isAlertedTeam(final Element teamNode) {
    return teamNode.parent().getElementsByAttribute("src").size() > 0;
  }

  private Optional<SupermanagerTeam> generateTeamWithChanges(SupermanagerTeam team, final float variablePercentage) {

    final float actualExpectedEff = team.getExpectedEfficiency();
    System.out.println("--------------------------------");
    System.out.println("PREVIOUS: " + actualExpectedEff);

    return range(0, ITERATIONS_PER_TEAM)
        .mapToObj((i) -> fillTeam(removePlayersWithLessExpectedEfficiency(team), variablePercentage))
        .filter(SupermanagerTeam::isValid)
        .filter(tempTeam -> !isAlreadyCreated(tempTeam.getKey()))
        .max(teamEffComparator)
        .map(bestTeam -> {
          addToCreatedTeams(bestTeam.getKey());
          System.out.println("AFTER: " + bestTeam.getExpectedEfficiency());
          return bestTeam;
        });
  }

  private void addToCreatedTeams(final String key) {
    CREATED_TEAMS.add(key);
  }

  private boolean isAlreadyCreated(final String key) {
    return CREATED_TEAMS.contains(key);
  }

  private SupermanagerTeam fillTeam(SupermanagerTeam teamWithRemovedPlayers, float variablePercentage) {

    HashMap<Integer, Player> playersPositions = new HashMap<>();

    List<Player> players = teamWithRemovedPlayers.getAllTeamPlayers();

    range(1, 12)
        .forEachOrdered(
            index -> playersPositions.put(
                index,
                teamWithRemovedPlayers.getPlayersPositions().containsKey(index)
                    ? teamWithRemovedPlayers.getPlayersPositions().get(index)
                    : index <= 3
                        ? getRandomPlayerToAdd(players, bestGuards, variablePercentage)
                        : index <= 7
                            ? getRandomPlayerToAdd(players, bestForwards, variablePercentage)
                            : getRandomPlayerToAdd(players, bestCenters, variablePercentage)
            )
        );

    return supermanagerTeam()
        .withId(teamWithRemovedPlayers.getId())
        .withMoney(teamWithRemovedPlayers.getMoney())
        .withPlayerPositions(playersPositions)
        .build();
  }

  private Player getRandomPlayerToAdd(
      List<Player> players, final List<Player> bestPositionPlayers, final float variablePercentage
  ) {
    shuffle(bestPositionPlayers);

    Player playerToAdd = bestPositionPlayers.stream()
        .filter(player -> !players.contains(player))
        .findFirst()
        .map(player -> newPlayer(player)
            //.withPredictedEff(player.getPredictedEff() * ((1 - variablePercentage) + ((float) random() * 2 * variablePercentage)))
            .withPredictedEff(player.getPredictedEff() * 0.001f * (1000 - POST_COUNT_MAP.getOrDefault(player.getName(), 0)))
            .build())
        .orElseThrow(() -> new RuntimeException("Missing best players!"));

    players.add(playerToAdd);

    return playerToAdd;
  }

  private SupermanagerTeam removePlayersWithLessExpectedEfficiency(SupermanagerTeam team) {
    List<Player> teamPlayersByRatio = team.getAllTeamPlayers();
    sort(teamPlayersByRatio);

    List<Player> teamPlayersByEfficiency = team.getAllTeamPlayers();
    sort(teamPlayersByEfficiency, playerEffComparator);

    List<Player> possibleToRemovePlayers = concat(
        teamPlayersByRatio.stream().skip(teamPlayersByRatio.size() - CHANGES_TO_BE_DONE - EXTRA_PLAYERS_TO_BE_REMOVED),
        teamPlayersByEfficiency.stream().skip(teamPlayersByEfficiency.size() - CHANGES_TO_BE_DONE - EXTRA_PLAYERS_TO_BE_REMOVED)
    )
        .distinct()
        .collect(toList());

    shuffle(possibleToRemovePlayers);

    List<Player> playersToBeRemoved = possibleToRemovePlayers.stream()
        .limit(CHANGES_TO_BE_DONE)
        .collect(toList());

    Map<Integer, Player> playerPositions = new HashMap<>();

    team.getPlayersPositions().forEach((index, player) -> {
      if (!playersToBeRemoved.contains(player)) {
        playerPositions.put(index, player);
      }
    });

    return supermanagerTeam()
        .withMoney(team.getMoney())
        .withId(team.getId())
        .withPlayerPositions(playerPositions)
        .build();
  }


  void updateTeams(boolean onlyAlert, boolean startFromBegining) {
    initializeTeamIds(onlyAlert);

    int updatedTeams = 0;
    for (Long teamId : teamIds) {
      if (startFromBegining) {
        supermanagerUIService.resetTeam(teamId);
        //supermanagerUIService.removeAllSustituir(teamId);
        long preTime = System.currentTimeMillis();

        Optional<SupermanagerTeam> initialTeam = initializeTeam(teamId);

        final int finalUpdatedTeams = updatedTeams;
        Optional<SupermanagerTeam> newTeam = initialTeam
            .map(initTeam -> generateTeamWithChanges(initTeam, (float) finalUpdatedTeams / teamIds.size()))
            .map(Optional::get);

        if (!newTeam.isPresent()) {
          System.out.println("Unable to change team: " + initialTeam.get().getId());
        } else {
          supermanagerUIService.updateTeam(initialTeam.get(), newTeam.get());
          updatePlayersChosenPre(initialTeam.get());
          updatePlayersChosenPost(newTeam.get());
          updatedTeams++;
          printTimeTaken(updatedTeams, preTime, initialTeam.get());
        }

      } else {
        updatedTeams++;
        System.out.println("Updated teams " + updatedTeams);
        if (teamId == 1276337) {
          startFromBegining = true;
        }
      }


      if (updatedTeams % 100 == 0) {
        System.out.println("---------------------------------------------------------------------");
        System.out.println("MAP AT " + updatedTeams + " TEAMS");
        printPlayersPresenceMap();
      }
    }

    printPlayersPresenceMap();
  }

  private void printPlayersPresenceMap() {
    System.out.println("--------------------------- PREVIOUS PLAYERS ------------------------------");

    PRE_COUNT_MAP.entrySet()
        .stream()
        .sorted(reverseOrder(comparingByValue()))
        .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

    System.out.println("--------------------------- POST PLAYERS ------------------------------");

    POST_COUNT_MAP.entrySet()
        .stream()
        .sorted(reverseOrder(comparingByValue()))
        .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
  }

  private void updatePlayersChosenPre(final SupermanagerTeam team) {

    team.getAllTeamPlayers()
        .stream()
        .map(Player::getName)
        .forEach(this::addPlayerToPreCountMap);
  }

  private void updatePlayersChosenPost(final SupermanagerTeam team) {

    team.getAllTeamPlayers()
        .stream()
        .map(Player::getName)
        .forEach(this::addPlayerToPostCountMap);
  }

  private void addPlayerToPreCountMap(final String playerName) {
    if (PRE_COUNT_MAP.containsKey(playerName)) {
      PRE_COUNT_MAP.put(playerName, PRE_COUNT_MAP.get(playerName) + 1);
    } else {
      PRE_COUNT_MAP.put(playerName, 1);
    }
  }

  private void addPlayerToPostCountMap(final String playerName) {
    if (POST_COUNT_MAP.containsKey(playerName)) {
      POST_COUNT_MAP.put(playerName, POST_COUNT_MAP.get(playerName) + 1);
    } else {
      POST_COUNT_MAP.put(playerName, 1);
    }
  }

  private void printTimeTaken(int updatedTeams, final long preTime, final SupermanagerTeam initialTeam) {
    System.out.println(
        updatedTeams + "/" + teamIds.size()
            + ": Updated team: " + initialTeam.getId()
            + " (" + (System.currentTimeMillis() - preTime) + ")");
  }

  private Optional<SupermanagerTeam> initializeTeam(Long teamId) {

    return supermanagerUIService.changesAlreadyDone(teamId)
        ? empty()
        : of(
            supermanagerTeam()
                .withId(teamId)
                .withMoney(supermanagerUIService.getTeamMoney(teamId))
                .withPlayerPositions(
                    findPlayerPositionsFromPlayerNamesPositions(supermanagerUIService.getTeamPlayerNamesPositions(teamId)))
                .build()
        );
  }

  private Map<Integer, Player> findPlayerPositionsFromPlayerNamesPositions(Map<Integer, String> playerNamesPositions) {
    Map<Integer, Player> playerPositions = new HashMap<>();
    playerNamesPositions.forEach((index, playerName) -> playerPositions.put(index, playerMap.get(playerName)));
    return playerPositions;
  }

}
