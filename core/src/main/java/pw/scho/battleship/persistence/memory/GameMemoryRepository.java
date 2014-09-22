package pw.scho.battleship.persistence.memory;

import pw.scho.battleship.model.Game;

public class GameMemoryRepository extends MemoryRepository<Game> {

    public GameMemoryRepository() {
        super(game -> game.getId(), object -> object instanceof Game);
    }
}
