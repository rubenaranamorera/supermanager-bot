import model.Player;
import model.SupermanagerTeam;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class SupermanagerUIService {

  public static final int MAX_CHANGES = 6;

  private static final int GUARDS_NUMBER = 3;
  private static final int FORWARDS_NUMBER = 4;
  private static final int CENTERS_NUMBER = 4;

  public static final String USER = "my_supermanager_user";
  public static final String PASSWORD = "my_supermanager_password";

  private TeamNameGenerator teamNameGenerator;

  private static ChromeDriver chromeDriver = new ChromeDriver();

  private static final String SM = "http://supermanager.acb.com";
  private static final String SM_LOG_IN = "http://supermanager.acb.com/index/identificar";
  private static final String SM_TEAMS_SELL = "http://supermanager.acb.com/equipos/vender/equipo_id/";
  private static final String SM_TEAMS_VIEW = "http://supermanager.acb.com/equipos/ver/id/";
  private static final String SM_TEAMS_CONFIRM = "http://supermanager.acb.com/equipos/confirmar/id/";
  private static final String SM_TEAMS_CREATE = "http://supermanager.acb.com/equipos/crear";
  private static final String SM_TEAMS_BUY = "http://supermanager.acb.com/equipos/comprar/equipo_id/";
  private static final String SM_TEAMS_RESET = "http://supermanager.acb.com/equipos/reset/equipo_id/";


  public SupermanagerUIService(TeamNameGenerator teamNameGenerator) {
    this.teamNameGenerator = teamNameGenerator;
  }


  public void start() {
    logIn();
    //initializePlayers();
    //initializePlayersToBuy();
    //initializePlayersToSell();

    //changePlayers();
  }




  public void updateTeam(SupermanagerTeam initialSupermanagerTeam, SupermanagerTeam newSupermanagerTeam) {
    //SELL
    for (Player player : initialSupermanagerTeam.getGuards()) {
      if (!newSupermanagerTeam.getGuards().contains(player)) {
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/1/posicion_id/1");
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/2/posicion_id/1");
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/3/posicion_id/1");
      }
    }

    for (Player player : initialSupermanagerTeam.getForwards()) {
      if (!newSupermanagerTeam.getForwards().contains(player)) {
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/4/posicion_id/3");
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/5/posicion_id/3");
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/6/posicion_id/3");
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/7/posicion_id/3");
      }
    }

    for (Player player : initialSupermanagerTeam.getCenters()) {
      if (!newSupermanagerTeam.getCenters().contains(player)) {
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/8/posicion_id/5");
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/9/posicion_id/5");
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/10/posicion_id/5");
        chromeDriver.get(SM_TEAMS_SELL + newSupermanagerTeam.getId() + "/jugador_id/" + player.getId() + "/puesto/11/posicion_id/5");
      }
    }

    //BUY
    for (Player player : newSupermanagerTeam.getGuards()) {
      if (!initialSupermanagerTeam.getGuards().contains(player)) {
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/1/puesto/1/jugador_id/" + player.getId());
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/1/puesto/2/jugador_id/" + player.getId());
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/1/puesto/3/jugador_id/" + player.getId());
      }
    }

    for (Player player : newSupermanagerTeam.getForwards()) {
      if (!initialSupermanagerTeam.getForwards().contains(player)) {
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/3/puesto/4/jugador_id/" + player.getId());
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/3/puesto/5/jugador_id/" + player.getId());
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/3/puesto/6/jugador_id/" + player.getId());
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/3/puesto/7/jugador_id/" + player.getId());
      }
    }

    for (Player player : newSupermanagerTeam.getCenters()) {
      if (!initialSupermanagerTeam.getCenters().contains(player)) {
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/5/puesto/8/jugador_id/" + player.getId());
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/5/puesto/9/jugador_id/" + player.getId());
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/5/puesto/10/jugador_id/" + player.getId());
        chromeDriver.get(SM_TEAMS_BUY + newSupermanagerTeam.getId() + "/posicion_id/5/puesto/11/jugador_id/" + player.getId());
      }
    }
  }

  /*
  private SupermanagerTeam fillTeam(SupermanagerTeam supermanagerTeam) {
    SupermanagerTeam newSupermanagerTeam = new SupermanagerTeam();
    List<Player> guards = new ArrayList<>();
    List<Player> forwards = new ArrayList<>();
    List<Player> centers = new ArrayList<>();

    for (Player guard : supermanagerTeam.getGuards()) {
      guards.add(guard);
    }
    for (Player forward : supermanagerTeam.getForwards()) {
      forwards.add(forward);
    }
    for (Player center : supermanagerTeam.getCenters()) {
      centers.add(center);
    }

    while (guards.size() < GUARDS_NUMBER) {
      Player player = RandomPicker.pickOnePlayer(guardsToBuy);
      if (!guards.contains(player)) {
        guards.add(player);
      }
    }

    while (forwards.size() < FORWARDS_NUMBER) {
      Player player = RandomPicker.pickOnePlayer(forwardsToBuy);
      if (!forwards.contains(player)) {
        forwards.add(player);
      }
    }

    while (centers.size() < CENTERS_NUMBER) {
      Player player = RandomPicker.pickOnePlayer(centersToBuy);
      if (!centers.contains(player)) {
        centers.add(player);
      }
    }

    newSupermanagerTeam.setGuards(guards);
    newSupermanagerTeam.setForwards(forwards);
    newSupermanagerTeam.setCenters(centers);
    newSupermanagerTeam.setId(supermanagerTeam.getId());
    newSupermanagerTeam.setMoney(supermanagerTeam.getMoney());

    return newSupermanagerTeam;
  }

  private SupermanagerTeam sellPlayers(SupermanagerTeam supermanagerTeam) {
    List<Player> guards = new ArrayList<>();
    List<Player> forwards = new ArrayList<>();
    List<Player> centers = new ArrayList<>();

    int soldPlayers = 0;

    for (Player player : supermanagerTeam.getGuards()) {
      if (soldPlayers == MAX_CHANGES) {
        guards.add(player);
      } else {
        if (!guardsToSell.contains(player)) {
          guards.add(player);
        } else {
          soldPlayers++;
        }
      }
    }

    for (Player player : supermanagerTeam.getForwards()) {
      if (soldPlayers == MAX_CHANGES) {
        forwards.add(player);
      } else {
        if (!forwardsToSell.contains(player)) {
          forwards.add(player);
        } else {
          soldPlayers++;
        }
      }
    }

    for (Player player : supermanagerTeam.getCenters()) {
      if (soldPlayers == MAX_CHANGES) {
        centers.add(player);
      } else {
        if (!centersToSell.contains(player)) {
          centers.add(player);
        } else {
          soldPlayers++;
        }
      }
    }


    if (soldPlayers < MAX_CHANGES && !centers.isEmpty()) {
      Player player = RandomPicker.pickOnePlayer(centers);
      centers.remove(player);
      soldPlayers++;
    }

    if (soldPlayers < MAX_CHANGES && !forwards.isEmpty()) {
      Player player = RandomPicker.pickOnePlayer(forwards);
      forwards.remove(player);
      soldPlayers++;
    }

    if (soldPlayers < MAX_CHANGES && !guards.isEmpty()) {
      Player player = RandomPicker.pickOnePlayer(guards);
      guards.remove(player);
      soldPlayers++;
    }

    SupermanagerTeam supermanagerTeamWithSoldPlayers = new SupermanagerTeam();
    supermanagerTeamWithSoldPlayers.setId(supermanagerTeam.getId());
    supermanagerTeamWithSoldPlayers.setMoney(supermanagerTeam.getMoney());
    supermanagerTeamWithSoldPlayers.setGuards(guards);
    supermanagerTeamWithSoldPlayers.setForwards(forwards);
    supermanagerTeamWithSoldPlayers.setCenters(centers);

    return supermanagerTeamWithSoldPlayers;
  }


  private void initializePlayersToBuy() {
    for (Player guard : guards) {
      if (GUARDS_TO_BUY_NAMES.contains(guard.getName())) {
        guardsToBuy.add(guard);
      }
    }

    for (Player forward : forwards) {
      if (FORWARDS_TO_BUY_NAMES.contains(forward.getName())) {
        forwardsToBuy.add(forward);
      }
    }

    for (Player center : centers) {
      if (CENTERS_TO_BUY_NAMES.contains(center.getName())) {
        centersToBuy.add(center);
      }
    }
  }

  private void initializePlayersToSell() {
    for (Player guard : guards) {
      if (GUARDS_TO_SELL_NAMES.contains(guard.getName())) {
        guardsToSell.add(guard);
      }
    }

    for (Player forward : forwards) {
      if (FORWARDS_TO_SELL_NAMES.contains(forward.getName())) {
        forwardsToSell.add(forward);
      }
    }

    for (Player center : centers) {
      if (CENTERS_TO_SELL_NAMES.contains(center.getName())) {
        centersToSell.add(center);
      }
    }
  }

  private void initializePlayers() {
    guards = initializePlayers("/Users/ruben.arana/git/SuperManagerBot/src/main/resources/guards.html", PositionEnum.GUARD);
    forwards = initializePlayers("/Users/ruben.arana/git/SuperManagerBot/src/main/resources/forwards.html", PositionEnum.FORWARD);
    centers = initializePlayers("/Users/ruben.arana/git/SuperManagerBot/src/main/resources/centers.html", PositionEnum.CENTER);
  }

  private List<Player> initializePlayers(String fileUri, PositionEnum postition) {

    List<Player> players = new ArrayList();
    try {
      File input = new File(fileUri);
      Document doc = Jsoup.parse(input, "UTF-8");

      Elements playerElements = doc.getElementsByClass("jugequipo");

      for (Element playerNode : playerElements) {
        try {
          String name = playerNode.getElementsByTag("a").text();
          String uri = playerNode.parent().childNodes().get(11).childNode(1).childNode(0).attr("href");
          String id = uri.substring(uri.lastIndexOf("/") + 1);
          long price = Long.parseLong(((Element) playerNode.parent().childNode(9)).text().replace(".", ""));
          Player player = Player.newPlayer().withPosition(postition).withId(id).withPrice(price).withName(name).build();
          players.add(player);
        } catch (Exception e) {
          //
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return players;
  }

  */

  public void logIn() {
    chromeDriver.get(SM_LOG_IN);

    chromeDriver.findElementById("email").sendKeys(USER);
    chromeDriver.findElementById("clave").sendKeys(PASSWORD);
    chromeDriver.findElementById("entrar").click();
  }

  public void createTeam(SupermanagerTeam supermanagerTeam) {
    System.out.println(supermanagerTeam);
    chromeDriver.get(SM_TEAMS_CREATE);
    long teamId = Integer.parseInt(chromeDriver.findElementById("FormBuscarLigas")
        .getAttribute("action")
        .replaceAll(SM_TEAMS_CONFIRM, ""));

    int index = 1;
    for (Player guard : supermanagerTeam.getGuards()) {
      chromeDriver.get(SM_TEAMS_BUY + teamId + "/posicion_id/1/puesto/" + index + "/jugador_id/" + guard.getId());
      index++;
    }

    for (Player forward : supermanagerTeam.getForwards()) {
      chromeDriver.get(SM_TEAMS_BUY + teamId + "/posicion_id/3/puesto/" + index + "/jugador_id/" + forward.getId());
      index++;
    }

    for (Player center : supermanagerTeam.getCenters()) {
      chromeDriver.get(SM_TEAMS_BUY + teamId + "/posicion_id/5/puesto/" + index + "/jugador_id/" + center.getId());
      index++;
    }

    chromeDriver.get(SM_TEAMS_CREATE);

    chromeDriver.findElementById("nombre").sendKeys(supermanagerTeam.getName());
    chromeDriver.findElementById("FormBuscarLigas").submit();
  }

  public boolean changesAlreadyDone(Long teamId) {
    chromeDriver.get(SM_TEAMS_VIEW + teamId);

    if (chromeDriver.findElementByClassName("cambios_jornada").getText().equals("0")) {
      return false;
    }
    return true;
  }

  public long getTeamMoney(Long teamId) {
    chromeDriver.get(SM_TEAMS_VIEW + teamId);

    long leftMoney = Integer.parseInt(chromeDriver.findElementById("presupuesto").getText().replaceAll("\\.", ""));
    long spendMoney = Integer.parseInt(chromeDriver.findElementByClassName("total-cotizacion").getText().replaceAll("\\.", ""));

    return leftMoney + spendMoney;
  }

  public List<String> getTeamPlayerNames(Long teamId) {
    chromeDriver.get(SM_TEAMS_VIEW + teamId);

    List<WebElement> playerElements = chromeDriver.findElementsByClassName("jugequipo");
    List<String> playerNames = new ArrayList<>();

    for (WebElement playerNode : playerElements) {
      try {
        String name = playerNode.findElement(By.className("negrita")).findElement(By.tagName("a")).getText();
        playerNames.add(SMUtils.normalize(name));
      } catch (Exception e) {
        //
      }
    }
    return playerNames;
  }

  public void resetTeam(Long teamId) {
    chromeDriver.get(SM_TEAMS_RESET + teamId);
  }

  public void removeAllSustituir(Long teamId) {
    chromeDriver.get(SM_TEAMS_VIEW + teamId);
    List<WebElement> buttons = chromeDriver.findElementsByClassName("gradient-verde");

    List<String> hrefs = new ArrayList<>();

    for (WebElement button: buttons) {
      String href = button.getAttribute("href");
      hrefs.add(href);
    }

    hrefs.forEach(href -> chromeDriver.get(href));
  }
}


