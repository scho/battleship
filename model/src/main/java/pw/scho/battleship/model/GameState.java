package pw.scho.battleship.model;

public class GameState {

    private String playersName;
    private String opponentsName = "";
    private boolean started = false;
    private boolean playersTurn = false;
    private boolean finished;
    private boolean won;

    public GameState(PersonalizedGame game) {
        this.playersName = game.getPlayer().getName();
        if (game.getOpponent() != null) {
            this.opponentsName = game.getOpponent().getName();
        }
        this.started = game.isStarted();
        this.playersTurn = game.isPlayersTurn();
        this.won = game.isWon();
        this.finished = game.isFinished();
    }

    public boolean isWon() {
        return won;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getPlayersName() {
        return playersName;
    }

    public String getOpponentsName() {
        return opponentsName;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }

}
