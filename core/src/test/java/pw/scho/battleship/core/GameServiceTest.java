package pw.scho.battleship.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pw.scho.battleship.model.*;
import pw.scho.battleship.persistence.Repository;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.memory.GameMemoryRepository;
import pw.scho.battleship.persistence.memory.InMemoryCache;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

public class GameServiceTest {

    private Repository<Game> gameRepository;
    private PlayerMongoRepository playerRepository;
    private GameService service;

    @Before
    public void setupInstances() {
        gameRepository = new GameMemoryRepository();
        playerRepository = new PlayerMongoRepository(MongoConfiguration.getInstance().getCollection("players_test"));
        service = new GameService(gameRepository, playerRepository);

        playerRepository.all().forEach(player -> playerRepository.delete(player.getId()));
    }

    @After
    public void clearCache() {
        InMemoryCache.getInstance().clear();
    }

    @Test
    public void testGetAllOpenGames() throws ServiceException {
        Player player = new Player();
        Player otherPlayer = new Player();
        addPlayerToRepository(player);
        Game openGame = new Game();
        openGame.setFirstPlayer(otherPlayer);
        Game ownGame = new Game();
        ownGame.setFirstPlayer(player);
        Game startedGame = new Game();
        startedGame.setFirstPlayer(otherPlayer);
        startedGame.setSecondPlayer(player);
        gameRepository.insert(openGame);
        gameRepository.insert(startedGame);
        gameRepository.insert(ownGame);

        List<LobbyGameInfo> openGames = service.getAllOpenGames(player.getId());

        assertThat(openGames.size(), is(1));
        assertThat(openGames.get(0).getGameId(), is(openGame.getId().toString()));
    }

    @Test
    public void testGetAllOwnGames() throws ServiceException {
        Player player = new Player();
        Player otherPlayer = new Player();
        addPlayerToRepository(player);
        Game openGame = new Game();
        openGame.setFirstPlayer(new Player());
        Game ownGame = new Game();
        ownGame.setFirstPlayer(player);
        Game startedGame = new Game();
        startedGame.setFirstPlayer(player);
        startedGame.setSecondPlayer(otherPlayer);
        Game finishedGame = new Game();
        finishedGame.setFirstPlayer(player);
        finishedGame.setSecondPlayer(otherPlayer);
        finishedGame.setFinished(true);
        gameRepository.insert(openGame);
        gameRepository.insert(startedGame);
        gameRepository.insert(ownGame);
        gameRepository.insert(finishedGame);

        List<LobbyGameInfo> ownGames = service.getAllOwnAndOngoingGames(player.getId());

        List<String> gameIds = ownGames.stream()
                .map(game -> game.getGameId().toString())
                .collect(Collectors.toList());

        assertThat(gameIds.size(), is(2));
        assertThat(gameIds, hasItem(startedGame.getId().toString()));
        assertThat(gameIds, hasItem(ownGame.getId().toString()));
    }

    @Test
    public void testJoinGame() throws ServiceException {
        Player player = new Player();
        addPlayerToRepository(player);
        Player otherPlayer = new Player();
        Game openGame = new Game();
        openGame.setFirstPlayer(otherPlayer);
        gameRepository.insert(openGame);

        service.joinGame(openGame.getId(), player.getId());

        assertThat(openGame.getSecondPlayer().getId(), is(player.getId()));
    }

    @Test
    public void testJoinGameWithInvalidGameIdThrowsException() throws ServiceException {
        Player player = new Player();
        addPlayerToRepository(player);

        try {
            service.joinGame(UUID.randomUUID(), player.getId());
            fail();
        } catch (ServiceException e) {
            assertThat(e.getKind(), is(ServiceException.Kind.NOT_FOUND));
        }
    }

    @Test
    public void testJoinFullGameThrowsException() {
        Player player = new Player();
        addPlayerToRepository(player);
        Player otherPlayer = new Player();
        Game fullGame = new Game();
        fullGame.setFirstPlayer(player);
        fullGame.setSecondPlayer(otherPlayer);
        gameRepository.insert(fullGame);

        try {
            service.joinGame(fullGame.getId(), player.getId());
            fail();
        } catch (ServiceException e) {
            assertThat(e.getKind(), is(ServiceException.Kind.INVALID_ACTION));
        }
    }

