import model.SupermanagerTeam;

import java.util.Comparator;

public class TeamEffComparator implements Comparator<SupermanagerTeam> {

  @Override
  public int compare(SupermanagerTeam o1, SupermanagerTeam o2) {
    if (o1.getExpectedEfficiency() > o2.getExpectedEfficiency()) {
      return 1;
    } else if (o1.getExpectedEfficiency() < o2.getExpectedEfficiency()) {
      return -1;
    } else {
      return 0;
    }
  }
}
