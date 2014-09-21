package pw.scho.battleship.core;

import org.mongolink.domain.criteria.Restrictions;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.Repository;

import java.util.List;
import java.util.UUID;

public class PlayerService {

    private final Repository<Player> repository;

    public PlayerService(Repository<Player> repository) {
        this.repository = repository;
    }

    public boolean nameAvailable(String name) {
        return repository.findByRestriction(Restrictions.equals("name", name)).size() == 0;
    }

    public Player register(String name, String password) {
        if (!nameAvailable(name)) {
            throw new RuntimeException("Name already taken");
        }
        Player player = new Player(name, password);

        repository.add(player);

        return player;
    }

    public Player authenticate(String name, String password) {
        List<Player> players = repository.findByRestriction(Restrictions.equals("name", name));

        if (players.size() != 1) {
            return null;
        }

        Player player = players.get(0);
        if (!player.getPassword().equals(password)) {
            return null;
        }

        return player;
    }

    public Player get(UUID playerId) {
        return repository.get(playerId);
    }
}
