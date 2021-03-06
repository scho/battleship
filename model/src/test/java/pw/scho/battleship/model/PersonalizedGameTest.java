package pw.scho.battleship.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
        assertThat(personalizedGame.isPlayersTurn(), is(true));
    }

    @Test
    public void testShootingChangesTurn() {
        assertThat(personalizedGame.isPlayersTurn(), is(true));

        personalizedGame.shootAt(new Position(0, 0));

        assertThat(personalizedGame.isPlayersTurn(), is(false));
    }

    @Test(expected = GameException.class)
    public void testShootingTwiceThrowsGameException() {
        personalizedGame.shootAt(new Position(0, 0));
        personalizedGame.shootAt(new Position(1, 0));
    }

    @Test
    public void testGetPlayersBoardPositions() {
        board.placeShip(Ship.createHorizontal(new Position(0, 0), 1));
        board.placeShip(Ship.createHorizontal(new Position(5, 5), 2));
        personalizedGame.shootAt(new Position(0, 0));
        new PersonalizedGame(opponent, game).shootAt(new Position(0, 0));
        personalizedGame.shootAt(new Position(0, 1));
        new PersonalizedGame(opponent, game).shootAt(new Position(5, 5));
        List<List<BoardPosition>> boardPositions = personalizedGame.getPlayersBoardPositions();

        assertThat(boardPositions.get(0).get(0).getKind(), is(BoardPosition.Kind.SHIP_SUNK));
        assertThat(boardPositions.get(5).get(5).getKind(), is(BoardPosition.Kind.SHIP_HIT));
        assertThat(boardPositions.get(5).get(6).getKind(), is(BoardPosition.Kind.SHIP));
        assertThat(boardPositions.get(0).get(2).getKind(), is(BoardPosition.Kind.WATER));
    }

    @Test
    public void testGetOpponentsBoardPositions() {
        opponentBoard.placeShip(Ship.createHorizontal(new Position(0, 0), 2));
        opponentBoard.placeShip(Ship.createHorizontal(new Position(2, 2), 1));
        personalizedGame.shootAt(new Position(0, 0));
        new PersonalizedGame(opponent, game).shootAt(new Position(0, 0));
        personalizedGame.shootAt(new Position(0, 1));
        new PersonalizedGame(opponent, game).shootAt(new Position(2, 2));
        personalizedGame.shootAt(new Position(2, 2));

        List<List<BoardPosition>> boardPositions = personalizedGame.getOpponentsBoardPositions();

        assertThat(boardPositions.get(0).get(0).getKind(), is(BoardPosition.Kind.SHIP_HIT));
        assertThat(boardPositions.get(2).get(2).getKind(), is(BoardPosition.Kind.SHIP_SUNK));
        assertThat(boardPositions.get(0).get(1).getKind(), is(BoardPosition.Kind.UNKNOWN));
        assertThat(boardPositions.get(1).get(0).getKind(), is(BoardPosition.Kind.WATER_HIT));
    }

    @Test
    public void testShootingTheLastShipFinishesTheGameAndSetsTheWinFlag() {
        opponentBoard.placeShip(Ship.createHorizontal(new Position(0, 0), 1));
        PersonalizedGame opponentPersonalizedGame = new PersonalizedGame(opponent, game);

        personalizedGame.shootAt(new Position(0, 0));

        assertThat(personalizedGame.isFinished(), is(true));
        assertThat(personalizedGame.isWon(), is(true));
        assertThat(opponentPersonalizedGame.isFinished(), is(true));
        assertThat(opponentPersonalizedGame.isWon(), is(false));
    }
}