import model.Player;
import model.PlayerNationalityEnum;
import model.PositionEnum;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerIdFileInitializer {

  public static final String URI = "src/main/resources/";
  public static final int ICONS_INDEX = 0;
  public static final int PLAYER_NAME_INDEX = 2;
  public static final int TEAM_NAME_INDEX = 3;

  HashMap<String, String> guardsIdMap = new HashMap<>();
  HashMap<String, String> forwardsIdMap = new HashMap<>();
  HashMap<String, String> centersIdMap = new HashMap<>();


  public void createPlayerIdFiles() throws IOException {
    getPlayersIdMap(PositionEnum.GUARD);
    getPlayersIdMap(PositionEnum.FORWARD);
    getPlayersIdMap(PositionEnum.CENTER);
  }

  private void getPlayersIdMap(PositionEnum position) throws IOException {
    File input = new File(URI + position + ".html");

    Document doc = Jsoup.parse(input, "UTF-8");
    Elements playerElements = doc.getElementsByClass("par ");
    playerElements.addAll(doc.getElementsByClass("impar "));

    for (Element playerNode : playerElements) {
      Elements tdList = playerNode.getElementsByTag("td");
      String name = SMUtils.normalize(tdList.get(PLAYER_NAME_INDEX).getElementsByTag("a").text());
      String buyPlayerUri = tdList.get(5).getElementsByTag("a").attr("href");
      String id = buyPlayerUri.substring(buyPlayerUri.lastIndexOf("/") + 1);

      if (position == PositionEnum.GUARD) {
        guardsIdMap.put(name, id);
      } else if (position == PositionEnum.FORWARD) {
        forwardsIdMap.put(name, id);
      } else {
        centersIdMap.put(name, id);
      }

    }
  }

  private PlayerNationalityEnum getNationalityFromIconSrc(String iconSrc) {
    if (iconSrc.contains("ico-espanol")) {
      return PlayerNationalityEnum.SPANISH;
    } else if (iconSrc.contains("ico-extracomunitario")) {
      return PlayerNationalityEnum.AMERICAN;
    } else {
      return null;
    }
  }

  public List<Player> obtainAllPlayersForNextGame() throws IOException {
    List<Player> players = new ArrayList<>();
    Document document = Jsoup.parse(new File(URI + "market.html"), "UTF-8");

    Elements tbodies = document.getElementsByTag("tbody");
    for (Element tbody: tbodies) {
      Elements trs = tbody.getElementsByTag("tr");
      for (Element tr: trs) {
        players.add(obtainPlayer(tr));
      }
    }


    /* Boungou-Colo, Nobel
    Player p2 = Player.newPlayer()
        .withId("2808")
        .withInjured(true)
        .withPrice(751188)
        .withPosition(PositionEnum.FORWARD)
        .withTeamName("REAL BETIS ENERGIA PLUS")
        .withName("BOUNGOU-COLO, NOBEL")
        .withNationality(PlayerNationalityEnum.EUROPEAN)
        .build();

    players.add(p2);*/

    return players;
  }

  private Player obtainPlayer(Element playerNode) {
    Elements tdList = playerNode.getElementsByTag("td");

    boolean isInjured = false;

    //Nationality
    PlayerNationalityEnum playerNationality = PlayerNationalityEnum.EUROPEAN;
    Elements icons = tdList.get(ICONS_INDEX).getElementsByAttribute("src");
    for (Element icon : icons) {
      String iconSrc = icon.attr("src");
      PlayerNationalityEnum playerNationalityCalculated = getNationalityFromIconSrc(iconSrc);
      if (playerNationalityCalculated != null) {
        playerNationality = playerNationalityCalculated;
        break;
      }
    }

    for (Element icon : icons) {
      String iconSrc = icon.attr("src");
      if (iconSrc.contains("ico-lesionado")){
        isInjured = true;
      }
    }

    String name = SMUtils.normalize(tdList.get(PLAYER_NAME_INDEX).getElementsByTag("a").text());
    String teamName = SMUtils.normalize(tdList.get(TEAM_NAME_INDEX).child(0).attr("title"));

    String id = "";
    PositionEnum position = null;
    if(guardsIdMap.containsKey(name)){
      id = guardsIdMap.get(name);
      position = PositionEnum.GUARD;
    } else if(forwardsIdMap.containsKey(name)){
      id = forwardsIdMap.get(name);
      position = PositionEnum.FORWARD;
    } else if(centersIdMap.containsKey(name)){
      id = centersIdMap.get(name);
      position = PositionEnum.CENTER;
    }

    int size = tdList.get(12).getElementsByTag("img").size();
    boolean home = false;
    if (size == 1) {
      home = true;
    }
    String teamAgainst = SMUtils.normalize(tdList.get(12).child(size-1).attr("title"));
    float eff = Float.parseFloat(tdList.get(4).text().replaceAll(",", "\\."));
    long price = Long.parseLong(tdList.get(5).text().replaceAll("\\.", ""));

    if (eff == 0) {
      eff = price / 70000;
    }

    return Player.newPlayer()
        .withId(id)
        .withName(name)
        .withPosition(position)
        .withPrice(price)
        .withHome(home)
        .withInjured(isInjured)
        .withTeamName(teamName)
        .withTeamAgainst(teamAgainst)
        .withPredictedEff(eff)
        .withNationality(playerNationality)
        .build();
  }
}
