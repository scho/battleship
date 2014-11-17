package pw.scho.battleship.model;

import java.util.List;

public class PersonalizedGame {

    private final Player player;
    private final Game game;

    public PersonalizedGame(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public Board getBoard() {
        if (playerIsFirstPlayer()) {
            return game.getFirstBoard();
        }

        return game.getSecondBoard();
    }

    public Board getOpponentBoard() {
        if (playerIsFirstPlayer()) {
            return game.getSecondBoard();
        }

        return game.getFirstBoard();
    }

    public Player getPlayer() {
        if (playerIsFirstPlayer()) {
            return game.getFirstPlayer();
        }

        return game.getSecondPlayer();
    }

    public Player getOpponent() {
        if (playerIsFirstPlayer()) {
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
        checkForWin();
    }


    public boolean isPlayersTurn() {
        if (playerIsFirstPlayer()) {
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

    public List<String> getMessages() {
        if (playerIsFirstPlayer()) {
            return game.getSecondBoard().getMessageQueue().getAllMessages();
        }
        return game.getFirstBoard().getMessageQueue().getAllMessages();
    }

    public boolean isStarted() {
        return getOpponent() != null;
    }


    public boolean isFinished() {
        return game.isFinished();
    }

    public boolean isWon() {
        if (playerIsFirstPlayer()) {
            return game.firstPlayerHasWon();
        }
        return game.secondPlayerHasWon();
    }

    private void checkForWin() {
        if (getOpponentBoard().allShipsSunk()) {
            if (playerIsFirstPlayer()) {
                game.setFirstPlayerHasWon(true);
            } else {
                game.setSecondPlayerHasWon(true);
            }
            game.setFinished(true);
        }
    }

    private boolean playerIsFirstPlayer() {
        return game.getFirstPlayer().getId().equals(player.getId());
    }


}
