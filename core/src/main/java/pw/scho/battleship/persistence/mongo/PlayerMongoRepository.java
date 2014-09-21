package pw.scho.battleship.persistence.mongo;

import org.mongolink.MongoSession;
import pw.scho.battleship.model.Player;

public class PlayerMongoRepository extends MongoRepository<Player> {

    public PlayerMongoRepository(MongoSession mongoSession) {
        super(mongoSession);
    }

}
