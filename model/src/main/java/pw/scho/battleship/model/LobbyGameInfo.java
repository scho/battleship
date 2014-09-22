package pw.scho.battleship.model;

import pw.scho.battleship.model.Game;

public class LobbyGameInfo {

    private String opponentName;
    private String gameId;

    public LobbyGameInfo() {
    }

    public LobbyGameInfo(Game game) {
        this.gameId = game.getId().toString();
        this.opponentName = game.getFirstPlayer().getName();
    }

}
