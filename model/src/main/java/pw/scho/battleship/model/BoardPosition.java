package pw.scho.battleship.model;

public class BoardPosition {

    private final Kind kind;

    private BoardPosition(Kind kind) {
        this.kind = kind;
    }

    public static BoardPosition createWater() {
        return new BoardPosition(Kind.WATER);
    }

    public static BoardPosition createHitWater() {
        return new BoardPosition(Kind.HIT_WATER);
    }

    public static BoardPosition createUnknown() {
        return new BoardPosition(Kind.UNKNOWN);
    }

    public static BoardPosition createShip() {
        return new BoardPosition(Kind.SHIP);
    }

    public static BoardPosition createHitShip() {
        return new BoardPosition(Kind.HIT_SHIP);
    }

    public Kind getKind() {
        return kind;
    }

    public enum Kind {
        WATER,
        HIT_WATER,
        UNKNOWN,
        SHIP,
        HIT_SHIP
    }
}
