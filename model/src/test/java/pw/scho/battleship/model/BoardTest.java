package pw.scho.battleship.model;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BoardTest {

    private Board board;

    @Before
    public void createBoard() {
        board = new Board();
    }

    // Placing of ships

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

    // Shooting at ships

    @Test
    public void testIsShootableAtIsFalseIfAlreadyShotAt() {
        board.shootAt(new Position(0, 0));

        assertThat(board.isShootableAt(new Position(0, 0)), is(false));
    }

    @Test
    public void testIsShootableAtIsTrueIfNotShotAt() {
        assertThat(board.isShootableAt(new Position(0, 0)), is(true));
    }

    @Test(expected = GameException.class)
    public void testIllegalVerticalPlacementThrowsException() {
        board.shootAt(new Position(0, 0));
        board.shootAt(new Position(0, 0));
    }

    @Test
    public void testAllShipsSunkIsTrueIfAllShipsWereFullyHit() {
        Ship ship = Ship.createHorizontal(new Position(0, 0), 2);

        board.placeShip(ship);
        board.shootAt(new Position(0, 0));
        board.shootAt(new Position(1, 0));

        assertThat(board.allShipsSunk(), is(true));
    }

    @Test
    public void testAllShipsSunkIsFalseIfSomeShipsPartiallyRemain() {
        Ship ship = Ship.createHorizontal(new Position(0, 0), 2);
        Ship otherShip = Ship.createVertical(new Position(3, 3), 3);

        board.placeShip(ship);
        board.placeShip(otherShip);
        board.shootAt(new Position(0, 0));
        board.shootAt(new Position(1, 0));
        board.shootAt(new Position(3, 3));

        assertThat(board.allShipsSunk(), is(false));
    }
}
