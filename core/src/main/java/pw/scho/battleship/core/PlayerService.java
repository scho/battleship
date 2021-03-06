package pw.scho.battleship.core;

import pw.scho.battleship.model.Player;
import pw.scho.battleship.persistence.mongo.MongoRepository;

import java.util.List;
import java.util.UUID;

public class PlayerService {

    // Use MongoRepository as type here and not Repository<Player> because we need restrictions
    private final MongoRepository<Player> repository;

    public PlayerService(MongoRepository<Player> repository) {
        this.repository = repository;
    }

    public boolean nameAvailable(String name) {
        return repository.findByRestriction("{ name: '" + name + "' }").size() == 0;
    }

    public Player register(String name, String password) {
        if (!nameAvailable(name)) {
            throw new RuntimeException("Name already taken");
        }
        Player player = new Player(name, password);

        repository.insert(player);
        return player;
    }

    public Player authenticate(String name, String password) {
        List<Player> players = repository.findByRestriction("{ name: '" + name + "' }");

        if (players.size() != 1) {
            return null;
        }

        Player player = players.get(0);
        if (!player.getPassword().equals(password)) {
            return null;
        }

        return player;
    }

    public Player getPlayerById(UUID playerId) throws ServiceException {
        Player player = repository.get(playerId.toString());
        if (player != null) {
            return player;
        }

        throw ServiceException.createUnauthorized();
    }
}
