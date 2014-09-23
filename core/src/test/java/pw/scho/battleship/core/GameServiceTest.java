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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

public class GameServiceTest {

    private Repository<Game> gameRepository;
    private GameService service;

    @Before
    public void setupInstances() {
        gameRepository = new GameMemoryRepository();
        service = new GameService(gameRepository, new PlayerMongoRepository(MongoConfiguration.createSession()));
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
        gameRepository.add(openGame);
        gameRepository.add(startedGame);
        gameRepository.add(ownGame);

        List<LobbyGameInfo> openGames = service.getAllOpenGames(player.getId());

        assertThat(openGames.size(), is(1));
        assertThat(openGames.get(0).getGameId(), is(openGame.getId().toString()));
    }

    @Test
    public void testJoinGame() throws ServiceException {
        Player player = new Player();
        addPlayerToRepository(player);
        Player otherPlayer = new Player();
        Game openGame = new Game();
        openGame.setFirstPlayer(otherPlayer);
        gameRepository.add(openGame);

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
        gameRepository.add(fullGame);

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
        gameRepository.add(game);

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
        gameRepository.add(game);

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
        gameRepository.add(game);

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
        gameRepository.add(game);

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
        gameRepository.add(game);

        service.shootAt(game.getId(), player.getId(), new Position(0, 0));
        List<String> playerMessages = service.getMessages(game.getId(), player.getId());

        assertThat(playerMessages.size(), is(1));
    }

    private void addPlayerToRepository(Player player) {
        PlayerMongoRepository playerMongoRepository = new PlayerMongoRepository(MongoConfiguration.createSession());
        playerMongoRepository.getSession().start();
        playerMongoRepository.add(player);
        playerMongoRepository.getSession().stop();
    }

    private Player getPlayerFromRepository(UUID playerId) {
        PlayerMongoRepository playerMongoRepository = new PlayerMongoRepository(MongoConfiguration.createSession());
        playerMongoRepository.getSession().start();
        return playerMongoRepository.get(playerId);
    }
}

