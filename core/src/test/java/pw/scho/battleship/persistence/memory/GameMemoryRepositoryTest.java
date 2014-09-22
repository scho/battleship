package pw.scho.battleship.persistence.memory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pw.scho.battleship.model.Game;
import pw.scho.battleship.model.Player;

import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class GameMemoryRepositoryTest {

    private GameMemoryRepository repository;

    @Before
    public void createGameMemoryRepository() {
        repository = new GameMemoryRepository();
    }

    @After
    public void clearCache() {
        InMemoryCache.getInstance().clear();
    }

    @Test
    public void testSetAndGet() {
        Game game = new Game();
        String id = game.getId().toString();

        repository.add(game);
        Game cachedGame = repository.get(UUID.fromString(id));

        assertThat(cachedGame, is(notNullValue()));
    }

    @Test
    public void testDelete() {
        Game cachedGame;
        Game game = new Game();
        repository.add(game);
        cachedGame = repository.get(game.getId());
        assertThat(cachedGame, is(repository.get(game.getId())));

        repository.delete(game);

        cachedGame = repository.get(game.getId());
        assertThat(cachedGame, is(nullValue()));
    }

    @Test
    public void testAll() {
        Game game = new Game();
        Game otherGame = new Game();

        repository.add(game);
        repository.add(otherGame);
        InMemoryCache.getInstance().set("anotherObjectWithDifferentType", new Object());

        Collection games = repository.all();
        assertThat(games.size(), is(2));
    }
}
