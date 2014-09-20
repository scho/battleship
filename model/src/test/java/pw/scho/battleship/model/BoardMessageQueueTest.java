package pw.scho.battleship.model;

import org.junit.Test;
import pw.scho.battleship.model.Board;
import pw.scho.battleship.model.Position;
import pw.scho.battleship.model.Ship;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BoardMessageQueueTest {

    private Board board = new Board();

    @Test
    public void testShootAtEmptyPositionAddsMessage() {
        board.shootAt(new Position(0, 0));

        assertThat(board.getMessageQueue().size(), is(1));
    }

    @Test
    public void testShootAtShipAddsMessage() {
        board.placeShip(Ship.createHorizontal(new Position(0, 0), 3));
        board.shootAt(new Position(0, 0));

        assertThat(board.getMessageQueue().size(), is(1));
    }

}
