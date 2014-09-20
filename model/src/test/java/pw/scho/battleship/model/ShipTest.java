package pw.scho.battleship.model;

import org.junit.Test;
import pw.scho.battleship.model.Position;
import pw.scho.battleship.model.Ship;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ShipTest {

    @Test
    public void testHorizontalShipHasCorrectSize() {
        Ship ship = Ship.createHorizontal(new Position(0, 0), 3);

        assertThat(ship.size(), is(3));
    }

    @Test
    public void testVerticalShipHasCorrectSize() {
        Ship ship = Ship.createVertical(new Position(0, 0), 5);

        assertThat(ship.size(), is(5));
    }

}
