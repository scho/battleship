package pw.scho.battleship.web.resources;

import org.mongolink.MongoSession;
import pw.scho.battleship.core.PlayerService;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.UUID;

public abstract class AuthenticatedResource {

    private PlayerService service;

    public AuthenticatedResource() {
        MongoSession session = MongoConfiguration.createSession();
        this.service = new PlayerService(new PlayerMongoRepository(session));
        session.start();
    }

    public Player authenticatePlayer(String playerId) {
        if (playerId != null && !playerId.isEmpty()) {
            Player player = service.get(UUID.fromString(playerId));
            if (player != null) {
                return player;
            }
        }

        throw new WebApplicationException(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authorization failed")
                        .build()
        );
    }
}
