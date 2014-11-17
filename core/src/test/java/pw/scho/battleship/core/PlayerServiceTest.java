package pw.scho.battleship.core;

import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.Test;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class PlayerServiceTest {

    private PlayerService service;

    private PlayerMongoRepository repository;

    @Before
    public void setup() {

        MongoCollection players = MongoConfiguration.getInstance().getCollection("players_test");

        repository = new PlayerMongoRepository(players);
        repository.all().forEach(player -> repository.delete(player.getId()));

        service = new PlayerService(repository);
    }

    @Test
    public void testRegister() throws Exception {
        Player player = service.register("Toni", "secret");

        assertThat(player, is(notNullValue()));
    }

    @Test
    public void testAuthenticateWithCorrectCredentials() throws Exception {
        Player player = new Player("Toni", "secret");
        insertPlayerToRepository(player);

        Player authenticatedPlayer = service.authenticate("Toni", "secret");

        assertThat(authenticatedPlayer.getId(), is(player.getId()));
    }

    @Test
    public void testAuthenticateWithIncorrectName() throws Exception {
        Player authenticatedPlayer = service.authenticate("Somebody", "secret");

        assertThat(authenticatedPlayer, is(nullValue()));
    }

    @Test
    public void testAuthenticateWithIncorrectPassword() throws Exception {
        Player player = new Player("Toni", "secret");
        insertPlayerToRepository(player);

        Player authenticatedPlayer = service.authenticate("Toni", "wrong password");

        assertThat(authenticatedPlayer, is(nullValue()));
    }

    private void insertPlayerToRepository(Player player) {
        repository.insert(player);
    }
}