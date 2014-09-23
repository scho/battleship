package pw.scho.battleship.web.resources;


import pw.scho.battleship.core.GameService;
import pw.scho.battleship.core.ServiceException;
import pw.scho.battleship.model.*;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.memory.GameMemoryRepository;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/games")
public class GameResource {
    private GameService service;

    public GameResource() {
        // TODO: Use DI Container
        this.service = new GameService(new GameMemoryRepository(), new PlayerMongoRepository(MongoConfiguration.createSession()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index(@CookieParam("playerId") String playerId) {
        try {
            List<LobbyGameInfo> lobbyGameInfos = service.getAllOpenGames(UUID.fromString(playerId));

            return Response.ok(lobbyGameInfos).build();
        } catch (ServiceException e) {
            return createResponseFromServiceException(e);
        }
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/open")
    public Response open(@CookieParam("playerId") String playerId) {
        try {
            Game game = service.openGame(UUID.fromString(playerId));
            return Response.ok(game.getId().toString()).build();
        } catch (ServiceException e) {
            return createResponseFromServiceException(e);
        }
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{gameId}/join")
    public Response join(@CookieParam("playerId") String playerId,
                         @PathParam("gameId") String gameId) {

        try {
            service.joinGame(UUID.fromString(gameId), UUID.fromString(playerId));
            return Response.ok(gameId).build();
        } catch (ServiceException e) {
            return createResponseFromServiceException(e);
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{gameId}/playersboardpositions")
    public Response getPlayersBoardPositions(@CookieParam("playerId") String playerId,
                                             @PathParam("gameId") String gameId) {
        try {
            List<List<BoardPosition>> boardPositions = service.getPlayersBoardPositions(UUID.fromString(gameId), UUID.fromString(playerId));
            return Response.ok(boardPositions).build();
        } catch (ServiceException e) {
            return createResponseFromServiceException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{gameId}/opponentsboardpositions")
    public Response getOpponentsBoardPositions(@CookieParam("playerId") String playerId,
                                               @PathParam("gameId") String gameId) {
        try {
            List<List<BoardPosition>> boardPositions = service.getOpponentsBoardPositions(UUID.fromString(gameId), UUID.fromString(playerId));
            return Response.ok(boardPositions).build();
        } catch (ServiceException e) {
            return createResponseFromServiceException(e);
        }
    }

    @POST
    @Path("/{gameId}/shootat/{x}-{y}")
    public Response shootAt(@CookieParam("playerId") String playerId,
                            @PathParam("gameId") String gameId,
                            @PathParam("x") int x,
                            @PathParam("y") int y) {

        try {
            service.shootAt(UUID.fromString(gameId), UUID.fromString(playerId), new Position(x, y));
            return Response.ok().build();
        } catch (ServiceException e) {
            return createResponseFromServiceException(e);
        }
    }

    @GET
    @Path("/{gameId}/state")
    @Produces(MediaType.APPLICATION_JSON)
    public Response state(@CookieParam("playerId") String playerId,
                          @PathParam("gameId") String gameId) {
        try {
            GameState gameState = service.getGameInfo(UUID.fromString(gameId), UUID.fromString(playerId));
            return Response.ok(gameState).build();
        } catch (ServiceException e) {
            return createResponseFromServiceException(e);
        }
    }

    private Response createResponseFromServiceException(ServiceException exception) {
        Response.Status status = null;
        switch (exception.getKind()) {
            case INVALID_ACTION:
                status = Response.Status.CONFLICT;
                break;
            case NOT_FOUND:
                status = Response.Status.NOT_FOUND;
                break;
            case UNAUTHORIZED:
                status = Response.Status.UNAUTHORIZED;
                break;
        }
        return Response.status(status).build();
    }
}
