package pw.scho.battleship.web.resources;


import pw.scho.battleship.core.GameService;
import pw.scho.battleship.model.*;
import pw.scho.battleship.persistence.memory.GameMemoryRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/games")
public class GameResource extends AuthenticatedResource {
    private GameService service;

    public GameResource() {
        // TODO: Use DI Container
        this.service = new GameService(new GameMemoryRepository());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index(@CookieParam("playerId") String playerId) {
        Player player = authenticatePlayer(playerId);

        List<LobbyGameInfo> lobbyGameInfos = service.getAllOpenGames(player);

        return Response.ok(lobbyGameInfos).build();
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/open")
    public Response open(@CookieParam("playerId") String playerId) {
        Player player = authenticatePlayer(playerId);

        Game game = service.createGame(player);

        return Response.ok(game.getId().toString()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{gameId}/join")
    public Response join(@CookieParam("playerId") String playerId,
                         @PathParam("gameId") String gameId) {
        Player player = authenticatePlayer(playerId);

        service.joinGame(UUID.fromString(gameId), player);

        return Response.ok(gameId).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{gameId}/playersboardpositions")
    public Response getPlayersBoardPositions(@CookieParam("playerId") String playerId,
                                             @PathParam("gameId") String gameId) {
        Player player = authenticatePlayer(playerId);

        List<List<BoardPosition>> boardPositions = service.getPlayersBoardPositions(UUID.fromString(gameId), player);

        return Response.ok(boardPositions).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{gameId}/opponentsboardpositions")
    public Response getOpponentsBoardPositions(@CookieParam("playerId") String playerId,
                                               @PathParam("gameId") String gameId) {
        Player player = authenticatePlayer(playerId);

        List<List<BoardPosition>> boardPositions = service.getOpponentsBoardPositions(UUID.fromString(gameId), player);

        return Response.ok(boardPositions).build();
    }

    @POST
    @Path("/{gameId}/shootat/{x}-{y}")
    public Response shootAt(@CookieParam("playerId") String playerId,
                            @PathParam("gameId") String gameId,
                            @PathParam("x") int x,
                            @PathParam("y") int y) {
        Player player = authenticatePlayer(playerId);

        try {
            service.shootAt(UUID.fromString(gameId), player, new Position(x, y));
            return Response.ok().build();
        } catch (GameException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/{gameId}/state")
    @Produces(MediaType.APPLICATION_JSON)
    public Response state(@CookieParam("playerId") String playerId,
                         @PathParam("gameId") String gameId) {
        Player player = authenticatePlayer(playerId);

        GameState gameState = service.getGameInfo(UUID.fromString(gameId), player);
        return Response.ok(gameState).build();
    }
}
