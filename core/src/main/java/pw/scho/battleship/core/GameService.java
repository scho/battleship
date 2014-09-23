package pw.scho.battleship.core;

import pw.scho.battleship.model.*;
import pw.scho.battleship.persistence.Repository;
import pw.scho.battleship.persistence.mongo.PlayerMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameService {

    private final Repository<Game> gameRepository;
    private final PlayerMongoRepository playerRepository;
    private final Transaction transaction;

    public GameService(Repository<Game> gameRepository, PlayerMongoRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
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

    public List<LobbyGameInfo> getAllOpenGames(Player player) {
        synchronized (transaction) {
            List<Game> games = gameRepository.all();
            List<LobbyGameInfo> openGames = new ArrayList();

            for (Game game : games) {
                if (game.getSecondPlayer() == null && !game.getFirstPlayer().getId().equals(player.getId())) {
                    openGames.add(new LobbyGameInfo(game));
                }
            }

            return openGames;
        }
    }

    public GameState getGameInfo(UUID gameId, Player player) {
        synchronized (transaction) {
            Game game = gameRepository.get(gameId);
            return new GameState(new PersonalizedGame(player, game));
        }
    }

    public void shootAt(UUID gameId, Player player, Position position) {
        synchronized (transaction) {
            Game game = gameRepository.get(gameId);
            PersonalizedGame personalizedGame = new PersonalizedGame(player, game);
            if(personalizedGame.isStarted() && personalizedGame.isPlayersTurn() && !personalizedGame.isFinished()){
                personalizedGame.shootAt(position);

                updatePlayerStats(personalizedGame);
            }
        }
    }

    private void updatePlayerStats(PersonalizedGame game) {
        if(game.isFinished()){
            playerRepository.getSession().start();

            Player winner = game.isWon() ? game.getPlayer() : game.getOpponent();
            Player looser = game.isWon() ? game.getOpponent() : game.getPlayer();

            winner = playerRepository.get(winner.getId());
            looser = playerRepository.get(looser.getId());

            winner.lastGameWon();
            looser.lastGameLost();

            playerRepository.getSession().stop();
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
