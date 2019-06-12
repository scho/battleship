package pw.scho.battleship.model;

public class PlayerInfo {

    private String name;
    private int gamesWon;
    private int gamesLost;

    public PlayerInfo(Player player) {
        this.name = player.getName();
        this.gamesLost = player.getGamesLost();
        this.gamesWon = player.getGamesWon();
    }

    public String getName() {
        return name;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }
}
