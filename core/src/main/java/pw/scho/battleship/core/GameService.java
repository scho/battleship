package pw.scho.battleship.core;

import org.mongolink.domain.criteria.Restriction;
import org.mongolink.domain.criteria.Restrictions;
import pw.scho.battleship.model.BoardRandomizer;
import pw.scho.battleship.model.Game;
import pw.scho.battleship.persistence.Repository;

import java.util.List;
import java.util.UUID;

public class GameService {

    private final Repository<Game> repository;

    public GameService(Repository<Game> repository) {
        this.repository = repository;
    }

    public Game createGame(UUID playerId) {
        Game game = new Game();
        BoardRandomizer boardRandomizer = new BoardRandomizer();
        game.setFirstPlayerId(playerId);
        game.setFirstBoard(boardRandomizer.randomizeWithStandardShips());
        game.setSecondBoard(boardRandomizer.randomizeWithStandardShips());

        repository.add(game);

        return game;
    }

    public void joinGame(UUID gameId, UUID playerId) {
        List<Game> games = repository.findByRestriction(Restrictions.equals("id", gameId));

        if (games.size() != 1) {
            throw new RuntimeException("Cannot find your game");
        }

        Game game = games.get(0);
        game.setSecondPlayerId(playerId);


        repository.add(game);
    }

    public List<Game> allGamesByPlayerId(UUID playerId) {
        Restriction restriction = Restrictions.or()
                .with(Restrictions.equals("firstPlayerId", playerId))
                .with(Restrictions.equals("secondPlayerId", playerId));
        List<Game> games = repository.findByRestriction(restriction);

        return games;
    }
}
