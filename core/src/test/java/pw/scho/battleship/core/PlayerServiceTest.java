package pw.scho.battleship.core;

import org.junit.Before;
import org.junit.Test;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.configuration.MongoConfiguration;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;


public class PlayerServiceTest {

    private PlayerService service;

    @Before
    public void setup() {
        service = new PlayerService(new PlayerMongoRepository(MongoConfiguration.createSession()));

        PlayerMongoRepository repository = new PlayerMongoRepository(MongoConfiguration.createSession());
        repository.getSession().start();
        repository.all().forEach(repository::delete);
        repository.getSession().flush();
    }

    @Test
    public void testRegister() throws Exception {
        Player player = service.register("Toni", "secret");

        assertThat(player, is(notNullValue()));
    }

    @Test
    public void testAuthenticateWithCorrectCredentials() throws Exception {
        Player player = new Player("Toni", "secret");
        addPlayerToRepository(player);

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
        addPlayerToRepository(player);

        Player authenticatedPlayer = service.authenticate("Toni", "wrong password");

        assertThat(authenticatedPlayer, is(nullValue()));
    }

    private void addPlayerToRepository(Player player) {
        PlayerMongoRepository playerMongoRepository = new PlayerMongoRepository(MongoConfiguration.createSession());
        playerMongoRepository.getSession().start();
        playerMongoRepository.add(player);
        playerMongoRepository.getSession().stop();
    }
}