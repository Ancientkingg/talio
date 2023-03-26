package client.services;

import client.exceptions.BoardChangeException;
import client.models.BoardModel;
import commons.Board;
import commons.Card;
import commons.Column;

import javax.inject.Inject;

public class BoardService {
    private final BoardModel boardModel;
    private final ServerService serverService;

    /**
     * Constructs a board service
     * @param boardModel the injected board model
     * @param serverService the injected server service
     */
    @Inject
    public BoardService(final BoardModel boardModel, final ServerService serverService) {
        this.boardModel = boardModel;
        this.serverService = serverService;
        this.setServerIP("http://localhost:8080");
    }

    /**
     * Sets the IP of the server to interact with
     * @param ip the ip of the server
     */
    public void setServerIP(final String ip) {
        serverService.setServerIP(ip);
    }

    /**
     * Adds a new board
     * @param board the board to add
     * @return the board returned by the server
     * @throws BoardChangeException if the board cannot be added
     */
    public Board addBoard(final Board board) throws BoardChangeException {
        boardModel.addBoard(board);
        return serverService.addBoard(board);
    }

    /**
     * Fetches a board by join-key
     * @param joinKey the join-key used to identify the board
     * @return the board that was retrieved
     */
    public Board fetchBoard(final String joinKey) {
        return serverService.getBoard(joinKey);
    }

    /**
     * Sets the current board
     * @param board the board to set as current
     */
    public void setCurrentBoard(final Board board) {
        boardModel.setCurrentBoard(board);
    }

    /**
     * Gets the current board
     * @return the current board
     */
    public Board getCurrentBoard() {
        return boardModel.getCurrentBoard();
    }

    /**
     * Adds a column to the currently selected board
     * @param column the column to add
     * @return the column returned by the server
     * @throws BoardChangeException if the column cannot be added
     */
    public Column addColumnToCurrentBoard(final Column column) throws BoardChangeException {
        boardModel.addColumn(column);
        return serverService.addColumn(this.boardModel.getCurrentBoard(), column);
    }

    /**
     * Removes a column from the currently selected board
     * @param column the column to remove
     * @return the removed column returned by the server
     * @throws BoardChangeException if the column cannot be removed
     */
    public Column removeColumnFromCurrentBoard(final Column column) throws BoardChangeException {
        boardModel.removeColumn(column);
        return serverService.removeColumn(this.boardModel.getCurrentBoard(), column);
    }

    /**
     * Adds a card to the specified column of the currently selected board
     * @param card the card to add
     * @param column the column to add the card to
     * @return the card returned by the server
     * @throws BoardChangeException if the card could not be added
     */
    public Card addCardToColumn(final Card card, final Column column) throws BoardChangeException {
        boardModel.addCard(card, column);
        return serverService.addCard(this.boardModel.getCurrentBoard(), column, card);
    }

    /**
     * Removes a card from the specified column of the currently selected board
     * @param card the card to remove
     * @param column the column to remove the card from
     * @return the removed card returned by the server
     * @throws BoardChangeException if the card could not be removed
     */
    public Card removeCardFromColumn(final Card card, final Column column) throws BoardChangeException {
        boardModel.removeCard(card, column);
        return serverService.removeCard(this.boardModel.getCurrentBoard(), column, card);
    }

    /**
     * Moves a card from one column to another
     * @param cardIdx the index of the card to move
     * @param columnFromIdx the index of the column to move the card from
     * @param columnToIdx the index of the column to move the card to
     * @param priority the priority of the card in the new column
     */
    public void moveCard(final long cardIdx, final long columnFromIdx, final long columnToIdx, final int priority) {
        boardModel.moveCard(cardIdx, columnFromIdx, columnToIdx, priority);
    }


    /**
     * Updates a column
     * @param column the column to update
     */
    public void updateColumn(final Column column) {
        boardModel.updateColumn(column);
    }
}
