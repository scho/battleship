package pw.scho.battleship.web.resources;

import org.mongolink.MongoSession;
import pw.scho.battleship.core.PlayerService;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.model.PlayerInfo;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/players")
public class PlayerResource extends AuthenticatedResource {
    private PlayerService service;
    private MongoSession session;

    public PlayerResource() {
        super();
        // TODO: Use DI Container
        session = MongoConfiguration.createSession();
        session.start();
        this.service = new PlayerService(new PlayerMongoRepository(session));
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("register")
    public Response register(@FormParam("name") String name, @FormParam("password") String password) {
        if (name == null || name.isEmpty() || !service.nameAvailable(name)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.CONFLICT)
                            .entity("Name is invalid")
                            .build()
            );
        }

        try {
            Player player = service.register(name, password);

            return Response.ok()
                    .cookie(createAuthenticationCookie(player.getId().toString()))
                    .build();
        } finally {
            session.stop();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("login")
    public Response login(@FormParam("name") String name, @FormParam("password") String password) {
        Player player = service.authenticate(name, password);

        if (player == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Authentication failed")
                            .build()
            );
        }

        return Response.ok()
                .cookie(createAuthenticationCookie(player.getId().toString()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("info")
    public Response info(@CookieParam("playerId") String playerId) {
        Player player = authenticatePlayer(playerId);

        return Response.ok(new PlayerInfo(player)).build();
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
