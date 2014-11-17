package pw.scho.battleship.persistence.mongo;

import org.jongo.MongoCollection;
import pw.scho.battleship.model.Player;

public class PlayerMongoRepository extends MongoRepository<Player> {

    public PlayerMongoRepository(MongoCollection collection) {
        super(collection);
    }
}
