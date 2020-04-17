import model.Player;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SuperManagerBotMain {

  private static final boolean INITIALIZE_PLAYERS_DEFAULT = true;

  public static void main(String[] args ) throws IOException {
    long pretime = System.currentTimeMillis();

    PlayerIdFileInitializer initializer = new PlayerIdFileInitializer();
    TeamNameGenerator teamNameGenerator = new TeamNameGenerator();
    SupermanagerUIService service = new SupermanagerUIService(teamNameGenerator);
    service.logIn();

    if (INITIALIZE_PLAYERS_DEFAULT) {
      initializer.createPlayerIdFiles();
    }

    List<Player> players = initializer.obtainAllPlayersForNextGame();

    InMemoryDB inMemoryDB = new InMemoryDB();
    SeasonDataLoader seasonDataLoader = new SeasonDataLoader(inMemoryDB);
    seasonDataLoader.obtainAllTeamGames();
    seasonDataLoader.populateDB();

    System.out.println("DB populated" +  (System.currentTimeMillis() - pretime));

    Predictor predictor = new Predictor(inMemoryDB);
    List<Player> playerPredictions = predictor.createPredictions(players);
    System.out.println("Predictions done" +  (System.currentTimeMillis() - pretime));

    Collections.sort(playerPredictions);

    System.out.println("Predictions sorted" +  (System.currentTimeMillis() - pretime));

    TeamCreator teamCreator = new TeamCreator(teamNameGenerator, service, new PlayerEffComparator());
    teamCreator.initializePlayersLists(playerPredictions);

    System.out.println("PlayerListsInitialized" +  (System.currentTimeMillis() - pretime));

    //teamCreator.createTeams();
    //teamCreator.updateTeams();
  }

}
