package de.zorcic.kicker.games.boundary;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import de.zorcic.kicker.games.entity.Game;
import de.zorcic.kicker.games.entity.NewGameEvent;
import de.zorcic.kicker.players.entity.Player;
import de.zorcic.kicker.ranking.entity.Ranking;

@Transactional
@RequestScoped
@Path("games")
public class GamesResource {

    @Inject
    Event<NewGameEvent> cdiEvent;

    @PersistenceContext
    EntityManager em;

    @Inject
    Ranking ranking;

    @POST
    public void createGame(JsonObject game) {
        long t1p1 = game.getJsonNumber("team1Player1").longValue();
        long t1p2 = game.getJsonNumber("team1Player2").longValue();
        long t2p1 = game.getJsonNumber("team2Player1").longValue();
        long t2p2 = game.getJsonNumber("team2Player2").longValue();
        int team1ScoreGame1 = game.getInt("team1ScoreGame1");
        int team1ScoreGame2 = game.getInt("team1ScoreGame2");
        Game newGame = new Game(em.getReference(Player.class, t1p1), em.getReference(Player.class, t1p2), em.getReference(Player.class, t2p1), em.getReference(Player.class, t2p2), team1ScoreGame1, team1ScoreGame2);
        em.persist(newGame);
        cdiEvent.fire(new NewGameEvent(newGame.getUuid()));
    }

    @GET
    public JsonArray games() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        em.createNamedQuery("Game.findAllSorted", Game.class)
                .getResultList()
                .stream()
                .map(game -> game.toJson(ranking.gameEloTeam1(game), ranking.gameEloTeam2(game)))
                .forEach(g -> jsonArrayBuilder.add(0, g)); // reverse order
        return jsonArrayBuilder.build();
    }
}
