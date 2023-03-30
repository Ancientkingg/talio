package client.services;

import client.exceptions.BoardChangeException;
import client.models.BoardModel;
import client.scenes.MainCtrl;
import commons.Board;
import commons.Card;
import commons.Column;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BoardService {
    private final BoardModel boardModel;
    private final ServerService serverService;
    private final MainCtrl mainCtrl;

    /**
     * Constructs a board service
     *
     * @param boardModel    the injected board model
     * @param serverService the injected server service
     * @param mainCtrl      the injected mainCtrl
     */
    @Inject
    public BoardService(final BoardModel boardModel, final ServerService serverService, final MainCtrl mainCtrl) {
        this.boardModel = boardModel;
        this.serverService = serverService;
        this.mainCtrl = mainCtrl;
        this.setServerIP("http://localhost:8080");
        this.startSocket();
    }

    private void startSocket() {
        serverService.startSocket(this);
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
     * Adds a column to the currently selected board (client initiated)
     * @param column the column to add
     * @return the column returned by the server
     * @throws BoardChangeException if the column cannot be added
     */
    public Column addColumnToCurrentBoard(final Column column) throws BoardChangeException {
        final Column serverColumn = serverService.addColumn(this.boardModel.getCurrentBoard(), column);
        boardModel.addColumn(serverColumn);
        return serverColumn;
    }

    /**
     * Adds a column to the currently selected board (server initiated)
     * @param column the column to add
     * @throws BoardChangeException if the column cannot be added
     */
    public void updateAddColumnToCurrentBoard(final Column column) throws BoardChangeException {
        boardModel.addColumn(column);
        mainCtrl.refreshOverview();
    }

    /**
     * Removes a column from the currently selected board (client initiated)
     * @param column the column to remove
     * @return the removed column returned by the server
     * @throws BoardChangeException if the column cannot be removed
     */
    public Column removeColumnFromCurrentBoard(final Column column) throws BoardChangeException {
        final Column serverColumn = serverService.removeColumn(this.boardModel.getCurrentBoard(), column);
        boardModel.removeColumn(serverColumn);
        return serverColumn;
    }

    /**
     * Removes a column from the currently selected board (client initiated)
     * @param id Long id of the column to remove
     * @throws BoardChangeException if the column cannot be removed
     */
    public void updateRemoveColumnFromCurrentBoard(final Long id) throws BoardChangeException {
        final Column column = boardModel.getCurrentBoard().getColumnById(id);
        boardModel.removeColumn(column);
        mainCtrl.refreshOverview();
    }

    /**
     * Adds a card to the specified column of the currently selected board (client initiated)
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
     * Adds a card to the specified column of the currently selected board (server initiated)
     * @param card the card to add
     * @param column the column to add the card to
     * @throws BoardChangeException if the card could not be added
     */
    public void updateAddCardToColumn(final Card card, final Column column) throws BoardChangeException {
        boardModel.addCard(card, column);
        mainCtrl.refreshOverview();
    }

    /**
     * Removes a card from the specified column of the currently selected board (client initiated)
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
     * Removes a card from the specified column of the currently selected board (server initiated)
     * @param card the card to remove
     * @param column the column to remove the card from
     * @throws BoardChangeException if the card could not be removed
     */
    public void updateRemoveCardFromColumn(final Card card, final Column column) throws BoardChangeException {
        boardModel.removeCard(card, column);
        mainCtrl.refreshOverview();
    }

    /**
     * Moves a card from one column to another (client initiated)
     * @param cardIdx the index of the card to move
     * @param columnFromIdx the index of the column to move the card from
     * @param columnToIdx the index of the column to move the card to
     * @param priority the priority of the card in the new column
     * @return Card moved to new position
     */
    public void repositionCard(final long cardIdx, final long columnFromIdx, final long columnToIdx, final int priority) {
        boardModel.moveCard(cardIdx, columnFromIdx, columnToIdx, priority);

        final Board board = this.boardModel.getCurrentBoard();

        serverService.repositionCard(board, board.getColumn(columnFromIdx), board.getColumn(columnToIdx), board.getCard(cardIdx), priority);
    }

    /**
     * Moves a card from one column to another (server initiated)
     * @param cardIdx the index of the card to move
     * @param columnFromIdx the index of the column to move the card from
     * @param columnToIdx the index of the column to move the card to
     * @param priority the priority of the card in the new column
     */
    public void updateRepositionCard(final long cardIdx, final long columnFromIdx, final long columnToIdx, final int priority) {
        boardModel.moveCard(cardIdx, columnFromIdx, columnToIdx, priority);
        mainCtrl.refreshOverview();
    }

    /**
     * Updates a column
     * @param column the column to update
     */
    public void updateColumn(final Column column) {
        boardModel.updateColumn(column);
    }

    public void renameColumn(final Column column, final String newName) {
        boardModel.renameColumn(column, newName);
        serverService.renameColumn(getCurrentBoard(), column);
    }

    public void updateRenameColumn(final Column column, final String newName) {
        boardModel.renameColumn(column, newName);
    }

    /**
     * Rename board function for local changes
     * @param newName String new board name
     */
    public void renameBoard(final String newName) {
        boardModel.renameBoard(newName);
        // needs to call server service to forward change to server
    }

    /**
     * Rename board function for server changes
     * @param newName String new board name
     */
    public void updateRenameBoard(final String newName) {
        boardModel.renameBoard(newName);
        mainCtrl.refreshOverview();
    }

    public void editCard(final Card card, final Column column) {
//        boardModel.editCard(card, column);
        serverService.editCard(getCurrentBoard(), card, column);
    }

    public void updateEditCard(final Card card, final Column column) {
//        boardModel.editCard(card, column);
    }
}
