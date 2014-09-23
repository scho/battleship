package pw.scho.battleship.model;

public class GameInfo {

    private String playersName;
    private String opponentsName = "";
    private boolean isStarted = false;
    private boolean isPlayersTurn = false;

    public GameInfo() {
    }

    public GameInfo(PersonalizedGame game) {
        this.playersName = game.getPlayer().getName();
        if (game.getOpponent() != null) {
            this.opponentsName = game.getOpponent().getName();
        }
        this.isStarted = game.isStarted();
        this.isPlayersTurn = game.isPlayersTurn();
    }

    public String getPlayersName() {
        return playersName;
    }

    public String getOpponentsName() {
        return opponentsName;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }

}
