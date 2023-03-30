package client.services;

import client.exceptions.BoardChangeException;
import client.models.BoardModel;
import client.scenes.MainCtrl;
import commons.Board;
import commons.Card;
import commons.Column;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class BoardService {
    private BoardModel boardModel;
    private final ServerService serverService;
    private final MainCtrl mainCtrl;

    /**
     * Constructs a board service
     *
     * @param serverService the injected server service
     * @param mainCtrl      the injected mainCtrl
     */
    @Inject
    public BoardService(final ServerService serverService, final MainCtrl mainCtrl) {
        this.serverService = serverService;
        this.mainCtrl = mainCtrl;
        this.setServerIP("http://localhost:8080"); // Default server IP
    }

    /**
     * Constructs a board service
     * @param boardModel the injected board model
     * @param serverService the injected server service
     * @param mainCtrl the injected mainCtrl
     */
    public BoardService(final BoardModel boardModel, final ServerService serverService, final MainCtrl mainCtrl) {
        this.boardModel = boardModel;
        this.serverService = serverService;
        this.mainCtrl = mainCtrl;
        this.setServerIP("http://localhost:8080"); // Default server IP
    }

    /**
     * Connects to the server
     * @param serverIP the ip of the server to connect to
     */
    public void connect(final String serverIP) {
        this.boardModel = new BoardModel();
        this.setServerIP(serverIP);
        this.startSocket();
    }

    /**
     * Disconnects from the server
     */
    public void disconnect() {
        this.boardModel = null;
        this.stopSocket();
    }

    /**
     * Starts the websocket connection for the set server URL
     */
    private void startSocket() {
        serverService.startSocket(this);
    }

    /**
     * Stops the websocket connection for the set server URL
     */
    private void stopSocket() {
        serverService.stopSocket();
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
        final Board serverBoard = serverService.addBoard(board);
        boardModel.addBoard(serverBoard);
        return serverBoard;
    }

    /**
     * Adds a board to the board list (server initiated)
     * @return the board returned by the server
     */
    public List<Board> getAllBoards() {
        return boardModel.getBoardList();
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
     * Fetches all boards
     * @param joinKeys the join-keys used to identify the boards
     * @return the boards that were retrieved
     */
    public List<Board> fetchAllBoards(final List<String> joinKeys) {
        return serverService.getAllBoards(joinKeys);
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
    public Card moveCard(final long cardIdx, final long columnFromIdx, final long columnToIdx, final int priority) {
        boardModel.moveCard(cardIdx, columnFromIdx, columnToIdx, priority);

        final Board board = this.boardModel.getCurrentBoard();

        return serverService.updatePosition(board, board.getColumn(columnFromIdx), board.getColumn(columnToIdx), board.getCard(cardIdx), priority);
    }

    /**
     * Moves a card from one column to another (server initiated)
     * @param cardIdx the index of the card to move
     * @param columnFromIdx the index of the column to move the card from
     * @param columnToIdx the index of the column to move the card to
     * @param priority the priority of the card in the new column
     */
    public void updateMoveCard(final long cardIdx, final long columnFromIdx, final long columnToIdx, final int priority) {
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

    /**
     * Saves joined or created boards to local storage
     */
    public void saveBoardsLocal() {
        final ArrayList<String> joinKeys = new ArrayList<>();

        for (final Board board : boardModel.getBoardList()) {
            joinKeys.add(board.getJoinKey());
        }

        final Map<URI, List<String>> allBoardJoinKeys = loadAllJoinKeysLocal();
        allBoardJoinKeys.put(serverService.getServerIP(), joinKeys);

        try {
            final FileOutputStream fileOutputStream = new FileOutputStream("saved-boards");
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(allBoardJoinKeys);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing stream");
        }
    }

    /**
     * Loads boards for the current server from local storage
     * @return the list of boards for the current server
     */
    public List<Board> loadBoardsForCurrentServer() {

        final Map<URI, List<String>> allBoardJoinKeys = loadAllJoinKeysLocal();

        if (allBoardJoinKeys.size() == 0) {
            return new ArrayList<>();
        }

        final List<String> joinKeys = allBoardJoinKeys.get(serverService.getServerIP());


        final List<Board> boards = this.fetchAllBoards(joinKeys);

        this.boardModel.setBoardList(boards.stream().filter(Objects::nonNull).collect(Collectors.toList()));


        return boards;
    }

    /**
     * Loads all join keys from local storage
     * @return a map of server URIs to lists of join keys
     */
    public Map<URI, List<String>> loadAllJoinKeysLocal() {
        try {
            final FileInputStream fileInputStream = new FileInputStream("saved-boards");
            final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            final Map<URI, List<String>> allBoardJoinKeys = (Map<URI, List<String>>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();


            return allBoardJoinKeys;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error initializing stream");
        }
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
}
