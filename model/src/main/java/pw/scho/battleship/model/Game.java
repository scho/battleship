package pw.scho.battleship.model;

import java.util.UUID;

public class Game {

    private Board firstBoard;
    private Board secondBoard;
    private Player firstPlayer;
    private Player secondPlayer;
    private UUID id;
    private boolean firstPlayersTurn = true;

    public Game() {
        this.id = UUID.randomUUID();
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

    public void toggleTurn(){
        firstPlayersTurn = !firstPlayersTurn;
    }

    public boolean isItFirstPlayersTurn(){
        return firstPlayersTurn;
    }

    public boolean isItSecondPlayersTurn(){
        return !firstPlayersTurn;
    }
}
