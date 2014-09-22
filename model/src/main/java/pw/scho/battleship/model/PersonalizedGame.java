package pw.scho.battleship.model;

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
        if (!isItPlayersTurn()) {
            throw GameException.CreateShotTwice();
        }
        getBoard().shootAt(position);
        game.toggleTurn();
    }

    public boolean isItPlayersTurn() {
        if (iAmFirst()) {
            return game.isItFirstPlayersTurn();
        }

        return game.isItSecondPlayersTurn();
    }
}
