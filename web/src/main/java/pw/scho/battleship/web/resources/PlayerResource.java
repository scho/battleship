package pw.scho.battleship.web.resources;

import org.mongolink.MongoSession;
import pw.scho.battleship.core.PlayerService;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;
import pw.scho.battleship.web.mapping.PlayerDto;
import pw.scho.battleship.web.mapping.PlayerMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/players")
public class PlayerResource {
    private PlayerService service;
    private MongoSession session;

    public PlayerResource() {
        // TODO: Use DI Container
        session = MongoConfiguration.createSession();
        this.service = new PlayerService(new PlayerMongoRepository(session));
        session.start();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response register(@FormParam("name") String name, @FormParam("password") String password) {
        if (name == null || name.isEmpty() || !service.nameAvailable(name)) {
            Response.ResponseBuilder builder = Response.status(Response.Status.CONFLICT);
            builder.entity("Name is invalid");
            throw new WebApplicationException(builder.build());
        }

        Player player = service.register(name, password);
        session.stop();

        return Response.ok(PlayerMapper.map(player))
                .cookie(new NewCookie("playerId", player.getId().toString()))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(@FormParam("name") String name, @FormParam("password") String password) {
        Player player = service.authenticate(name, password);

        if (player == null) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("Authentication failed");
            throw new WebApplicationException(builder.build());
        }

        return Response.ok(PlayerMapper.map(player))
                .cookie(new NewCookie("playerId", player.getId().toString()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("info")
    public PlayerDto register(@CookieParam("playerId") String playerId) {
        if (playerId == null || playerId.isEmpty()) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("playerId must be set");
            throw new WebApplicationException(builder.build());
        }

        return PlayerMapper.map(service.get(UUID.fromString(playerId)));
    }


    @POST
    @Path("logout")
    public Response logout() {
        return Response.ok()
                .cookie(new NewCookie("playerId", ""))
                .build();
    }
}
