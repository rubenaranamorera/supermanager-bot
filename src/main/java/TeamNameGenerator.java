import java.util.Arrays;
import java.util.List;

public class TeamNameGenerator {

  private static final List<String> USERNAMES;
  private static final List<String> NICKNAMES;


  static {
    USERNAMES = Arrays.asList("ArMoZ", "ArMoZSlaYeR", "armoz", "armozslayer", "SCM", "Ruarmo", "Roser", "CBRoser", "CIC", "CBCIC",
        "Carlorz", "Ratilla", "Barcelona", "BCN", "IJ", "BigBurger", "Sam", "Infojobs", "Pintimilf", "Frankfust", "Ribs", "Martins",
        "Pollo", "VP", "Penyal", "Curri", "Iba", "AláAlá", "DarksideVeron", "Gadzeta", "Juanpi", "Corco");

    NICKNAMES = Arrays.asList("Wildcats", "Grizzlies", "Panthers", "Bulldogs", "Falcons", "Zips", "Tide", "Blazers", "Chargers",
        "Hornets", "Seawolves", "Nanooks", "Lions", "Braves", "AllReds", "Black&Reds", "AllBlacks", "Battlers", "Eagles", "Crusaders",
        "Ravens", "Trojans", "Hackers", "Knights", "Devils", "SunDevils", "Vampires", "Timberwolves", "Pandas", "Suns", "JailBlazers",
        "Dancers", "Conquerors", "Saints", "Avengers", "Greyhounds", "Senators", "Stealers", "Vikings", "Spartans", "Kangaroos", "Cougars",
        "Cardinals", "Raptors", "Bears", "Whales", "Dolphins", "Bucaneers", "Travelers", "Pilots", "Huskies", "Royals", "Bearcats",
        "Deacons", "Rams", "Broncos", "Terriers", "Seahawks", "Bisons", "BlueWarriors", "RedWarriors", "BlackWarriors", "Judges",
        "Prisoners", "Cavaliers", "Clippers", "Bulls", "Supersonics", "Highlanders", "Tritons", "Slugs", "Roadrunners", "Titans",
        "Matadors", "Aztecs", "Coyotes", "Gators", "Hornets", "Vulcans", "Camels", "Tartans", "Drivers", "Fuckers", "Winners", "Loosers",
        "Cobras", "Tornadoes", "Storms", "Lightbolts", "Aussies", "Pocha model.SupermanagerTeam", "Paladins", "UnderTheBridges", "Lumberjacks",
        "Muskies", "Picachus", "Pirates", "Engineers", "Rebels", "76ers", "Longpants", "Waterparties");

  }

  public String generateName() {
    return new StringBuilder()
        .append(RandomPicker.pickOneString(USERNAMES))
        .append(" ")
        .append(RandomPicker.pickOneString(NICKNAMES))
        .toString();
  }
}
