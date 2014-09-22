package pw.scho.battleship.web.mapping;

import pw.scho.battleship.model.Game;

public class GameInfo {

    public String opponentName;
    public String gameId;

    public GameInfo() {
    }

    public GameInfo(Game game) {
        this.gameId = game.getId().toString();
        this.opponentName = game.getFirstPlayer().getName();
    }

}
