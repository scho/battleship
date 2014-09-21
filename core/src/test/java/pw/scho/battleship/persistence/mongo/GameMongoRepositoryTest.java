package pw.scho.battleship.persistence.mongo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongolink.MongoSession;
import pw.scho.battleship.model.Game;
import pw.scho.battleship.persistence.Repository;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GameMongoRepositoryTest {

    private Repository<Game> repository;
    private MongoSession session;

    @Before
    public void setupRepository() {
        session = MongoConfiguration.createSession();
        session.start();

        repository = new GameMongoRepository(session);

        repository.all().forEach(repository::delete);
        session.flush();
    }

    @After
    public void sessionClear() {
        session.stop();
    }

    @Test()
    public void testInsert() {
        Game game = new Game();
        UUID firstPlayerId = UUID.randomUUID();
        UUID secondPlayerId = UUID.randomUUID();
        game.setFirstPlayerId(firstPlayerId);
        game.setSecondPlayerId(secondPlayerId);

        repository.add(game);
        session.flush();
        session.clear();
        Game gameFromRepository = repository.get(game.getId());

        assertThat(gameFromRepository.getFirstPlayerId(), is(firstPlayerId));
        assertThat(gameFromRepository.getSecondPlayerId(), is(secondPlayerId));
    }
}