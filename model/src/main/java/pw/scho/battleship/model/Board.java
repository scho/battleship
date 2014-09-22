package pw.scho.battleship.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    private Ship[][] ships2DMap = new Ship[HEIGHT][WIDTH];
    private boolean[][] shots2DMap = new boolean[HEIGHT][WIDTH];
    private final BoardMessageQueue messageQueue = new BoardMessageQueue();
    private List<Ship> ships = new ArrayList();
    private List<Position> shots = new ArrayList();

    public BoardMessageQueue getMessageQueue() {
        return messageQueue;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<Position> getShots() {
        return shots;
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
        return shots2DMap[position.getY()][position.getX()] == false;
    }

    public boolean positionWasShotAt(Position position) {
        return !positionIsShootableAt(position);
    }

    public void shootAt(Position position) {
        if (!positionIsShootableAt(position)) {
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

    private boolean positionIsTakenByShip(Position position) {
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

    public BoardPosition[][] getMaskedBoardPositions() {
        return getBoardPositions(true);
    }

    public BoardPosition[][] getUnmaskedBoardPositions() {
        return getBoardPositions(false);
    }

    private BoardPosition[][] getBoardPositions(boolean mask) {
        BoardPosition[][] boardPositions = new BoardPosition[HEIGHT][WIDTH];

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Position position = new Position(x, y);
                BoardPosition boardPosition;
                if (positionWasShotAt(position)) {
                    if (positionIsTakenByShip(position)) {
                        boardPosition = BoardPosition.createHitShip();
                    } else {
                        boardPosition = BoardPosition.createHitWater();
                    }
                } else {
                    if (mask) {
                        boardPosition = BoardPosition.createUnknown();
                    } else {
                        if (positionIsTakenByShip(position)) {
                            boardPosition = BoardPosition.createShip();
                        } else {
                            boardPosition = BoardPosition.createWater();
                        }
                    }
                }

                boardPositions[y][x] = boardPosition;
            }
        }

        return boardPositions;
    }
}
