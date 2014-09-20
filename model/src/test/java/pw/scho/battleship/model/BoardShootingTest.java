package pw.scho.battleship.model;

import org.junit.Test;
import pw.scho.battleship.model.Board;
import pw.scho.battleship.model.GameException;
import pw.scho.battleship.model.Position;
import pw.scho.battleship.model.Ship;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BoardShootingTest {

    private Board board = new Board();

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
