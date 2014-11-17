package pw.scho.battleship.web.resources;

import pw.scho.battleship.core.PlayerService;
import pw.scho.battleship.core.ServiceException;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.model.PlayerInfo;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/players")
public class PlayerResource {
    private PlayerService service;

    public PlayerResource() {
        super();
        // TODO: Use DI Container
        this.service = new PlayerService(new PlayerMongoRepository(MongoConfiguration.getInstance().getCollection("players")));
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("register")
    public Response register(@FormParam("name") String name, @FormParam("password") String password) {
        if (name == null || name.isEmpty() || !service.nameAvailable(name)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Name is invalid")
                    .build();
        }

        Player player = service.register(name, password);

        return Response.ok(player.getId().toString())
                .cookie(createAuthenticationCookie(player.getId().toString()))
                .build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("login")
    public Response login(@FormParam("name") String name, @FormParam("password") String password) {
        Player player = service.authenticate(name, password);

        if (player == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authentication failed")
                    .build();
        }

        return Response.ok(player.getId().toString())
                .cookie(createAuthenticationCookie(player.getId().toString()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("info")
    public Response info(@CookieParam("playerId") String playerId) {
        try {
            Player player = service.getPlayerById(UUID.fromString(playerId));
            return Response.ok(new PlayerInfo(player)).build();
        } catch (ServiceException e) {
            return new ResponseFromServiceExceptionBuilder(e).build();
        }
    }

    @POST
    @Path("logout")
    public Response logout() {
        return Response.ok()
                .cookie(createAuthenticationCookie(""))
                .build();
    }

    private NewCookie createAuthenticationCookie(String playerId) {
        return new NewCookie("playerId",
                playerId,
                "/",
                null,
                NewCookie.DEFAULT_VERSION,
                null,
                NewCookie.DEFAULT_MAX_AGE,
                null,
                false,
                false);
    }
}
