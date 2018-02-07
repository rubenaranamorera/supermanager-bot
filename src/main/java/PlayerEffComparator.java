import model.Player;

import java.util.Comparator;

public class PlayerEffComparator implements Comparator<Player> {

  @Override
  public int compare(Player o1, Player o2) {
    if (o1.getPredictedEff() > o2.getPredictedEff()) {
      return 1;
    } else if (o1.getPredictedEff() < o2.getPredictedEff()) {
      return -1;
    } else {
      return 0;
    }
  }
}
