package pw.scho.battleship.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import pw.scho.battleship.model.Ship;

public class ShipMapping extends AggregateMap<Ship> {

    @Override
    public void map() {
        property().onProperty(element().getFromPosition());
        property().onProperty(element().getToPosition());
    }
}
