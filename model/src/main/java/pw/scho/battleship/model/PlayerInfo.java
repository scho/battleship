package pw.scho.battleship.model;

import pw.scho.battleship.model.Player;

public class PlayerInfo {

    private String name;
    private int gamesWon;
    private int gamesLost;

    public PlayerInfo() {
    }

    public PlayerInfo(Player player) {
        this.name = player.getName();
        this.gamesLost = player.getGamesLost();
        this.gamesWon = player.getGamesWon();
    }
}
