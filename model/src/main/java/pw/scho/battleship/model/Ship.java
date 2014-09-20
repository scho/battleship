package pw.scho.battleship.model;

import java.util.ArrayList;

public class Ship {

    private final Position fromPosition;
    private final Position toPosition;
    private int hits = 0;

    protected Ship(Position fromPosition, Position toPosition) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    public static Ship createHorizontal(Position position, int size) {
        return new Ship(position, new Position(position.getX() + size - 1, position.getY()));
    }

    public static Ship createVertical(Position position, int size) {
        return new Ship(position, new Position(position.getX(), position.getY() + size - 1));
    }

    public Position getFromPosition() {
        return fromPosition;
    }

    public Position getToPosition() {
        return toPosition;
    }

    public int size() {
        return Math.max(toPosition.getX() - fromPosition.getX(), toPosition.getY() - fromPosition.getY()) + 1;
    }

    public void hit() {
        hits++;
    }

    public boolean sunk() {
        return hits == size();
    }

    public Iterable<Position> getShipPositions() {
        ArrayList<Position> positions = new ArrayList();

        for (int x = fromPosition.getX(); x <= toPosition.getX(); x++) {
            for (int y = fromPosition.getY(); y <= toPosition.getY(); y++) {
                positions.add(new Position(x, y));
            }
        }

        return positions;
    }

    public Iterable<Position> getBoarderPositions() {
        ArrayList<Position> positions = new ArrayList();

        for (int x = fromPosition.getX() - 1; x <= toPosition.getX() + 1; x++) {
            for (int y = fromPosition.getY() - 1; y <= toPosition.getY() + 1; y++) {
                if (x < fromPosition.getX() || x > toPosition.getX()
                        || y < fromPosition.getY() || y > toPosition.getY()) {
                    positions.add(new Position(x, y));
                }
            }
        }

        return positions;
    }
}
