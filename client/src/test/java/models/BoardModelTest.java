package models;

import client.exceptions.BoardChangeException;
import client.models.BoardModel;
import commons.Board;
import commons.Card;
import commons.Column;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardModelTest {

    private BoardModel boardModel;

    @BeforeEach
    public void setup() {
        this.boardModel = new BoardModel();
    }

    @Test
    public void testCurrentBoard() {
        Board currentBoard = new Board("join-key", "title", "password", null);
        boardModel.setCurrentBoard(currentBoard);

        assertEquals(boardModel.getCurrentBoard(), currentBoard);
    }

    @Test
    public void testAddCard() throws BoardChangeException {
        Board currentBoard = new Board("join-key", "title", "password", new TreeSet<>());
        boardModel.setCurrentBoard(currentBoard);

        Column column = new Column("heading", 0, new TreeSet<>());
        currentBoard.addColumn(column);

        Card card = new Card("title", 0, "description", null);
        boardModel.addCard(card, column);

        assertEquals(column.getCards().size(), 1);
        assertEquals(column.getCards().first(), card);
    }

    @Test
    public void testRemoveCard() throws BoardChangeException {
        Board currentBoard = new Board("join-key", "title", "password", new TreeSet<>());
        boardModel.setCurrentBoard(currentBoard);

        Column column = new Column("heading", 0, new TreeSet<>());
        currentBoard.addColumn(column);

        Card card = new Card("title", 0, "description", new HashSet<>());
        column.addCard(card);

        boardModel.removeCard(card, column);

        assertEquals(column.getCards().size(), 0);
    }

    @Test
    public void testAddColumn() throws BoardChangeException {
        Board currentBoard = new Board("join-key", "title", "password", new TreeSet<>());
        boardModel.setCurrentBoard(currentBoard);

        Column column = new Column("heading", 0, new TreeSet<>());
        boardModel.addColumn(column);

        assertEquals(currentBoard.getColumns().size(), 1);
        assertEquals(currentBoard.getColumns().first(), column);
    }

    @Test
    public void testRemoveColumn() throws BoardChangeException {
        Board currentBoard = new Board("join-key", "title", "password", new TreeSet<>());
        boardModel.setCurrentBoard(currentBoard);

        Column column = new Column("heading", 0, new TreeSet<>());
        currentBoard.addColumn(column);

        boardModel.removeColumn(column);

        assertEquals(currentBoard.getColumns().size(), 0);
    }

    @Test
    public void testMoveCard() {
        Board currentBoard = new Board("join-key", "title", "password", new TreeSet<>());
        boardModel.setCurrentBoard(currentBoard);

        Column column1 = new Column(0, 0, "heading1", new TreeSet<>());
        Column column2 = new Column(1, 1, "heading2", new TreeSet<>());
        currentBoard.addColumn(column1);
        currentBoard.addColumn(column2);

        Card card = new Card("title", 0, "description", null);
        column1.addCard(card);

        boardModel.moveCard(card.getId(), column1.getIndex(), column2.getIndex(), 0);

        assertEquals(column1.getCards().size(), 0);
        assertEquals(column2.getCards().size(), 1);
        assertEquals(column2.getCards().first(), card);
    }

    @Test
    public void testUpdateColumn() {
        Board currentBoard = new Board("join-key", "title", "password", new TreeSet<>());
        boardModel.setCurrentBoard(currentBoard);

        Column column = new Column("heading", 0, new TreeSet<>());
        currentBoard.addColumn(column);

        Column newColumn = new Column("newHeading", 0, new TreeSet<>());

        boardModel.updateColumn(newColumn);

        assertEquals(currentBoard.getColumns().size(), 1);
        assertEquals(currentBoard.getColumns().first(), newColumn);
    }
}
