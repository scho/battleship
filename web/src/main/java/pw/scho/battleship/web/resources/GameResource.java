package pw.scho.battleship.web.resources;


import pw.scho.battleship.core.GameService;
import pw.scho.battleship.model.BoardPosition;
import pw.scho.battleship.model.Game;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.memory.GameMemoryRepository;
import pw.scho.battleship.web.mapping.GameInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        List<GameInfo> gameInfos = service.getAllOpenGames(player)
                .stream()
                .map(GameInfo::new)
                .collect(Collectors.toList());

        return Response.ok(gameInfos).build();
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public Response create(@CookieParam("playerId") String playerId) {
        Player player = authenticatePlayer(playerId);

        Game game = service.createGame(player);

        return Response.ok(game.getId().toString()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{gameId}/join")
    public Response join(@CookieParam("playerId") String playerId, @PathParam("gameId") String gameId) {
        Player player = authenticatePlayer(playerId);

        service.joinGame(UUID.fromString(gameId), player);

        return Response.ok(gameId).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{gameId}/playersboardpositions")
    public Response getPlayersBoardPositions(@CookieParam("playerId") String playerId, @PathParam("gameId") String gameId) {
        Player player = authenticatePlayer(playerId);

        List<List<BoardPosition>> boardPositions = service.getPlayersBoardPositions(UUID.fromString(gameId), player);

        return Response.ok(boardPositions).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{gameId}/opponentsboardpositions")
    public Response getOpponentsBoardPositions(@CookieParam("playerId") String playerId, @PathParam("gameId") String gameId) {
        Player player = authenticatePlayer(playerId);

        List<List<BoardPosition>> boardPositions = service.getOpponentsBoardPositions(UUID.fromString(gameId), player);

        return Response.ok(boardPositions).build();
    }
}
