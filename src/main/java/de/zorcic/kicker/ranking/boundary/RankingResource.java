package de.zorcic.kicker.ranking.boundary;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.zorcic.kicker.ranking.entity.Ranking;

@Transactional
@RequestScoped
@Path("ranking")
@Produces(MediaType.APPLICATION_JSON)
public class RankingResource {

    @Inject
    Ranking ranking;

    @GET
    public JsonArray ranking() {
        return ranking.playerRanking();
    }

}
