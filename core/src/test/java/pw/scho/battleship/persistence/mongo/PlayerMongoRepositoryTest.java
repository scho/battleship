package pw.scho.battleship.persistence.mongo;

import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.Test;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;

import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PlayerMongoRepositoryTest {

    private MongoRepository<Player> repository;

    @Before
    public void setupRepository() throws UnknownHostException {
        MongoCollection players = MongoConfiguration.getInstance().getCollection("players_test");

        repository = new PlayerMongoRepository(players);
        repository.all().forEach(player -> repository.delete(player.getId()));
    }


    @Test
    public void testInsert() {
        Player player = new Player();
        player.setName("Toni");
        player.setPassword("secret");

        repository.insert(player);

        Player toni = repository.get(player.getId().toString());

        assertThat(toni.getName(), is("Toni"));
        assertThat(toni.getPassword(), is("secret"));
    }

    @Test
    public void testFindByName() {
        Player player = new Player();
        player.setName("Toni");

        repository.insert(player);

        Player toni = repository.findByRestriction("{ name: 'Toni'}").get(0);

        assertThat(toni.getId(), is(player.getId()));
    }
}