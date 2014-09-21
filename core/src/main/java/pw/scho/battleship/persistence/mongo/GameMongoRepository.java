package pw.scho.battleship.persistence.mongo;

import org.mongolink.MongoSession;
import pw.scho.battleship.model.Game;

public class GameMongoRepository extends MongoRepository<Game> {

    public GameMongoRepository(MongoSession mongoSession) {
        super(mongoSession);
    }

}
