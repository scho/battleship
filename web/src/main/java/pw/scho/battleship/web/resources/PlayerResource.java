package pw.scho.battleship.web.resources;


import pw.scho.battleship.core.PlayerService;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/player")
public class PlayerResource {
    private PlayerService service;

    public PlayerResource() {
        // TODO: Use DI Container

        this.service = new PlayerService(new PlayerMongoRepository(MongoConfiguration.createSession()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("show")
    public Player show() {
        return new Player("Georg", "secret");
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register/{name}/{password}")
    public Player register(@PathParam("name") String name, @PathParam("password") String password) {
        if (service.nameAvailable(name)) {
            return null;
        }
        return service.register(name, password);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("authenticate/{name}/{password}")
    public Player authenticate(@PathParam("name") String name, @PathParam("password") String password) {
        Player player = service.authenticate(name, password);

        if (player != null) {
            return player;
        }

        return null;
    }
}
