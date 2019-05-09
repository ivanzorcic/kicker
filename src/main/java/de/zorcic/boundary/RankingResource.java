package de.zorcic.boundary;

import de.zorcic.entity.Player;
import de.zorcic.entity.Ranking;
import java.util.Map;
import java.util.Map.Entry;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Transactional
@RequestScoped
@Path("ranking")
@Produces(MediaType.APPLICATION_JSON)
public class RankingResource {

    @Inject
    Ranking ranking;

    @PersistenceContext
    EntityManager em;

    @GET
    public JsonArray ranking() {
        return ranking.getEloScores().perPlayer()
                .sorted(Map.Entry.comparingByValue((a, b) -> b.compareTo(a)))
                .map(this::entryToJson)
                .collect(JsonCollectors.toJsonArray());
    }

    private JsonObject entryToJson(Entry<Long, Integer> entry) {
        return Json.createObjectBuilder()
                .add("name", em.find(Player.class, entry.getKey()).getName())
                .add("elo", entry.getValue())
                .build();
    }

}
