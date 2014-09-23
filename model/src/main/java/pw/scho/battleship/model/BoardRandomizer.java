package pw.scho.battleship.model;

import java.util.Random;

public class BoardRandomizer {

    private final Random random = new Random();
    private Board board;
    private int numberOfTries = 0;
    private final int maximumNumberOfTries = 50;

    public Board randomizeWithStandardShips() {
        return randomize(new int[]{2, 2, 2, 2, 3, 3, 3, 4, 4, 5});
    }

    public Board randomize(int[] shipSizes) {
        while(true){
            Board board = tryToPlaceShips(shipSizes);

            if(board != null){
                break;
            }
        }
        return board;
    }

    private Board tryToPlaceShips(int[] shipSizes){
        board = new Board();

        for (int size : shipSizes) {
            boolean success = placeShip(size);
            if(!success){
                return null;
            }
        }

        return board;
    }

    private boolean placeShip(int size) {
        while (true) {
            Ship ship = createRandomShip(size);

            if (board.shipIsPlaceable(ship)) {
                board.placeShip(ship);
                return true;
            }

            numberOfTries++;
            if(numberOfTries > maximumNumberOfTries){
                numberOfTries = 0;
                return false;
            }
        }
    }

    private Ship createRandomShip(int size) {
        int x = random.nextInt(Board.WIDTH - 1);
        int y = random.nextInt(Board.HEIGHT - 1);

        Position position = new Position(x, y);

        if (random.nextBoolean()) {
            return Ship.createHorizontal(position, size);
        } else {
            return Ship.createVertical(position, size);
        }
    }
}
