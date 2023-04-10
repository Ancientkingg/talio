package client.services;

import client.exceptions.BoardChangeException;
import client.exceptions.ServerException;
import client.models.BoardModel;
import client.scenes.MainCtrl;
import client.scenes.components.modals.InfoModal;
import commons.*;
import commons.exceptions.CardNotFoundException;
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
     * Boolean returning whether the session is connected
     * @return isConnected
     */
    public boolean isConnected() {
        return serverService.isConnected();
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
     * @param boards the join-keys used to identify the boards
     *
     * @return the boards that were retrieved
     */
    public List<Board> fetchAllBoards(final HashMap<String, String> boards) {
        try {
            return serverService.getAllBoards(boards);
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
        if (mainCtrl.getCardDetailsModal() != null) {
            mainCtrl.getCardDetailsModal().closeModal();
        }
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
        } catch (CardNotFoundException e) {
            final InfoModal errorModal = new InfoModal(this, "Card Not Found",
                    "The card you are trying to reposition could not be found on the Server.", mainCtrl.getCurrentScene());
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
     * Adds tag to current board (client initiated)
     * @param tag Tag to add
     */
    public void addTagToCurrentBoard(final Tag tag) {
        try {
            serverService.addTagToBoard(getCurrentBoard(), tag);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The tag couldn't be added to the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds tag to board (server initiated)
     *
     * @param tag Tag to add
     */
    public void updateAddTagToBoard(final Tag tag) {
        boardModel.getCurrentBoard().addTag(tag);
        if (mainCtrl.getTagsOverviewModal() != null) mainCtrl.getTagsOverviewModal().refresh();
    }

    /**
     * Removes tag from board (client initiated)
     *
     * @param tag Tag to remove
     */
    public void removeTagFromBoard(final Tag tag) {
        try {
            serverService.removeTagFromBoard(getCurrentBoard(), tag);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The tag couldn't be removed from the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }

    }

    /**
     * Removes tag from board (server initiated)
     *
     * @param tag Tag to remove
     */
    public void updateRemoveTagFromBoard(final Tag tag) {
        boardModel.removeTag(tag, getCurrentBoard());

        boardModel.getCurrentBoard().getColumns().forEach(column -> column.getCards().forEach(card -> card.removeTag(tag)));

        mainCtrl.refreshOverview();
        if (mainCtrl.getTagsOverviewModal() != null) mainCtrl.getTagsOverviewModal().refresh();
    }

    /**
     * Edits tag in board (client initiated)
     *
     * @param tag Tag to edit
     */
    public void editTag(final Tag tag) {
        try {
            serverService.editTag(getCurrentBoard(), tag);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception", "The tag couldn't be edited on the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Edits tag in board (server initiated)
     *
     * @param tag Tag to edit
     */
    public void updateEditTag(final Tag tag) {
        boardModel.getCurrentBoard().updateTag(tag);
        System.out.println("EDITED CARD!!!");
        if (mainCtrl.getTagsOverviewModal() != null) mainCtrl.getTagsOverviewModal().refresh();
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
        final HashMap<String, String> boards = new HashMap<>();

        for (final Board board : boardModel.getBoardList()) {
            boards.put(board.getJoinKey(), board.getPassword());
        }

        final Map<URI, HashMap<String, String>> allBoardJoinKeys = loadAllBoardsLocal();
        allBoardJoinKeys.put(serverService.getServerIP(), boards);

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

        final Map<URI, HashMap<String, String>> allBoardJoinKeys = loadAllBoardsLocal();

        if (allBoardJoinKeys.size() == 0) {
            return new ArrayList<>();
        }

        final HashMap<String, String> joinKeys = allBoardJoinKeys.get(serverService.getServerIP());


        final List<Board> boards = this.fetchAllBoards(joinKeys);

        this.boardModel.setBoardList(boards.stream().filter(Objects::nonNull).collect(Collectors.toList()));


        return boards;
    }

    /**
     * Loads all join keys from local storage
     *
     * @return a map of server URIs to lists of join keys
     */
    public Map<URI, HashMap<String, String>> loadAllBoardsLocal() {
        try {
            final FileInputStream fileInputStream = new FileInputStream("saved-boards");
            final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            final Map<URI, HashMap<String, String>> allBoards =
                    (Map<URI, HashMap<String, String>>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();


            return allBoards;
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
        serverService.renameBoard(getCurrentBoard(), newName);
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
     * Changes card title, description, and tags (server initiated)
     *
     * @param card   Card to edit
     */
    public void updateEditCard(final Card card) throws CardNotFoundException {
        boardModel.editCard(card);
        mainCtrl.getOverviewCtrl().refreshCard(card.getId());
        if (mainCtrl.getCardDetailsModal() != null) mainCtrl.getCardDetailsModal().refresh();
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

    /**
     * adds subtask to card (client initiated)
     * @param card card to which subtask is to be added
     * @param description description of subtask to be added
     */
    public void addSubTask(final Card card, final String description) {
        // TODO update boardModel to show changes
        serverService.addSubTask(this.boardModel.getCurrentBoard(), card, description);
    }

    /**
     * removes subtask from card (client initiated)
     * @param card card from which subtask is to be removed
     * @param subTask subtask to remove
     */
    public void removeSubTask(final Card card, final SubTask subTask) {
        // TODO update boardModel to show changes
        serverService.removeSubTask(this.boardModel.getCurrentBoard(), card, subTask);
    }

    /**
     * toggles done/not done of subtask in card (server initiated)
     * @param card card whose subtask is to be toggled
     * @param subTask subtask to toggle
     */
    public void toggleSubTask(final Card card, final SubTask subTask) {
        // TODO update boardModel to show changes
        serverService.toggleSubTask(this.boardModel.getCurrentBoard(), card, subTask);
    }

    /**
     * moves a subtask within a card
     * @param card containing the subtask
     * @param subTask to be moved
     * @param index new index of subtask
     */
    public void moveSubCard(final Card card, final SubTask subTask, final int index) {
        // TODO update boardModel
        serverService.moveSubTask(this.boardModel.getCurrentBoard(), card, subTask, index);
    }


    /**
     * adds subtask to card (server initiated)
     * @param card card to which subtask is to be added
     * @param subTask subtask to add
     */
    public void updateAddSubTask(final Card card, final SubTask subTask) {
        // TODO update boardModel to show changes
    }

    /**
     * removes subtask from card (server initiated)
     * @param card card from which subtask is to be removed
     * @param subTask subtask to remove
     */
    public void updateRemoveSubTask(final Card card, final SubTask subTask) {
        // TODO update boardModel to show changes
    }

    /**
     * toggles done/not done of subtask in card (server initiated)
     * @param card card whose subtask is to be toggled
     * @param subTask subtask to toggle
     */
    public void updateToggleSubTask(final Card card, final SubTask subTask) {
        // TODO update boardModel to show changes
    }

    /**
     * moves a subtask within a card
     * @param card containing the subtask
     * @param subTask to be moved
     * @param index new index of subtask
     */
    public void updateMoveSubTask(final Card card, final SubTask subTask, final int index) {
        // TODO update boardModel
    }

    /**
     * Edits a color preset and sends it to the server
     * @param colorPreset Color preset to edit
     */
    public void editColorPreset(final ColorScheme colorPreset) {
        try {
            serverService.editColorPreset(getCurrentBoard(), colorPreset);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception",
                    "The color preset couldn't be edited on the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a color preset from the current board
     * @param colorPreset Color preset to remove
     */
    public void removeColorPresetFromBoard(final ColorScheme colorPreset) {
        try {
            serverService.removeColorPresetFromBoard(getCurrentBoard(), colorPreset);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception",
                    "The color preset couldn't be removed from the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a color preset to the current board
     * @param colorPreset Color preset to add
     */
    public void addColorPresetToCurrentBoard(final ColorScheme colorPreset) {
        try {
            serverService.addColorPresetToBoard(getCurrentBoard(), colorPreset);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception",
                    "The tag couldn't be added to the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Edits one of the existing color presets
     * @param payload Color preset
     */
    public void updateEditColorPreset(final ColorScheme payload) {
        boardModel.getCurrentBoard().updateColorScheme(payload);
        if (mainCtrl.getColorPresetsOverviewModal() != null) mainCtrl.getColorPresetsOverviewModal().refresh();
        mainCtrl.refreshOverview();
    }

    /**
     * Adds a color preset to the current board
     * @param payload Color preset
     */
    public void updateAddColorPresetToBoard(final ColorScheme payload) {
        boardModel.getCurrentBoard().addColorPreset(payload);
        if (mainCtrl.getColorPresetsOverviewModal() != null) mainCtrl.getColorPresetsOverviewModal().refresh();
        mainCtrl.refreshOverview();
    }

    /**
     * Removes a color preset from the current board
     * @param payload Color preset
     */
    public void updateRemoveColorPresetFromBoard(final ColorScheme payload) {
        boardModel.getCurrentBoard().deleteColorPreset(payload);
        if (mainCtrl.getColorPresetsOverviewModal() != null) mainCtrl.getColorPresetsOverviewModal().refresh();
        mainCtrl.refreshOverview();
    }

    /**
     * Sets the default column color preset for the current board
     * @param colorPreset Color preset to set as default
     */
    public void setDefaultColorPresetColumn(final ColorScheme colorPreset) {
        try {
            serverService.setDefaultColorPresetColumn(getCurrentBoard(), colorPreset);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception",
                    "The color preset couldn't be set as default on the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the default board color preset for the current board
     * @param colorPreset Color preset to set as default
     */
    public void setDefaultColorPresetBoard(final ColorScheme colorPreset) {
        try {
            serverService.setDefaultColorPresetBoard(getCurrentBoard(), colorPreset);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception",
                    "The color preset couldn't be set as default on the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the default card color preset for the current board
     * @param colorPreset Color preset to set as default
     */
    public void setDefaultColorPresetCard(final ColorScheme colorPreset) {
        try {
            serverService.setDefaultColorPresetCard(getCurrentBoard(), colorPreset);
        } catch (ServerException e) {
            final InfoModal errorModal = new InfoModal(this, "Server Exception",
                    "The color preset couldn't be set as default on the Server.", mainCtrl.getCurrentScene());
            errorModal.showModal();
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the default column color preset for the current board
     * @param payload Color preset
     */
    public void updateDefaultColorPresetColumn(final ColorScheme payload) {
        boardModel.getCurrentBoard().setColumnColorScheme(payload);
        if (mainCtrl.getBoardSettingsModal() != null) mainCtrl.getBoardSettingsModal().refresh();
        mainCtrl.refreshOverview();
    }

    /**
     * Updates the default board color preset for the current board
     * @param payload Color preset
     */
    public void updateDefaultColorPresetBoard(final ColorScheme payload) {
        boardModel.getCurrentBoard().setBoardColorScheme(payload);
        if (mainCtrl.getBoardSettingsModal() != null) mainCtrl.getBoardSettingsModal().refresh();
        mainCtrl.refreshOverview();
    }

    /**
     * Updates the default card color preset for the current board
     * @param payload Color preset
     */
    public void updateDefaultColorPresetCard(final ColorScheme payload) {
        boardModel.getCurrentBoard().setCardColorScheme(payload);
        if (mainCtrl.getBoardSettingsModal() != null) mainCtrl.getBoardSettingsModal().refresh();
        mainCtrl.refreshOverview();
    }
}
