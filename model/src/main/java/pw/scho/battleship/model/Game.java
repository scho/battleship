package pw.scho.battleship.model;

import java.util.UUID;

public class Game {

    private Board firstBoard;
    private Board secondBoard;
    private Player firstPlayer;
    private Player secondPlayer;
    private UUID id;
    private boolean firstPlayersTurn = true;
    private boolean finished;
    private boolean firstPlayerHasWon = false;
    private boolean secondPlayerHasWon = false;

    public Game() {
        this.id = UUID.randomUUID();
    }

    boolean secondPlayerHasWon() {
        return secondPlayerHasWon;
    }

    void setSecondPlayerHasWon() {
        this.secondPlayerHasWon = true;
    }

    boolean firstPlayerHasWon() {
        return firstPlayerHasWon;
    }

    void setFirstPlayerHasWon() {
        this.firstPlayerHasWon = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public UUID getId() {
        return id;
    }

    Board getFirstBoard() {
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

    void toggleTurn() {
        firstPlayersTurn = !firstPlayersTurn;
    }

    boolean isItFirstPlayersTurn() {
        return firstPlayersTurn;
    }

    boolean isItSecondPlayersTurn() {
        return !firstPlayersTurn;
    }


}
