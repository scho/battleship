package pw.scho.battleship.persistence.mongo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Restrictions;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.Repository;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PlayerMongoRepositoryTest {

    private Repository<Player> repository;
    private MongoSession session;

    @Before
    public void setupRepository() {
        session = MongoConfiguration.createSession();
        session.start();

        repository = new PlayerMongoRepository(session);

        repository.all().forEach(repository::delete);
        session.flush();
    }

    @After
    public void sessionClear() {
        session.stop();
    }

    @Test
    public void testInsert() {
        Player player = new Player();
        player.setName("Toni");

        repository.add(player);
        session.flush();
        session.clear();
        Player toni = repository.get(player.getId());

        assertThat(toni.getName(), is("Toni"));
    }

    @Test
    public void testFindByName() {
        Player player = new Player();
        player.setName("Toni");

        repository.add(player);
        session.flush();
        session.clear();
        Player toni = repository.findByRestriction(Restrictions.equals("name", "Toni")).get(0);

        assertThat(toni.getId(), is(player.getId()));
    }
}