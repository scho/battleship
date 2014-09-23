package pw.scho.battleship.model;

import java.util.List;

public class PersonalizedGame {

    private final Player player;
    private final Game game;

    public PersonalizedGame(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    private boolean iAmFirst() {
        return game.getFirstPlayer().getId().equals(player.getId());
    }

    public Board getBoard() {
        if (iAmFirst()) {
            return game.getFirstBoard();
        }

        return game.getSecondBoard();
    }

    public Board getOpponentBoard() {
        if (iAmFirst()) {
            return game.getSecondBoard();
        }

        return game.getFirstBoard();
    }

    public Player getPlayer() {
        if (iAmFirst()) {
            return game.getFirstPlayer();
        }

        return game.getSecondPlayer();
    }

    public Player getOpponent() {
        if (iAmFirst()) {
            return game.getSecondPlayer();
        }

        return game.getFirstPlayer();
    }

    public void shootAt(Position position) {
        if (!isPlayersTurn()) {
            throw GameException.CreateNotPlayersTurn();
        }
        getOpponentBoard().shootAt(position);
        game.toggleTurn();
    }

    public boolean isPlayersTurn() {
        if (iAmFirst()) {
            return game.isItFirstPlayersTurn();
        }

        return game.isItSecondPlayersTurn();
    }

    public List<List<BoardPosition>> getPlayersBoardPositions() {
        return getBoard().getUnmaskedBoardPositions();
    }

    public List<List<BoardPosition>> getOpponentsBoardPositions() {
        return getOpponentBoard().getMaskedBoardPositions();
    }

    public boolean isStarted() {
        return getOpponent() != null;
    }
}
