package pw.scho.model;

import java.util.Random;

public class BoardRandomizer {

    private final Random random = new Random();
    private final Board board = new Board();

    public Board randomize(int[] shipSizes) {

        for(int size : shipSizes){
            placeShip(size);
        }

        return board;
    }

    private void placeShip(int size) {
        while(true){
            Ship ship = createRandomShip(size);

            if(board.shipIsPlaceable(ship)){
                board.placeShip(ship);
                break;
            }
        }
    }

    private Ship createRandomShip(int size) {
        int x = random.nextInt(Board.WIDTH - 1);
        int y = random.nextInt(Board.HEIGHT - 1);

        Position position = new Position(x, y);

        if(random.nextBoolean()){
            return Ship.createHorizontal(position, size);
        } else {
            return Ship.createVertical(position, size);
        }
    }
}
