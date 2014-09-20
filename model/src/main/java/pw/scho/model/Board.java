package pw.scho.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    private final BoardMessageQueue messageQueue = new BoardMessageQueue();
    private List<Ship> ships;
    private List<Position> shots;
    private Ship[][] ships2DMap;
    private boolean[][] shots2DMap;

    public Board() {
        clearAllShips();
        clearAllShots();
    }

    public BoardMessageQueue getMessageQueue() {
        return messageQueue;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<Position> getShots() {
        return shots;
    }

    public void clearAllShips() {
        ships2DMap = new Ship[HEIGHT][WIDTH];
        ships = new ArrayList();
    }

    public void clearAllShots() {
        shots2DMap = new boolean[HEIGHT][WIDTH];
        shots = new ArrayList();
    }

    public boolean shipIsPlaceable(Ship ship) {
        for (Position position : ship.getShipPositions()) {
            if (!positionIsOnBoard(position) || positionIsTaken(position)) {
                return false;
            }
        }

        for (Position position : ship.getBoarderPositions()) {
            if (positionIsOnBoard(position) && positionIsTaken(position)) {
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

    public boolean isShootableAt(Position position) {
        return shots2DMap[position.getY()][position.getX()] == false;
    }

    public void shootAt(Position position) {
        if (!isShootableAt(position)) {
            throw GameException.CreateAlreadyShotAt();
        }

        shots2DMap[position.getY()][position.getX()] = true;
        Ship ship = getShipAtPosition(position);

        shots.add(position);

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

    private boolean positionIsTaken(Position position) {
        return getShipAtPosition(position) != null;
    }

    private Ship getShipAtPosition(Position position) {
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
}
