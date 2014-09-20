package pw.scho.battleship.core.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import pw.scho.battleship.model.Game;

public class GameMapping extends AggregateMap<Game> {

    @Override
    public void map() {
        id().onProperty(element()).natural();
        property().onField("name");
        property().onProperty(element());
    }
}
