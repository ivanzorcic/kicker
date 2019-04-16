package de.zorcic;

import javax.json.Json;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class KickerException extends WebApplicationException {

    public KickerException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                        .add("kickerException", message)
                        .build())
                .build());
    }
}
