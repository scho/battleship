package pw.scho.battleship.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import pw.scho.battleship.model.Game;

public class GameMapping extends AggregateMap<Game> {

    @Override
    public void map() {
        id().onProperty(element().getId()).natural();
        property().onProperty(element().getFirstBoard());
        property().onProperty(element().getSecondBoard());
        property().onProperty(element().getSecondPlayerId());
        property().onProperty(element().getFirstPlayerId());
    }
}
