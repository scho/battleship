package pw.scho.battleship.model;

public class BoardPosition {

    private Kind kind;

    public BoardPosition() {
    }

    private BoardPosition(Kind kind) {
        this.kind = kind;
    }

    public static BoardPosition createWater() {
        return new BoardPosition(Kind.WATER);
    }

    public static BoardPosition createWaterHit() {
        return new BoardPosition(Kind.WATER_HIT);
    }

    public static BoardPosition createUnknown() {
        return new BoardPosition(Kind.UNKNOWN);
    }

    public static BoardPosition createShip() {
        return new BoardPosition(Kind.SHIP);
    }

    public static BoardPosition createShipHit() {
        return new BoardPosition(Kind.SHIP_HIT);
    }

    public static BoardPosition createShipSunk() {
        return new BoardPosition(Kind.SHIP_SUNK);
    }

    public Kind getKind() {
        return kind;
    }

    public enum Kind {
        WATER,
        WATER_HIT,
        UNKNOWN,
        SHIP,
        SHIP_HIT,
        SHIP_SUNK
    }
}
