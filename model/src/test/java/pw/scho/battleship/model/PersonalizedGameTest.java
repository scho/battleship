package pw.scho.battleship.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PersonalizedGameTest {

    private PersonalizedGame personalizedGame;
    private Player player;
    private Player opponent;
    private Board board;
    private Board opponentBoard;
    private Game game;

    @Before
    public void setUp() {
        player = new Player("Player", "secret");
        opponent = new Player("Opponent", "secret");
        board = new Board();
        opponentBoard = new Board();
        game = new Game();
        game.setFirstPlayer(player);
        game.setSecondPlayer(opponent);
        game.setFirstBoard(board);
        game.setSecondBoard(opponentBoard);
        personalizedGame = new PersonalizedGame(player, game);
    }

    @Test
    public void testPlayerEntities() {
        personalizedGame = new PersonalizedGame(player, game);

        assertThat(personalizedGame.getPlayer(), is(player));
        assertThat(personalizedGame.getBoard(), is(board));
        assertThat(personalizedGame.getOpponent(), is(opponent));
        assertThat(personalizedGame.getOpponentBoard(), is(opponentBoard));
    }

    @Test
    public void testOpponentEntities() {
        personalizedGame = new PersonalizedGame(opponent, game);

        assertThat(personalizedGame.getPlayer(), is(opponent));
        assertThat(personalizedGame.getBoard(), is(opponentBoard));
        assertThat(personalizedGame.getOpponent(), is(player));
        assertThat(personalizedGame.getOpponentBoard(), is(board));
    }

    @Test
    public void testFirstPlayerAlwaysStarts() {
        assertThat(personalizedGame.isItPlayersTurn(), is(true));
    }

    @Test
    public void testShootingChangesTurn() {
        assertThat(personalizedGame.isItPlayersTurn(), is(true));

        personalizedGame.shootAt(new Position(0, 0));

        assertThat(personalizedGame.isItPlayersTurn(), is(false));
    }
}