    @Test
    public void testOpenGame() throws ServiceException {
        Player player = new Player();
        addPlayerToRepository(player);

        Game game = service.openGame(player.getId());

        assertThat(game.getFirstPlayer().getId(), is(player.getId()));
        assertThat(game.getSecondPlayer(), is(nullValue()));
    }

    @Test
    public void testGetGameInfo() throws ServiceException {
        Player player = new Player();
        addPlayerToRepository(player);
        Player otherPlayer = new Player();
        Game game = new Game();
        game.setFirstPlayer(player);
        game.setSecondPlayer(otherPlayer);
        gameRepository.insert(game);

        GameState gameState = service.getGameInfo(game.getId(), player.getId());

        assertThat(gameState.isStarted(), is(true));
    }

    @Test
    public void testShootAt() throws ServiceException {
        Player player = new Player();
        addPlayerToRepository(player);
        Player otherPlayer = new Player();
        Game game = new Game();
        game.setFirstPlayer(player);
        game.setSecondPlayer(otherPlayer);
        game.setFirstBoard(new Board());
        game.setSecondBoard(new Board());
        game.getSecondBoard().placeShip(Ship.createHorizontal(new Position(0, 0), 5));
        gameRepository.insert(game);

        service.shootAt(game.getId(), player.getId(), new Position(0, 0));

        assertThat(service.getGameInfo(game.getId(), player.getId()).isPlayersTurn(), is(false));
    }

    @Test
    public void testShootAtUpdatesPlayersStats() throws ServiceException {
        Player player = new Player();
        Player otherPlayer = new Player();

        addPlayerToRepository(player);
        addPlayerToRepository(otherPlayer);

        Game game = new Game();
        game.setFirstPlayer(player);
        game.setSecondPlayer(otherPlayer);
        game.setFirstBoard(new Board());
        game.setSecondBoard(new Board());
        game.getSecondBoard().placeShip(Ship.createHorizontal(new Position(0, 0), 1));
        gameRepository.insert(game);

        service.shootAt(game.getId(), player.getId(), new Position(0, 0));

        player = getPlayerFromRepository(player.getId());
        otherPlayer = getPlayerFromRepository(otherPlayer.getId());
        assertThat(player.getGamesWon(), is(1));
        assertThat(player.getGamesLost(), is(0));
        assertThat(otherPlayer.getGamesWon(), is(0));
        assertThat(otherPlayer.getGamesLost(), is(1));
    }

    @Test(expected = ServiceException.class)
    public void testGetGameInfoForSomeoneElesGameThrowsException() throws ServiceException {
        Player player = new Player();
        addPlayerToRepository(player);
        Player otherPlayer = new Player();
        Player yetAnotherPlayer = new Player();
        Game game = new Game();
        game.setFirstPlayer(otherPlayer);
        game.setSecondPlayer(yetAnotherPlayer);
        gameRepository.insert(game);

        service.getGameInfo(game.getId(), player.getId());
    }

    @Test
    public void testMessageQueue() throws ServiceException {
        Player player = new Player();
        Player otherPlayer = new Player();
        addPlayerToRepository(player);
        addPlayerToRepository(otherPlayer);
        Game game = new Game();
        game.setFirstPlayer(player);
        game.setSecondPlayer(otherPlayer);
        game.setFirstBoard(new Board());
        game.setSecondBoard(new Board());
        game.getSecondBoard().placeShip(Ship.createHorizontal(new Position(0, 0), 1));
        gameRepository.insert(game);

        service.shootAt(game.getId(), player.getId(), new Position(0, 0));
        List<String> playerMessages = service.getMessages(game.getId(), player.getId());

        assertThat(playerMessages.size(), is(1));
    }

    private void addPlayerToRepository(Player player) {
        playerRepository.insert(player);
    }

    private Player getPlayerFromRepository(UUID playerId) {
        return playerRepository.get(playerId);
    }
}

