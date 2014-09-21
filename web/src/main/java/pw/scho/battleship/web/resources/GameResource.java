package pw.scho.battleship.web.resources;


import org.mongolink.MongoSession;
import pw.scho.battleship.core.GameService;
import pw.scho.battleship.model.Game;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.mongo.GameMongoRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/games")
public class GameResource {
    private GameService service;
    private MongoSession session;

    public GameResource() {
        // TODO: Use DI Container

        session = MongoConfiguration.createSession();
        this.service = new GameService(new GameMongoRepository(session));
        session.start();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Game> register(@CookieParam("playerId") String playerId) {
        if (playerId == null || playerId.isEmpty()) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("playerId must be set");
            throw new WebApplicationException(builder.build());
        }

        return service.allGamesByPlayerId(UUID.fromString(playerId));
    }


}
