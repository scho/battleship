package pw.scho.battleship.persistence.mongo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongolink.MongoSession;
import pw.scho.battleship.model.Board;
import pw.scho.battleship.model.Game;
import pw.scho.battleship.model.Position;
import pw.scho.battleship.model.Ship;
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
    public void testSimpleInsert() {
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

    @Test()
    public void testInsertWithAssociations() {
        Game game = new Game();
        Board firstBoard = new Board();
        firstBoard.placeShip(Ship.createHorizontal(new Position(1, 1), 5));
        firstBoard.placeShip(Ship.createHorizontal(new Position(3, 3), 2));
        firstBoard.shootAt(new Position(1, 1));
        Board secondBoard = new Board();
        secondBoard.placeShip(Ship.createVertical(new Position(0, 0), 5));
        secondBoard.shootAt(new Position(0, 0));
        secondBoard.shootAt(new Position(1, 1));
        game.setFirstBoard(firstBoard);
        game.setSecondBoard(secondBoard);

        repository.add(game);
        session.flush();
        session.clear();

        Game gameFromRepository = repository.get(game.getId());
        firstBoard = gameFromRepository.getFirstBoard();
        secondBoard = gameFromRepository.getSecondBoard();
        assertThat(firstBoard.getShips().size(), is(2));
        assertThat(firstBoard.getShots().size(), is(1));
        assertThat(secondBoard.getShips().size(), is(1));
        assertThat(secondBoard.getShots().size(), is(2));
    }
}