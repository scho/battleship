package pw.scho.battleship.persistence.mongo.mapping;

import org.mongolink.domain.mapper.AggregateMap;
import pw.scho.battleship.model.Player;

public class PlayerMapping extends AggregateMap<Player> {

    @Override
    public void map() {
        id().onProperty(element().getId()).natural();
        property().onProperty(element().getName());
        property().onProperty(element().getGamesLost());
        property().onProperty(element().getGamesWon());
    }
}
