package de.zorcic.entity;

import de.zorcic.KickerException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.LongStream;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Game.findAllSorted", query = "SELECT g from Game as g ORDER BY g.addedAt ASC")
public class Game {

    @Id
    private UUID uuid;
    @ManyToOne
    private Player team1Player1;
    @ManyToOne
    private Player team1Player2;
    @ManyToOne
    private Player team2Player1;
    @ManyToOne
    private Player team2Player2;
    private int scoreTeam1Game1;
    private int scoreTeam1Game2;
    private LocalDateTime addedAt;

    public Game(Player team1Player1, Player team1Player2, Player team2Player1, Player team2Player2, int scoreTeam1Game1, int scoreTeam1Game2) {
        this.team1Player1 = team1Player1;
        this.team1Player2 = team1Player2;
        this.team2Player1 = team2Player1;
        this.team2Player2 = team2Player2;
        this.scoreTeam1Game1 = scoreTeam1Game1;
        this.scoreTeam1Game2 = scoreTeam1Game2;
        this.uuid = UUID.randomUUID();
        this.addedAt = LocalDateTime.now();
        this.validate();
    }

    protected Game() {
    }

    private void validate() {
        if (LongStream.of(team1Player1.getId(), team1Player2.getId(), team2Player1.getId(), team2Player2.getId()).distinct().count() != 4) {
            throw new KickerException("players should be unique: " + team1Player1.getName() + " " + team1Player2.getName() + " " + team2Player1.getName() + " " + team2Player2.getName());
        }
        if (scoreTeam1Game1 > 10 || scoreTeam1Game2 > 10 || scoreTeam1Game1 < 0 || scoreTeam1Game2 < 0) {
            throw new KickerException("score should be between 0 and 10, but is: " + scoreTeam1Game1 + " and " + scoreTeam1Game2);
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getTeam1Player1() {
        return team1Player1;
    }

    public Player getTeam1Player2() {
        return team1Player2;
    }

    public Player getTeam2Player1() {
        return team2Player1;
    }

    public Player getTeam2Player2() {
        return team2Player2;
    }

    public int getScoreTeam1Game1() {
        return scoreTeam1Game1;
    }

    public int getScoreTeam1Game2() {
        return scoreTeam1Game2;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

}
