package client.services;

import client.exceptions.BoardChangeException;
import client.exceptions.ServerException;
import client.models.BoardModel;
import client.scenes.MainCtrl;
import client.scenes.components.InfoModal;
import commons.Board;
import commons.Card;
import commons.Column;
import commons.Tag;
import commons.exceptions.ColumnNotFoundException;

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
     *
     * @param boardModel    the injected board model
     * @param serverService the injected server service
     * @param mainCtrl      the injected mainCtrl
     */
    public BoardService(final BoardModel boardModel, final ServerService serverService, final MainCtrl mainCtrl) {
        this.boardModel = boardModel;
        this.serverService = serverService;
        this.mainCtrl = mainCtrl;
        this.setServerIP("http://localhost:8080"); // Default server IP
    }

    /**
     * Connects to the server
     *
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
        serverService.checkConnection();
    }

    /**
     * Stops the websocket connection for the set server URL
     */
    private void stopSocket() {
        serverService.stopSocket();
    }

    /**
     * Sets the IP of the server to interact with
     *
     * @param ip the ip of the server
     */
    public void setServerIP(final String ip) {
        String serverIP = ip;
        if (!ip.startsWith("http://")) serverIP = "http://" + serverIP;
        if (!ip.contains(":")) serverIP = serverIP + ":8080";
        serverService.setServerIP(serverIP);
    }

    /**
     * gets the server IP from server service
     *
     * @return current server IP address
     */
    public URI getServerIP() {
        return serverService.getServerIP();
    }

    /**
     * Adds a new board
     *
     * @param board the board to add
     *
     * @return the board returned by the server
     *
     * @throws BoardChangeException if the board cannot be added
     */
    public Board addBoard(final Board board) {
        try {
            final Board serverBoard = serverService.addBoard(board);
            boardModel.addBoard(serverBoard);
            return serverBoard;
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this,
                    "Server Exception", "The Board couldn't be added to the Server: " + serverService.getServerIP()
                    , mainCtrl.getCurrentScene());
            errorModal.showModal();
            return null;
        }
    }

    /**
     * Returns all boards on the client
     *
     * @return the boards present on the client
     */
    public List<Board> getAllBoards() {
        return boardModel.getBoardList();
    }

    /**
     * Fetches a board by join-key
     *
     * @param joinKey the join-key used to identify the board
     *
     * @return the board that was retrieved
     */
    public Board fetchBoard(final String joinKey) {
        try {
            return serverService.getBoard(joinKey);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this,
                    "Server Exception", "The Board couldn't be retrieved from the Server: " + serverService.getServerIP()
                    , mainCtrl.getCurrentScene());
            errorModal.showModal();
            return null;
        }
    }

    /**
     * Fetches all boards
     *
     * @param joinKeys the join-keys used to identify the boards
     *
     * @return the boards that were retrieved
     */
    public List<Board> fetchAllBoards(final List<String> joinKeys) {
        try {
            return serverService.getAllBoards(joinKeys);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this,
                    "Server Exception", "The Boards couldn't be retrieved from the Server: " + serverService.getServerIP()
                    , mainCtrl.getCurrentScene());
            errorModal.showModal();
            return null;
        }
    }

    /**
     * Sets the current board
     *
     * @param board the board to set as current
     */
    public void setCurrentBoard(final Board board) {
        boardModel.setCurrentBoard(board);
    }

    /**
     * Gets the current board
     *
     * @return the current board
     */
    public Board getCurrentBoard() {
        return boardModel.getCurrentBoard();
    }

    /**
     * Adds a column to the currently selected board (client initiated)
     *
     * @param column the column to add
     *
     * @throws BoardChangeException if the column cannot be added
     */
    public void addColumnToCurrentBoard(final Column column) {
        try {
            serverService.addColumn(this.boardModel.getCurrentBoard(), column);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The column couldn't be added to the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a column to the currently selected board (server initiated)
     *
     * @param column the column to add
     *
     * @throws BoardChangeException if the column cannot be added
     */
    public void updateAddColumnToCurrentBoard(final Column column) throws BoardChangeException {
        boardModel.addColumn(column);
        mainCtrl.refreshOverview();
    }

    /**
     * Removes a column from the currently selected board (client initiated)
     *
     * @param column the column to remove
     *
     * @throws BoardChangeException if the column cannot be removed
     */
    public void removeColumnFromCurrentBoard(final Column column) {
        try {
            serverService.removeColumn(this.boardModel.getCurrentBoard(), column);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The column couldn't be removed from the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a column from the currently selected board (client initiated)
     *
     * @param id Long id of the column to remove
     *
     * @throws BoardChangeException if the column cannot be removed
     */
    public void updateRemoveColumnFromCurrentBoard(final Long id) throws BoardChangeException, ColumnNotFoundException {
        final Column column = boardModel.getCurrentBoard().getColumnById(id);
        boardModel.removeColumn(column);
        mainCtrl.refreshOverview();
    }

    /**
     * Adds a card to the specified column of the currently selected board (client initiated)
     *
     * @param card   the card to add
     * @param column the column to add the card to
     *
     * @throws BoardChangeException if the card could not be added
     */
    public void addCardToColumn(final Card card, final Column column) {
        try {
            serverService.addCard(this.boardModel.getCurrentBoard(), column, card);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The card couldn't be added to the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds a card to the specified column of the currently selected board (server initiated)
     *
     * @param card   the card to add
     * @param column the column to add the card to
     *
     * @throws BoardChangeException if the card could not be added
     */
    public void updateAddCardToColumn(final Card card, final Column column) throws BoardChangeException {
        boardModel.addCard(card, column);
        mainCtrl.refreshOverview();
    }

    /**
     * Removes a card from the specified column of the currently selected board (client initiated)
     *
     * @param card   the card to remove
     * @param column the column to remove the card from
     *
     * @throws BoardChangeException if the card could not be removed
     */
    public void removeCardFromColumn(final Card card, final Column column) {
        try {
            serverService.removeCard(this.boardModel.getCurrentBoard(), column, card);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The card couldn't be removed from the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a card from the specified column of the currently selected board (server initiated)
     *
     * @param card   the card to remove
     * @param column the column to remove the card from
     *
     * @throws BoardChangeException if the card could not be removed
     */
    public void updateRemoveCardFromColumn(final Card card, final Column column) throws BoardChangeException {
        boardModel.removeCard(card, column);
        mainCtrl.refreshOverview();
    }

    /**
     * Moves a card from one column to another (client initiated)
     *
     * @param cardIdx       the index of the card to move
     * @param columnFromIdx the index of the column to move the card from
     * @param columnToIdx   the index of the column to move the card to
     * @param priority      the priority of the card in the new column
     */
    public void repositionCard(final long cardIdx, final long columnFromIdx, final long columnToIdx, final int priority) {
        try {
            final Board board = this.boardModel.getCurrentBoard();
            serverService.repositionCard(board, board.getColumn(columnFromIdx), board.getColumn(columnToIdx), board.getCard(cardIdx), priority);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception",
                    "The card couldn't be repositioned on the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Moves a card from one column to another (server initiated)
     *
     * @param cardIdx       the index of the card to move
     * @param columnFromIdx the index of the column to move the card from
     * @param columnToIdx   the index of the column to move the card to
     * @param priority      the priority of the card in the new column
     */
    public void updateRepositionCard(final long cardIdx, final long columnFromIdx, final long columnToIdx, final int priority) throws BoardChangeException {
        boardModel.moveCard(cardIdx, columnFromIdx, columnToIdx, priority);
        mainCtrl.refreshOverview();
    }

    /**
     * Updates a column
     *
     * @param column the column to update
     */
    public void updateColumn(final Column column) throws ColumnNotFoundException {
        boardModel.updateColumn(column);
    }

    /**
     * Renames column to new name (client initiated)
     *
     * @param column  Column to rename
     * @param newName String new name
     */
    public void renameColumn(final Column column, final String newName) {
        try {
            serverService.renameColumn(getCurrentBoard(), column, newName);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The column couldn't be renamed on the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Renames column to new name (server initiated)
     *
     * @param column  Column to rename
     * @param newName String new name
     */
    public void updateRenameColumn(final Column column, final String newName) {
        boardModel.renameColumn(column, newName);
        mainCtrl.refreshColumnHeading(column.getId());
    }

    /**
     * Adds tag to board (client initiated)
     *
     * @param tag Tag to add
     */
    public void addTagToBoard(final Tag tag) {
        final Tag addedTag = serverService.addTagToBoard(getCurrentBoard(), tag);
        // Update the boardModel with addedTag here
    }

    /**
     * Adds tag to board (server initiated)
     *
     * @param tag Tag to add
     */
    public void updateAddTagToBoard(final Tag tag) {
    }

    /**
     * Removes tag from board (client initiated)
     *
     * @param tag Tag to remove
     */
    public void removeTagFromBoard(final Tag tag) {
        final Tag removedTag = serverService.removeTagFromBoard(getCurrentBoard(), tag);
        // Update the boardModel with deletedTag here
    }

    /**
     * Removes tag from board (server initiated)
     *
     * @param tag Tag to remove
     */
    public void updateRemoveTagFromBoard(final Tag tag) {
    }

    /**
     * Edits tag in board (client initiated)
     *
     * @param tag Tag to edit
     */
    public void editTag(final Tag tag) {
        serverService.editTag(getCurrentBoard(), tag);
        // Update the boardModel with editedTag here
    }

    /**
     * Edits tag in board (server initiated)
     *
     * @param tag Tag to edit
     */
    public void updateEditTag(final Tag tag) {
    }

    /**
     * Adds tag to card (client initiated)
     *
     * @param card Card added to
     * @param tag  Tag to add
     */
    public void addTagToCard(final Card card, final Tag tag) {
        final Tag addedTag = serverService.addTagToCard(getCurrentBoard(), card, tag);
        // Update the boardModel with addedTag here
    }

    /**
     * Adds tag to card (server initiated)
     *
     * @param card Card added to
     * @param tag  Tag to add
     */
    public void updateAddTagToCard(final Card card, final Tag tag) {
    }

    /**
     * Removes tag from card (client initiated)
     *
     * @param card Card removed from
     * @param tag  Tag to remove
     */
    public void removeTagFromCard(final Card card, final Tag tag) {
        final Tag removedTag = serverService.removeTagFromCard(getCurrentBoard(), card, tag);
        // Update the boardModel with removedTag here
    }

    /**
     * Removes tag from card (client initiated)
     *
     * @param card Card removed from
     * @param tag  Tag to remove
     */
    public void updateRemoveTagFromCard(final Card card, final Tag tag) {
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
     *
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
     *
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
     *
     * @param newName String new board name
     */
    public void renameBoard(final String newName) {
        boardModel.renameBoard(newName);
        // needs to call server service to forward change to server
    }

    /**
     * Rename board function for server changes
     *
     * @param newName String new board name
     */
    public void updateRenameBoard(final String newName) {
        boardModel.renameBoard(newName);
        mainCtrl.refreshOverview();
    }

    /**
     * Currently not functional, but connects to socket.
     * Changes card title, description, and tags (client initiated)
     *
     * @param card   Card to edit
     * @param column Column that card is in
     */
    public void editCard(final Card card, final Column column) {
        try {
            serverService.editCard(getCurrentBoard(), card, column);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The card couldn't be edited on the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Currently not functional, but connects to socket.
     * Changes card title, description, and tags (server initiated)
     *
     * @param card   Card to edit
     * @param column Column that card is in
     */
    public void updateEditCard(final Card card, final Column column) {
//        boardModel.editCard(card, column);
    }

    /**
     * Subscribes to loaded board
     *
     * @param joinKey String key to board
     */
    public void subscribeToBoard(final String joinKey) {
        serverService.subscribeToBoard(joinKey);
    }

    /**
     * This is used to assign a newly created column its index value
     *
     * @return int index
     */
    public int getHighestIndex() {
        return getCurrentBoard().getColumns().size();
    }

    /**
     * Removes a board from the client side
     *
     * @param board Board to remove
     *
     * @return true if board was removed
     */
    public boolean removeBoard(final Board board) {
        return this.boardModel.getBoardList().remove(board);
    }

    /**
     * Deletes a board from the server and client side
     *
     * @param board Board to delete
     *
     * @return true if board was deleted
     */
    public boolean deleteBoard(final Board board) {
        this.removeBoard(board);
        return serverService.deleteBoard(board);
    }

    /**
     * Verifies password provided by user for switching to admin mode
     *
     * @param adminPassword Password provided by user
     *
     * @return correct/incorrect
     */
    public boolean verifyAdminPassword(final String adminPassword) {
        return serverService.verifyAdminPassword(adminPassword);
    }

    /**
     * Gets all Boards in the database from the server
     *
     * @return List of all Boards
     */
    public List<Board> adminGetAllBoards() {
        return serverService.adminGetAllBoards();
    }

    /**
     * Loads all the Boards in the server for admin view
     */
    public void adminLoadAllBoards() {
        final List<Board> boards = this.adminGetAllBoards();
        this.boardModel.setBoardList(boards.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    /**
     * Removes all saved boards (used for clearing cache)
     */
    public void removeAllBoards() {
        this.boardModel.getBoardList().clear();
    }

    /**
     * Checks if the boards are still valid
     */
    public void checkBoardsValidity() {
        final List<String> validBoards = serverService.getBoardsStatus(
                this.boardModel.getBoardList()
                        .stream()
                        .map(Board::getJoinKey)
                        .toList());

        this.boardModel.setBoardList(
                this.boardModel.getBoardList()
                        .stream()
                        .filter(board -> validBoards.contains(board.getJoinKey()))
                        .collect(Collectors.toList())
        );
        this.saveBoardsLocal();
    }
}
