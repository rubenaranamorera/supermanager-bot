import model.Player;
import model.SupermanagerTeam;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class SupermanagerUIService {

  public static final String USER = "armoz";

  public static final String PASSWORD = "starcraf";

  private static ChromeDriver chromeDriver = new ChromeDriver();

  private static final String SM_LOG_IN = "http://supermanager.acb.com/index/identificar";

  private static final String SM_TEAMS_SELL = "http://supermanager.acb.com/equipos/vender/equipo_id/";

  private static final String SM_TEAMS_VIEW = "http://supermanager.acb.com/equipos/ver/id/";

  private static final String SM_TEAMS_CONFIRM = "http://supermanager.acb.com/equipos/confirmar/id/";

  private static final String SM_TEAMS_CREATE = "http://supermanager.acb.com/equipos/crear";

  private static final String SM_TEAMS_BUY = "http://supermanager.acb.com/equipos/comprar/equipo_id/";

  private static final String SM_TEAMS_RESET = "http://supermanager.acb.com/equipos/reset/equipo_id/";


  public void updateTeam(SupermanagerTeam initialSupermanagerTeam, SupermanagerTeam newSupermanagerTeam) {

    Map<Integer, Player> initialPlayerPositions = initialSupermanagerTeam.getPlayersPositions();
    Map<Integer, Player> endPlayerPositions = newSupermanagerTeam.getPlayersPositions();

    IntStream.range(1, 12)
        .filter(index -> !initialPlayerPositions.get(index).equals(endPlayerPositions.get(index)))
        .forEach(index -> sellPlayer(
            newSupermanagerTeam.getId(),
            initialPlayerPositions.get(index),
            index));

    IntStream.range(1, 12)
        .filter(index -> !initialPlayerPositions.get(index).equals(endPlayerPositions.get(index)))
        .forEach(index -> buyPlayer(
            newSupermanagerTeam.getId(),
            endPlayerPositions.get(index),
            index));
  }

  private void sellPlayer(long teamId, Player player, int index) {
    chromeDriver.get(
        SM_TEAMS_SELL + teamId
            + "/jugador_id/" + player.getId()
            + "/puesto/" + index
            + "/posicion_id/" + player.getPosition().getId());
  }

  private void buyPlayer(long teamId, Player player, int index) {
    chromeDriver.get(SM_TEAMS_BUY + teamId
        + "/posicion_id/" + player.getPosition().getId()
        + "/puesto/" + index
        + "/jugador_id/" + player.getId());
  }

  public void logIn() {
    chromeDriver.get(SM_LOG_IN);

    chromeDriver.findElementById("email").sendKeys(USER);
    chromeDriver.findElementById("clave").sendKeys(PASSWORD);
    chromeDriver.findElementById("entrar").click();
  }

  public void createTeam(SupermanagerTeam supermanagerTeam) {
    System.out.println(supermanagerTeam);

    chromeDriver.get(SM_TEAMS_CREATE);
    long teamId = parseInt(chromeDriver.findElementById("FormBuscarLigas")
        .getAttribute("action")
        .replaceAll(SM_TEAMS_CONFIRM, ""));

    IntStream.range(1, 12)
        .forEach(index -> buyPlayer(
            teamId,
            supermanagerTeam.getPlayersPositions().get(index),
            index));

    chromeDriver.get(SM_TEAMS_CREATE);

    chromeDriver.findElementById("nombre").sendKeys(supermanagerTeam.getName());
    chromeDriver.findElementById("FormBuscarLigas").submit();
  }

  public boolean changesAlreadyDone(Long teamId) {
    chromeDriver.get(SM_TEAMS_VIEW + teamId);
    return !chromeDriver.findElementByClassName("cambios_jornada").getText().equals("0");
  }

  public long getTeamMoney(Long teamId) {
    chromeDriver.get(SM_TEAMS_VIEW + teamId);

    long leftMoney = parseInt(chromeDriver.findElementById("presupuesto").getText().replaceAll("\\.", ""));
    long spendMoney = parseInt(chromeDriver.findElementByClassName("total-cotizacion").getText().replaceAll("\\.", ""));

    return leftMoney + spendMoney;
  }

  public Map<Integer, String> getTeamPlayerNamesPositions(Long teamId) {
    chromeDriver.get(SM_TEAMS_VIEW + teamId);

    List<WebElement> playerElements = chromeDriver.findElementsByClassName("jugequipo");
    Map<Integer, String> playerNamesPositions = new HashMap<>();

    int index = -1;
    for (WebElement playerNode : playerElements) {
      index++;
      try {
        String name = playerNode.findElement(By.className("negrita")).findElement(By.tagName("a")).getText();
        playerNamesPositions.put(index, SMUtils.normalize(name));
      } catch (Exception e) {
        //
      }
    }
    return playerNamesPositions;
  }

  public void resetTeam(Long teamId) {
    chromeDriver.get(SM_TEAMS_RESET + teamId);
  }

  public void removeAllSustituir(Long teamId) {
    chromeDriver.get(SM_TEAMS_VIEW + teamId);
    List<WebElement> buttons = chromeDriver.findElementsByClassName("gradient-verde");

    List<String> hrefs = new ArrayList<>();

    for (WebElement button : buttons) {
      String href = button.getAttribute("href");
      hrefs.add(href);
    }

    hrefs.forEach(href -> chromeDriver.get(href));
  }
}


