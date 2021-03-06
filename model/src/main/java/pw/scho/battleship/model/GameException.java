package pw.scho.battleship.model;

public class GameException extends RuntimeException {

    private GameRuntimeExceptionKind kind;

    private GameException(GameRuntimeExceptionKind kind) {
        super(kind.toString());
        this.kind = kind;
    }

    static GameException CreateShipNotPlaceable() {
        return new GameException(GameRuntimeExceptionKind.SHIP_NOT_PLACEABLE);
    }

    static GameException CreateAlreadyShotAt() {
        return new GameException(GameRuntimeExceptionKind.ALREADY_SHOT_AT);
    }

    static GameException CreateNotPlayersTurn() {
        return new GameException(GameRuntimeExceptionKind.NOT_PLAYERS_TURN);
    }

    private enum GameRuntimeExceptionKind {
        SHIP_NOT_PLACEABLE,
        ALREADY_SHOT_AT,
        NOT_PLAYERS_TURN;
    }
}
