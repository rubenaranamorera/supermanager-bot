import model.Player;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.sort;

public class SuperManagerBotMain {

  private static final boolean INITIALIZE_PLAYERS_DEFAULT = true;

  public static void main(String[] args) throws IOException {
    SupermanagerUIService supermanagerUIService = new SupermanagerUIService();
    supermanagerUIService.logIn();

    PlayerIdFileInitializer initializer = new PlayerIdFileInitializer();
    if (INITIALIZE_PLAYERS_DEFAULT) {
      initializer.createPlayerIdFiles();
    }
    List<Player> players = initializer.obtainAllPlayersForNextGame();

    InMemoryDB inMemoryDB = new InMemoryDB();
    SeasonDataLoader seasonDataLoader = new SeasonDataLoader(inMemoryDB);
    seasonDataLoader.obtainAllTeamGames();
    seasonDataLoader.populateDB();

    Predictor predictor = new Predictor(inMemoryDB);
    List<Player> playerPredictions = predictor.createPredictions(players);
    sort(playerPredictions);

    TeamCreator teamCreator = new TeamCreator(supermanagerUIService);
    teamCreator.initializePlayersLists(playerPredictions);

    //teamCreator.createTeams();
    teamCreator.updateTeams(false, true);
  }

}
