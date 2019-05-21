package de.zorcic.kicker.ranking.entity;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import de.zorcic.kicker.games.entity.Game;
import de.zorcic.kicker.games.entity.NewGameEvent;
import de.zorcic.kicker.players.entity.Player;
import io.quarkus.runtime.StartupEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;

@Transactional
@ApplicationScoped
public class Ranking {

    ConcurrentMap<Long, Integer> eloScorePerPlayer;
    ConcurrentMap<UUID, Integer> eloScorePerGame;

    @PersistenceContext
    EntityManager em;

    void onStart(@Observes StartupEvent ev) {
        eloScorePerPlayer = new ConcurrentHashMap<>();
        eloScorePerGame = new ConcurrentHashMap<>();
        em.createNamedQuery("Game.findAllSorted", Game.class).getResultList().forEach(this::updateRanking);
    }

    public void observeNewGame(@Observes NewGameEvent event) {
        this.updateRanking(em.find(Game.class, event.gameId()));
    }

    private void updateRanking(Game game) {
        int team1elo = teamElo(game.getTeam1Player1(), game.getTeam1Player2());
        int team2elo = teamElo(game.getTeam2Player1(), game.getTeam2Player2());
        int team1gain = calculateElo(game.getScoreTeam1Game1() + game.getScoreTeam1Game2(), team1elo, team2elo);
        int team2gain = calculateElo(20 - game.getScoreTeam1Game1() - game.getScoreTeam1Game2(), team2elo, team1elo);
        updateGame(game, team1gain);
        updatePlayer(game.getTeam1Player1(), team1gain);
        updatePlayer(game.getTeam1Player2(), team1gain);
        updatePlayer(game.getTeam2Player1(), team2gain);
        updatePlayer(game.getTeam2Player2(), team2gain);
    }

    private int teamElo(Player player1, Player player2) {
        return ((eloScorePerPlayer.getOrDefault(player1.getId(), 1000))
                + (eloScorePerPlayer.getOrDefault(player2.getId(), 1000)))
                / 2;
    }

    private int calculateElo(int scoreTeam1, int team1elo, int team2elo) {
        // https://play.eslgaming.com/archive/esl-europe/de/faq/1863/
        BigDecimal wPercent = new BigDecimal(scoreTeam1).multiply(new BigDecimal("0.05"));
        BigDecimal exponent = new BigDecimal("-1").multiply(new BigDecimal(team1elo - team2elo)).divide(new BigDecimal("400")); // -(R-Rother)/C2
        double doubleUsed = Math.pow(10, exponent.doubleValue());
        BigDecimal e = BigDecimal.ONE.divide(BigDecimal.ONE.add(BigDecimal.valueOf(doubleUsed)), MathContext.DECIMAL128); // E = 1/(1+10^(-(R-Rother)/C2))
        BigDecimal result = new BigDecimal("50").multiply(wPercent.subtract(e)); //  C1 * ( W - E )
        return result.setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    private void updateGame(Game game, int gain) {
        eloScorePerGame.put(game.getUuid(), gain);
    }

    private void updatePlayer(Player player, int gain) {
        eloScorePerPlayer.put(player.getId(), (eloScorePerPlayer.getOrDefault(player.getId(), 1000) + gain));
    }

    public String gameEloTeam1(Game game) {
        Integer elo = eloScorePerGame.get(game.getUuid());
        return elo == null ? "--" : elo.toString();
    }

    public String gameEloTeam2(Game game) {
        Integer elo = eloScorePerGame.get(game.getUuid());
        return elo == null ? "--" : String.valueOf(-1 * elo);
    }

    public JsonArray playerRanking() {
        return eloScorePerPlayer.entrySet().stream().sorted(Map.Entry.comparingByValue((a, b) -> b.compareTo(a)))
                .map(this::entryToJson)
                .collect(JsonCollectors.toJsonArray());
    }

    private JsonObject entryToJson(Map.Entry<Long, Integer> entry) {
        return Json.createObjectBuilder()
                .add("name", em.find(Player.class, entry.getKey()).getName())
                .add("elo", entry.getValue())
                .build();
    }
}
