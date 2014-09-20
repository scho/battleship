package pw.scho.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PositionTest {

    @Test
    public void testToString(){
        Position position = new Position(3, 3);

        assertThat(position.toString(), is("D4"));
    }


}
