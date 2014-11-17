package pw.scho.battleship.model;

public class LobbyGameInfo {

    private String playerName;
    private String opponentName;
    private String gameId;
    public LobbyGameInfo() {
    }

    public LobbyGameInfo(Game game) {
        this.gameId = game.getId().toString();
        this.opponentName = game.getFirstPlayer().getName();
        this.playerName = game.getSecondPlayer() != null ? game.getSecondPlayer().getName() : "";
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getGameId() {
        return gameId;
    }

    public String getOpponentName() {
        return opponentName;
    }
}
