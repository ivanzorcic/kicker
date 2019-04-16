package de.zorcic.entity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class EloScores {

    ConcurrentMap<Long, Integer> perPlayer = new ConcurrentHashMap<>();
    ConcurrentMap<UUID, Integer> perGame = new ConcurrentHashMap<>();

    public void updateRanking(Game game) {
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

    private void updateGame(Game game, int gain) {
        perGame.put(game.getUuid(), gain);
    }

    private void updatePlayer(Player player, int gain) {
        perPlayer.put(player.getId(), (perPlayer.getOrDefault(player.getId(), 1000) + gain));
    }

    private int teamElo(Player player1, Player player2) {
        return ((perPlayer.getOrDefault(player1.getId(), 1000))
                + (perPlayer.getOrDefault(player2.getId(), 1000)))
                / 2;
    }

    public Stream<Map.Entry<Long, Integer>> perPlayer() {
        return perPlayer.entrySet().stream();
    }

    public String gameEloTeam1(Game game) {
        Integer elo = perGame.get(game.getUuid());
        return elo == null ? "--" : elo.toString();
    }

    public String gameEloTeam2(Game game) {
        Integer elo = perGame.get(game.getUuid());
        return elo == null ? "--" : String.valueOf(-1 * elo);
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

    public static void main(String... args) {
        EloScores test = new EloScores();
        System.out.println(test.calculateElo(20, 1000, 1000));
        System.out.println(test.calculateElo(0, 1000, 1000));
        System.out.println(test.calculateElo(20, 900, 1200));
        System.out.println(test.calculateElo(0, 1200, 900));
        System.out.println(test.calculateElo(20, 1200, 900));
        System.out.println(test.calculateElo(0, 900, 1200));
        System.out.println(test.calculateElo(10, 1000, 1000));
        System.out.println(test.calculateElo(10, 800, 800));
        System.out.println(test.calculateElo(10, 1200, 1200));
        System.out.println(test.calculateElo(13, 1000, 1000));
        System.out.println(test.calculateElo(7, 1000, 1000));
    }

}
