package pw.scho.battleship.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pw.scho.battleship.model.*;
import pw.scho.battleship.persistence.Repository;
import pw.scho.battleship.persistence.memory.GameMemoryRepository;
import pw.scho.battleship.persistence.memory.InMemoryCache;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GameServiceTest {

    private Repository<Game> repository;
    private GameService service;

    @Before
    public void setupInstances() {
        repository = new GameMemoryRepository();
        service = new GameService(repository);
    }

    @After
    public void clearCache() {
        InMemoryCache.getInstance().clear();
    }

    @Test
    public void testGetAllOpenGames() {
        Player player = new Player();
        Player otherPlayer = new Player();
        Game openGame = new Game();
        openGame.setFirstPlayer(otherPlayer);
        Game ownGame = new Game();
        ownGame.setFirstPlayer(player);
        Game startedGame = new Game();
        startedGame.setFirstPlayer(otherPlayer);
        startedGame.setSecondPlayer(player);
        repository.add(openGame);
        repository.add(startedGame);
        repository.add(ownGame);

        List<LobbyGameInfo> openGames = service.getAllOpenGames(player);

        assertThat(openGames.size(), is(1));
        assertThat(openGames.get(0).getGameId(), is(openGame.getId().toString()));
    }

    @Test
    public void testJoinGame() {
        Player player = new Player();
        Player otherPlayer = new Player();
        Game openGame = new Game();
        openGame.setFirstPlayer(otherPlayer);
        repository.add(openGame);

        service.joinGame(openGame.getId(), player);

        assertThat(openGame.getSecondPlayer(), is(player));
    }

    @Test(expected = RuntimeException.class)
    public void testJoinGameWithInvalidGameIdThrowsException() {
        Player player = new Player();

        service.joinGame(UUID.randomUUID(), player);
    }

    @Test(expected = RuntimeException.class)
    public void testJoinFullGameThrowsException() {
        Player player = new Player();
        Player otherPlayer = new Player();
        Game fullGame = new Game();
        fullGame.setFirstPlayer(player);
        fullGame.setSecondPlayer(otherPlayer);
        repository.add(fullGame);

        service.joinGame(fullGame.getId(), player);
    }

    @Test
    public void testCreateGame() {
        Player player = new Player();

        Game game = service.createGame(player);

        assertThat(game.getFirstPlayer(), is(player));
        assertThat(game.getSecondPlayer(), is(nullValue()));
    }

    @Test
    public void testGetGameInfo() {
        Player player = new Player();
        Player otherPlayer = new Player();
        Game game = new Game();
        game.setFirstPlayer(player);
        game.setSecondPlayer(otherPlayer);
        repository.add(game);

        GameInfo gameInfo = service.getGameInfo(game.getId(), player);

        assertThat(gameInfo.isStarted(), is(true));
    }

    @Test
    public void testShootAt() {
        Player player = new Player();
        Player otherPlayer = new Player();
        Game game = new Game();
        game.setFirstPlayer(player);
        game.setSecondPlayer(otherPlayer);
        game.setFirstBoard(new Board());
        game.setSecondBoard(new Board());
        repository.add(game);

        service.shootAt(game.getId(), player, new Position(0, 0));

        assertThat(service.getGameInfo(game.getId(), player).isPlayersTurn(), is(false));
    }
}

