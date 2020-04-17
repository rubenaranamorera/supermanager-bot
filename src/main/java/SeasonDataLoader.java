import model.PlayerGame;
import model.TeamGame;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SeasonDataLoader {

  public static final String SEASON = "62";
  public static final String GAME_STATS_END_URL = ".php";
  public static final String GAME_STATS_URL = "http://www.acb.com/fichas/LACB" + SEASON;
  public static final int GAMES_NUMBER = 300; //jornada 20 160, total 290

  private InMemoryDB inMemoryDB;

  public SeasonDataLoader(InMemoryDB inMemoryDB) {
    this.inMemoryDB = inMemoryDB;
  }


  public List<TeamGame> obtainAllTeamGames() {

    List<TeamGame> games = new ArrayList<>();

    for (int game = 1; game < GAMES_NUMBER; game++) {

      String gameStr = String.valueOf(game);
      if (gameStr.length() == 1) {
        gameStr = "00" + gameStr;
      } else if (gameStr.length() == 2) {
        gameStr = "0" + gameStr;
      }

      try {

        URL url = new URL(GAME_STATS_URL + gameStr + GAME_STATS_END_URL);
        Document gameDoc = Jsoup.parse(url, 5000);

        List<String> teams = parseTeamNames(gameDoc);

        Elements trElements = gameDoc.getElementsByClass("estadisticasnew")
            .get(1)
            .getElementsByTag("tr");


        List<PlayerGame> team1PlayerGames = new ArrayList<>();
        List<PlayerGame> team2PlayerGames = new ArrayList<>();

        boolean shouldAddToTeam1 = true;
        int estverde = 0;
        for (Element tr : trElements) {
          if (tr.hasClass("estverde")) {
            estverde = estverde + 1;
            if (estverde == 4) {
              shouldAddToTeam1 = false;
            }
          } else if (tr.getAllElements().size() > 4 && tr.getElementsByTag("a").size() == 1) {
            PlayerGame playerGame;
            if (shouldAddToTeam1) {
              playerGame = parsePlayerGame(tr, teams.get(0), teams.get(1));
              team1PlayerGames.add(playerGame);
            } else {
              playerGame = parsePlayerGame(tr, teams.get(1), teams.get(0));
              team2PlayerGames.add(playerGame);
            }
          }
        }

        TeamGame teamGame = new TeamGame.Builder()
            .withTeam1Name(teams.get(0))
            .withTeam1PlayerGames(team1PlayerGames)
            .withTeam2Name(teams.get(1))
            .withTeam2PlayerGames(team2PlayerGames)
            .build();

        inMemoryDB.addTeamGame(teamGame);
        games.add(teamGame);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }


    System.out.println("GAMES: " + games.size());

    return games;
  }

  private PlayerGame parsePlayerGame(Element tr, String teamName, String oppositeTeamName) {
    Elements tdElements = tr.getElementsByTag("td");

    return new PlayerGame.Builder()
        .withName(SMUtils.normalize(getCuratedInput(tdElements.get(1).text())))
        .withSecondsPlayed(calculateSeconds(getCuratedInput((tdElements.get(2).text()))))
        .withPoints(Integer.parseInt(getCuratedInput(tdElements.get(3).text())))
        .withThrees(Integer.parseInt(getCuratedInput(tdElements.get(6).text())))
        .withRebounds(Integer.parseInt(getCuratedInput(tdElements.get(10).text())))
        .withAssists(Integer.parseInt(getCuratedInput(tdElements.get(12).text())))
        .withEfficiency(Integer.parseInt(getCuratedInput(tdElements.get(22).text())))
        .withTeamName(teamName)
        .withOppositeTeamName(oppositeTeamName)
        .build();
  }

  private String getCuratedInput(String text) {
    if (text.equals("\u00a0")) {
      return "0";
    }
    if (text.contains("/")) {
      return text.substring(0, text.indexOf("/"));
    }
    return text;
  }

  private int calculateSeconds(String timePlayed) {
    if (timePlayed.contains(":")) {
      String min = timePlayed.substring(0, timePlayed.indexOf(":"));
      String sec = timePlayed.substring(timePlayed.indexOf(":") + 1);
      return Integer.parseInt(min) * 60 + Integer.parseInt(sec);
    }
    return Integer.parseInt(timePlayed);
  }

  private List<String> parseTeamNames(Document gameDoc) {
    List<String> teams = new ArrayList<>();
    Elements teamNames = gameDoc.getElementsByClass("estverdel");

    for (Element teamName : teamNames) {
      teams.add(SMUtils.normalize(teamName.text().substring(0, teamName.text().lastIndexOf(' '))));
    }

    return teams;
  }

  public void populateDB() {
    inMemoryDB.createTeamStatistics();
    inMemoryDB.createPlayerGamesList();
  }
}
