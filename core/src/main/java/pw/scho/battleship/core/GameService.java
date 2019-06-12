package pw.scho.battleship.core;

import pw.scho.battleship.model.BoardPosition;
import pw.scho.battleship.model.BoardRandomizer;
import pw.scho.battleship.model.Game;
import pw.scho.battleship.model.GameState;
import pw.scho.battleship.model.LobbyGameInfo;
import pw.scho.battleship.model.PersonalizedGame;
import pw.scho.battleship.model.Player;
import pw.scho.battleship.model.Position;
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

    public Game openGame(UUID playerId) throws ServiceException {
        synchronized (transaction) {
            Player player = getPlayerById(playerId);
            Game game = new Game();

            BoardRandomizer boardRandomizer = new BoardRandomizer();
            game.setFirstPlayer(player);
            game.setFirstBoard(boardRandomizer.randomizeWithStandardShips());
            game.setSecondBoard(boardRandomizer.randomizeWithStandardShips());

            gameRepository.insert(game);

            return game;
        }
    }

    public void joinGame(UUID gameId, UUID playerId) throws ServiceException {
        synchronized (transaction) {
            Player player = getPlayerById(playerId);
            Game game = getGameById(gameId);

            if (game == null) {
                throw ServiceException.createNotFound();
            }

            if (game.getSecondPlayer() != null) {
                throw ServiceException.createInvalidAction();
            }

            game.setSecondPlayer(player);
        }
    }

    public List<LobbyGameInfo> getAllOpenGames(UUID playerId) throws ServiceException {
        synchronized (transaction) {
            Player player = getPlayerById(playerId);

            List<Game> games = gameRepository.all();
            List<LobbyGameInfo> openGames = new ArrayList<>();

            for (Game game : games) {
                if (game.getSecondPlayer() == null && !game.getFirstPlayer().getId().equals(
                    player.getId())) {
                    openGames.add(new LobbyGameInfo(game));
                }
            }

            return openGames;
        }
    }

    public List<LobbyGameInfo> getAllOwnAndOngoingGames(UUID playerId) throws ServiceException {
        synchronized (transaction) {
            Player player = getPlayerById(playerId);

            List<Game> games = gameRepository.all();
            List<LobbyGameInfo> openGames = new ArrayList<>();

            for (Game game : games) {
                boolean isFirstPlayer = game.getFirstPlayer().getId().equals(player.getId());
                boolean isSecondPlayer = game.getSecondPlayer() != null && game.getSecondPlayer().getId().equals(
                    player.getId());

                if (!game.isFinished() && (isFirstPlayer || isSecondPlayer)) {
                    openGames.add(new LobbyGameInfo(game));
                }
            }

            return openGames;
        }
    }

    public GameState getGameInfo(UUID gameId, UUID playerId) throws ServiceException {
        synchronized (transaction) {
            Game game = getGameById(gameId);
            Player player = getPlayerById(playerId);

            checkIfPlayerIsMemberOfGame(game, player);

            return new GameState(new PersonalizedGame(player, game));
        }
    }

    public void shootAt(UUID gameId, UUID playerId, Position position) throws ServiceException {
        synchronized (transaction) {
            Game game = getGameById(gameId);
            Player player = getPlayerById(playerId);

            checkIfPlayerIsMemberOfGame(game, player);

            PersonalizedGame personalizedGame = new PersonalizedGame(player, game);
            if (personalizedGame.isStarted() && personalizedGame.isPlayersTurn() && !personalizedGame.isFinished()) {
                personalizedGame.shootAt(position);

                updatePlayerStats(personalizedGame);
            }
        }
    }


    public List<List<BoardPosition>> getPlayersBoardPositions(UUID gameId, UUID playerId) throws ServiceException {
        synchronized (transaction) {
            Game game = getGameById(gameId);
            Player player = getPlayerById(playerId);

            checkIfPlayerIsMemberOfGame(game, player);

            return new PersonalizedGame(player, game).getPlayersBoardPositions();
        }
    }

    public List<List<BoardPosition>> getOpponentsBoardPositions(UUID gameId, UUID playerId) throws ServiceException {
        synchronized (transaction) {
            Game game = getGameById(gameId);
            Player player = getPlayerById(playerId);

            checkIfPlayerIsMemberOfGame(game, player);

            return new PersonalizedGame(player, game).getOpponentsBoardPositions();
        }
    }

    public List<String> getMessages(UUID gameId, UUID playerId) throws ServiceException {
        synchronized (transaction) {
            Game game = getGameById(gameId);
            Player player = getPlayerById(playerId);

            checkIfPlayerIsMemberOfGame(game, player);

            return new PersonalizedGame(player, game).getMessages();
        }
    }

    private void updatePlayerStats(PersonalizedGame game) {
        if (game.isFinished()) {
            Player winner = game.isWon() ? game.getPlayer() : game.getOpponent();
            Player looser = game.isWon() ? game.getOpponent() : game.getPlayer();

            winner = playerRepository.get(winner.getId().toString());
            looser = playerRepository.get(looser.getId().toString());

            winner.lastGameWon();
            looser.lastGameLost();

            playerRepository.update(winner.getId(), winner);
            playerRepository.update(looser.getId(), looser);
        }
    }

    private void checkIfPlayerIsMemberOfGame(Game game, Player player) throws ServiceException {
        if (game.getFirstPlayer().getId().equals(player.getId())
            || game.getSecondPlayer() != null && game.getSecondPlayer().getId().equals(
            player.getId())) {
            return;
        }

        throw ServiceException.createUnauthorized();
    }

    private Player getPlayerById(UUID playerId) throws ServiceException {
        Player player = playerRepository.get(playerId);

        if (player != null) {
            return player;
        }

        throw ServiceException.createUnauthorized();
    }

    private Game getGameById(UUID gameId) throws ServiceException {
        Game game = gameRepository.get(gameId);

        if (game != null) {
            return game;
        }

        throw ServiceException.createNotFound();
    }
}
