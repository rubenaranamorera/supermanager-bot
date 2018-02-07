import model.Player;

import java.util.List;
import java.util.Random;

public class RandomPicker {

  public static Player pickOnePlayer(List<Player> players) {
    return players.get(new Random().nextInt(players.size()));
  }

  public static String pickOneString(List<String> elements) {
    return elements.get(new Random().nextInt(elements.size()));
  }

}
