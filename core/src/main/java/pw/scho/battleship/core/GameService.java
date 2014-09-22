package pw.scho.battleship.core;

import pw.scho.battleship.model.*;
import pw.scho.battleship.persistence.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameService {

    private final Repository<Game> gameRepository;
    private final Transaction transaction;

    public GameService(Repository<Game> gameRepository) {
        this.gameRepository = gameRepository;
        this.transaction = Transaction.getInstance();
    }

    public Game createGame(Player player) {
        synchronized (transaction) {
            Game game = new Game();

            BoardRandomizer boardRandomizer = new BoardRandomizer();
            game.setFirstPlayer(player);
            game.setFirstBoard(boardRandomizer.randomizeWithStandardShips());
            game.setSecondBoard(boardRandomizer.randomizeWithStandardShips());

            gameRepository.add(game);

            return game;
        }
    }

    public void joinGame(UUID gameId, Player player) {
        synchronized (transaction) {
            Game game = gameRepository.get(gameId);

            if (game == null) {
                throw new RuntimeException("Cannot find your game");
            }

            if (game.getSecondPlayer() != null) {
                throw new RuntimeException("Cannot join full game");
            }

            game.setSecondPlayer(player);
        }
    }

    public List<Game> getAllOpenGames(Player player) {
        synchronized (transaction) {
            List<Game> games = gameRepository.all();
            List<Game> openGames = new ArrayList();

            for (Game game : games) {
                if (game.getSecondPlayer() == null && !game.getFirstPlayer().getId().equals(player.getId())) {
                    openGames.add(game);
                }
            }

            return openGames;
        }
    }

    public boolean isItPlayersTurn(UUID gameId, Player player) {
        synchronized (transaction) {
            Game game = gameRepository.get(gameId);
            return new PersonalizedGame(player, game).isItPlayersTurn();
        }
    }

    public void shootAt(UUID gameId, Player player, Position position) {
        synchronized (transaction) {
            Game game = gameRepository.get(gameId);
            new PersonalizedGame(player, game).shootAt(position);
        }
    }

    public List<List<BoardPosition>> getPlayersBoardPositions(UUID gameId, Player player) {
        synchronized (transaction) {
            Game game = gameRepository.get(gameId);
            return new PersonalizedGame(player, game).getPlayersBoardPositions();
        }
    }

    public List<List<BoardPosition>> getOpponentsBoardPositions(UUID gameId, Player player) {
        synchronized (transaction) {
            Game game = gameRepository.get(gameId);
            return new PersonalizedGame(player, game).getOpponentsBoardPositions();
        }
    }
}
