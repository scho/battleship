package pw.scho.model;

public class GameRuntimeException extends RuntimeException {

    private GameRuntimeExceptionKind kind;

    private GameRuntimeException(GameRuntimeExceptionKind kind) {
        super(kind.toString());
        this.kind = kind;
    }

    public static GameRuntimeException CreateShipNotPlaceable() {
        return new GameRuntimeException(GameRuntimeExceptionKind.SHIP_NOT_PLACEABLE);
    }

    public static GameRuntimeException CreateAlreadyShotAt() {
        return new GameRuntimeException(GameRuntimeExceptionKind.ALREADY_SHOT_AT);
    }


    private enum GameRuntimeExceptionKind {
        SHIP_NOT_PLACEABLE,
        ALREADY_SHOT_AT;
    }
}
