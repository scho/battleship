package pw.scho.battleship.model;

public class BoardPosition {

    private Kind kind;


    private BoardPosition(Kind kind) {
        this.kind = kind;
    }

    static BoardPosition createWater() {
        return new BoardPosition(Kind.WATER);
    }

    static BoardPosition createWaterHit() {
        return new BoardPosition(Kind.WATER_HIT);
    }

    static BoardPosition createUnknown() {
        return new BoardPosition(Kind.UNKNOWN);
    }

    static BoardPosition createShip() {
        return new BoardPosition(Kind.SHIP);
    }

    static BoardPosition createShipHit() {
        return new BoardPosition(Kind.SHIP_HIT);
    }

    static BoardPosition createShipSunk() {
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
