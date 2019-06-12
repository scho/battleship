package pw.scho.battleship.model;

import java.util.ArrayList;
import java.util.List;

public class BoardPositionGenerator {

    private final Board board;
    private final boolean mask;
    private List<List<BoardPosition>> boardPositions;

    public BoardPositionGenerator(Board board, boolean mask) {
        this.board = board;
        this.mask = mask;
        boardPositions = new ArrayList<>();
    }

    public List<List<BoardPosition>> generate() {
        for (int y = 0; y < Board.HEIGHT; y++) {
            processRow(y);
        }

        return boardPositions;
    }

    private void processRow(int y) {
        List<BoardPosition> row = new ArrayList<>();
        boardPositions.add(row);
        for (int x = 0; x < Board.WIDTH; x++) {
            row.add(processPosition(new Position(x, y)));
        }
    }

    private BoardPosition processPosition(Position position) {
        if (board.positionWasShotAt(position)) {
            if (board.positionIsTakenByShip(position)) {
                Ship ship = board.getShipAtPosition(position);
                if (ship.sunk()) {
                    return BoardPosition.createShipSunk();
                }
                return BoardPosition.createShipHit();
            }
            return BoardPosition.createWaterHit();
        }
        if (mask) {
            return BoardPosition.createUnknown();
        }
        if (board.positionIsTakenByShip(position)) {
            return BoardPosition.createShip();
        }
        return BoardPosition.createWater();
    }
}
