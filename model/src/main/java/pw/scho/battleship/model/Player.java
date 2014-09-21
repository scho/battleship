package pw.scho.battleship.model;

import java.util.UUID;

public class Player {

    private UUID id;
    private String name;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private String password;

    public Player(String name, String password) {
        this();
        this.name = name;
        this.password = password;
    }

    public Player() {
        id = UUID.randomUUID();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public UUID getId() {
        return id;
    }
}
