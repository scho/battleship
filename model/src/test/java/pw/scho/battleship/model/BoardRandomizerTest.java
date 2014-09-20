package pw.scho.battleship.model;

import org.junit.Test;
import pw.scho.battleship.model.Board;
import pw.scho.battleship.model.BoardRandomizer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BoardRandomizerTest {

    private BoardRandomizer boardRandomizer = new BoardRandomizer();

    @Test
    public void testAddsCorrectNumberOfShips() {
        Board board = boardRandomizer.randomize(new int[]{2, 3, 4});

        assertThat(board.getShips().size(), is(3));
    }

    @Test
    public void testAddsCorrectSize() {
        Board board = boardRandomizer.randomize(new int[]{2, 3, 4});

        assertThat(board.getShips().get(0).size(), is(2));
        assertThat(board.getShips().get(1).size(), is(3));
        assertThat(board.getShips().get(2).size(), is(4));
    }

}
