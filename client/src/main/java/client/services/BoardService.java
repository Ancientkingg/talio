package client.services;

import client.exceptions.BoardChangeException;
import client.models.BoardModel;
import commons.Board;
import commons.Card;
import commons.Column;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
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
        this.startSocket();
    }

    private void startSocket() {
        serverService.startSocket();
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
        Board serverBoard = serverService.addBoard(board);
        boardModel.addBoard(serverBoard);
        return serverBoard;
    }

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

        this.boardModel.setBoardList(boards);


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

}
