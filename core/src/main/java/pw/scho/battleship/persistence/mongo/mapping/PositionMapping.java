package pw.scho.battleship.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import pw.scho.battleship.model.Position;

public class PositionMapping extends AggregateMap<Position> {

    @Override
    public void map() {
        property().onProperty(element().getX());
        property().onProperty(element().getY());
    }
}
