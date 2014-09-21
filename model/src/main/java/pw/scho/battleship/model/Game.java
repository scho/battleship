package pw.scho.battleship.model;

import java.util.UUID;

public class Game {

    private Board firstBoard;
    private Board secondBoard;
    private UUID firstPlayerId;
    private UUID secondPlayerId;
    private UUID id;

    public Game() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public Board getFirstBoard() {
        return firstBoard;
    }

    public void setFirstBoard(Board firstBoard) {
        this.firstBoard = firstBoard;
    }

    public Board getSecondBoard() {
        return secondBoard;
    }

    public void setSecondBoard(Board secondBoard) {
        this.secondBoard = secondBoard;
    }

    public UUID getFirstPlayerId() {
        return firstPlayerId;
    }

    public void setFirstPlayerId(UUID firstPlayerId) {
        this.firstPlayerId = firstPlayerId;
    }

    public UUID getSecondPlayerId() {
        return secondPlayerId;
    }

    public void setSecondPlayerId(UUID secondPlayerId) {
        this.secondPlayerId = secondPlayerId;
    }
}
