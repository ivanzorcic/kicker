package de.zorcic.boundary;

import de.zorcic.KickerException;
import de.zorcic.entity.Player;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.stream.JsonCollectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Stateless
@Path("players")
public class PlayersResource {

    @PersistenceContext
    EntityManager em;

    @POST
    public void createPlayer(String name) {
        if (name == null || name.trim().length() < 2) {
            throw new KickerException("please provide a proper name with more than two characters, not: '" + name + "'");
        }
        em.persist(new Player(name.trim()));
    }

    @GET
    public JsonArray players() {
        return em.createNamedQuery("Players.findAll", Player.class)
                .getResultList()
                .stream()
                .map(Player::toJson)
                .collect(JsonCollectors.toJsonArray());
    }

}
