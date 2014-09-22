package pw.scho.battleship.web.mapping;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
}
