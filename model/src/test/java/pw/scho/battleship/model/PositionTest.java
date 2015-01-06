package pw.scho.battleship.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PositionTest {

    @Test
    public void testToString() {
        Position position = new Position(3, 3);

        assertThat(position.toString(), is("D4"));
    }

    @Test
    public void testDistance(){
        Position position = new Position(1,1);
        Position otherPosition = new Position(4,5);

        assertThat(position.distanceTo(otherPosition), is(5d));
    }

    @Test
    public void testZeroDistance(){
        Position position = new Position(1,1);

        assertThat(position.distanceTo(position), is(0d));
    }
}
