package pw.scho.battleship.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import pw.scho.battleship.model.Board;

public class BoardMapping extends AggregateMap<Board> {

    @Override
    public void map() {
        property().onProperty(element().getShips());
        property().onProperty(element().getShots());
    }
}
