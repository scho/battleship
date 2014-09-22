package pw.scho.battleship.model;

public class GameException extends RuntimeException {

    private GameRuntimeExceptionKind kind;

    private GameException(GameRuntimeExceptionKind kind) {
        super(kind.toString());
        this.kind = kind;
    }

    public static GameException CreateShipNotPlaceable() {
        return new GameException(GameRuntimeExceptionKind.SHIP_NOT_PLACEABLE);
    }

    public static GameException CreateAlreadyShotAt() {
        return new GameException(GameRuntimeExceptionKind.ALREADY_SHOT_AT);
    }

    public static GameException CreateShotTwice() {
        return new GameException(GameRuntimeExceptionKind.SHOT_TWICE);
    }

    private enum GameRuntimeExceptionKind {
        SHIP_NOT_PLACEABLE,
        ALREADY_SHOT_AT,
        SHOT_TWICE;
    }
}
