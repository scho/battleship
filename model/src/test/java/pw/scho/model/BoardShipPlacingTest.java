package pw.scho.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BoardShipPlacingTest {

    private Board board = new Board();

    @Test
    public void testShipIsPlaceableForEmptyBoard() {
        Ship ship = Ship.createHorizontal(new Position(2, 2), 5);

        assertThat(board.shipIsPlaceable(ship), is(true));
    }

    @Test
    public void testShipIsPlaceableNextToEdge() {
        Ship ship = Ship.createHorizontal(new Position(0, 9), 5);

        assertThat(board.shipIsPlaceable(ship), is(true));
    }

    @Test
    public void testShipIsNotPlaceableIfOtherShipIsOnSpot() {
        board.placeShip(Ship.createHorizontal(new Position(0, 0), 5));

        Ship ship = Ship.createHorizontal(new Position(4, 0), 2);

        assertThat(board.shipIsPlaceable(ship), is(false));
    }

    @Test
    public void testIsNotPlaceableIfOtherShipBordersInXDirection() {
        board.placeShip(Ship.createVertical(new Position(1, 0), 5));

        Ship ship = Ship.createHorizontal(new Position(0, 0), 1);

        assertThat(board.shipIsPlaceable(ship), is(false));
    }

    @Test
    public void testIsNotPlaceableIfOtherShipBordersInYDirection() {
        board.placeShip(Ship.createHorizontal(new Position(0, 1), 5));

        Ship ship = Ship.createHorizontal(new Position(0, 0), 1);

        assertThat(board.shipIsPlaceable(ship), is(false));
    }

    @Test
    public void testIsNotPlaceableIfShipIsOutOfBoard() {
        Ship ship = Ship.createHorizontal(new Position(9, 9), 2);

        assertThat(board.shipIsPlaceable(ship), is(false));
    }

    @Test
    public void testIsPlaceableInCorner() {
        Ship ship = Ship.createHorizontal(new Position(9, 9), 1);

        assertThat(board.shipIsPlaceable(ship), is(true));
    }

    @Test(expected = GameException.class)
    public void testIllegalPlacementThrowsException() {
        Ship ship = Ship.createHorizontal(new Position(9, 9), 1);
        Ship otherShip = Ship.createHorizontal(new Position(9, 9), 1);

        board.placeShip(ship);
        board.placeShip(otherShip);
    }
}
