package pw.scho.battleship.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import pw.scho.battleship.model.Board;

public class BoardMapping extends AggregateMap<Board> {

    @Override
    public void map() {
        collection().onProperty(element().getShips());
        collection().onProperty(element().getShots());
    }
}
