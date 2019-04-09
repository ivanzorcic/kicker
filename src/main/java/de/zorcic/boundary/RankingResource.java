package de.zorcic.boundary;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Stateless
@Path("ranking")
public class RankingResource {

    @GET
    public JsonArray ranking() { 
        return Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("name", "Rudi")
                        .add("elo", 1337)
                        .build())
                .add(Json.createObjectBuilder()
                        .add("name", "Manfred")
                        .add("elo", 663)
                        .build())
                .build();
    }

}
