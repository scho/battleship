package pw.scho.battleship.persistence.mongo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Restrictions;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PlayerMongoRepositoryTest {

    private MongoRepository<Player> repository;

    @Before
    public void setupRepository() {
        repository = new PlayerMongoRepository(MongoConfiguration.createSession());
        repository.getSession().start();
        repository.all().forEach(repository::delete);
        repository.getSession().flush();
    }

    @After
    public void sessionClear() {
        repository.getSession().stop();
    }

    @Test
    public void testInsert() {
        Player player = new Player();
        player.setName("Toni");
        player.setPassword("secret");

        repository.add(player);
        repository.getSession().flush();
        repository.getSession().clear();
        Player toni = repository.get(player.getId());

        assertThat(toni.getName(), is("Toni"));
        assertThat(toni.getPassword(), is("secret"));
    }

    @Test
    public void testFindByName() {
        Player player = new Player();
        player.setName("Toni");

        repository.add(player);
        repository.getSession().flush();
        repository.getSession().clear();
        Player toni = repository.findByRestriction(Restrictions.equals("name", "Toni")).get(0);

        assertThat(toni.getId(), is(player.getId()));
    }
}