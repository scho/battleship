package pw.scho.battleship.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    static final int WIDTH = 10;
    static final int HEIGHT = 10;
    private final BoardMessageQueue messageQueue = new BoardMessageQueue();
    private Ship[][] ships2DMap = new Ship[HEIGHT][WIDTH];
    private boolean[][] shots2DMap = new boolean[HEIGHT][WIDTH];
    private List<Ship> ships = new ArrayList<>();

    public BoardMessageQueue getMessageQueue() {
        return messageQueue;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public boolean shipIsPlaceable(Ship ship) {
        for (Position position : ship.getShipPositions()) {
            if (!positionIsOnBoard(position) || positionIsTakenByShip(position)) {
                return false;
            }
        }

        for (Position position : ship.getBoarderPositions()) {
            if (positionIsOnBoard(position) && positionIsTakenByShip(position)) {
                return false;
            }
        }

        return true;
    }

    public void placeShip(Ship ship) {
        if (!shipIsPlaceable(ship)) {
            throw GameException.CreateShipNotPlaceable();
        }

        for (Position position : ship.getShipPositions()) {
            setShipAtPosition(ship, position);
        }
        ships.add(ship);
    }

    public boolean positionIsShootableAt(Position position) {
        return !shots2DMap[position.getY()][position.getX()];
    }

    boolean positionWasShotAt(Position position) {
        return !positionIsShootableAt(position);
    }

    public void shootAt(Position position) {
        if (!positionIsShootableAt(position)) {
            throw GameException.CreateAlreadyShotAt();
        }

        shots2DMap[position.getY()][position.getX()] = true;
        Ship ship = getShipAtPosition(position);

        if (ship != null) {
            ship.hit();
            messageQueue.addHitMessage(position, ship);
        } else {
            messageQueue.addMissMessage(position);
        }
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.sunk()) {
                return false;
            }
        }
        return true;
    }

    boolean positionIsTakenByShip(Position position) {
        return getShipAtPosition(position) != null;
    }

    Ship getShipAtPosition(Position position) {
        return ships2DMap[position.getY()][position.getX()];
    }

    private void setShipAtPosition(Ship ship, Position position) {
        ships2DMap[position.getY()][position.getX()] = ship;
    }

    private boolean positionIsOnBoard(Position position) {
        return position.getX() >= 0 &&
            position.getY() >= 0 &&
            position.getX() < WIDTH &&
            position.getY() < HEIGHT;
    }

    List<List<BoardPosition>> getMaskedBoardPositions() {
        return getBoardPositions(true);
    }

    List<List<BoardPosition>> getUnmaskedBoardPositions() {
        return getBoardPositions(false);
    }

    private List<List<BoardPosition>> getBoardPositions(boolean mask) {
        return new BoardPositionGenerator(this, mask).generate();
    }
}
