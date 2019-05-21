package de.zorcic.kicker.players.boundary;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import de.zorcic.kicker.KickerException;
import de.zorcic.kicker.players.entity.Player;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Transactional
@RequestScoped
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<Player> players() {
        return em.createNamedQuery("Players.findAll", Player.class)
                .getResultList();
    }

}
