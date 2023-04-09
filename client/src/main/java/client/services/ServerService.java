package client.services;

import client.exceptions.ServerException;
import client.utils.SessionHandler;
import client.utils.SocketThread;
import commons.*;
import commons.DTOs.CardDTO;
import commons.DTOs.SubTaskDTO;
import commons.DTOs.TagDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import jakarta.ws.rs.core.GenericType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Singleton
public class ServerService {

    @Getter
    private URI serverIP;

    private Logger logger = LogManager.getLogger(ServerService.class);

    private SessionHandler sessionHandler;
    private StompSession session;

    private SocketThread socketThread;

    /**
     * Initializes client socket in a thread at the given serverIP
     * @param boardService BoardService that is passed on to SessionHandler through Socket
     */
    public void startSocket(final BoardService boardService) {
        this.socketThread = new SocketThread(this, serverIP, boardService);
        final Thread thread = new Thread(socketThread);
        thread.start();
    }

    /**
     * Stops the client socket
     */
    public void stopSocket() {
        this.socketThread.stop();
    }

    /**
     * The handler passes itself to the serverService in order to subscribe client socket to boards
     * upon getBoard() and addBoard()
     * @param sessionHandler SessionHandler access to session
     */
    public void setHandler(final SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    public void setSession(final StompSession session) { this.session = session; }

    /**
     * Sets the IP of the server to interact with
     * @param ip the ip of the server
     * @throws IllegalArgumentException if the ip is not a valid URI
     */
    public void setServerIP(final String ip) throws IllegalArgumentException {
        this.serverIP = URI.create(ip);
        logger.info("Set IP to: " + serverIP);
    }

    /**
     * Gets a board by join-key
     * @param joinKey the join-key used to identify the board
     * @return the board that was retrieved
     */
    public Board getBoard(final String joinKey) throws ServerException {
        try (Client client = ClientBuilder.newClient()) {
            final Board board = client.target(serverIP)
                    .path("/boards")
                    .path("/get")
                    .path(joinKey)
                    .request(APPLICATION_JSON)
                    .get(Board.class);
            logger.info("Board request sent to server: " + joinKey);
            return board;
        }
        catch (ResponseStatusException e) {
            throw new ServerException("The Board couldn't be retrieved from the Server: \n" + getServerIP());
        }
    }

    /**
     * Gets multiple boards by join-keys
     * @param joinKeys the join-keys used to identify the boards
     * @return the boards that were retrieved
     */
    public List<Board> getAllBoards(final List<String> joinKeys) throws ServerException {
        try (Client client = ClientBuilder.newClient()) {
            final List<Board> boards = client.target(serverIP)
                    .path("/boards")
                    .path("/getAll")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(joinKeys, APPLICATION_JSON), new GenericType<>() { });
            logger.info("Board request sent to server: " + joinKeys);
            return boards;
        }
        catch (ResponseStatusException e) {
            throw new ServerException("The Boards couldn't be retrieved from the Server: \n" + getServerIP());
        }
    }

    /**
     * Creates a board on the server
     * @param board the board to be added/created
     * @return the board that was created by the server (with id)
     */
    public Board addBoard(final Board board) throws ServerException {
        try (Client client = ClientBuilder.newClient()) {
            final Board addedBoard =  client.target(serverIP)
                    .path("/boards")
                    .path("/create")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(board, APPLICATION_JSON), Board.class);
            logger.info("Created board sent to server: " + board.getJoinKey());
            return addedBoard;
        }
        catch (ResponseStatusException e) {
            throw new ServerException("The Board couldn't be added to the Server: \n" + getServerIP());
        }
    }

    /**
     * Creates a column on the server
     * @param board the board to add the column to
     * @param column the column to be added/created
     * @return the column that was created by the server (with id)
     */
    public Column addColumn(final Board board, final Column column) throws ServerException {
        try (Client client = ClientBuilder.newClient()) {
            final Column addedColumn = client.target(serverIP)
                    .path("/columns")
                    .path("/create")
                    .path(board.getJoinKey())
                    .path(column.getHeading())
                    .path(Long.toString(column.getId()))
                    .queryParam("index", column.getIndex())
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(board.getPassword(), APPLICATION_JSON), Column.class);
            logger.info("Added column sent to server: " + column.getHeading());
            return addedColumn;
        }
        catch (RuntimeException e) {
            throw new ServerException("The column couldn't be added to the server.");
        }
    }

    /**
     * Removes a column from the server
     * @param board the board to remove the column from
     * @param column the column to be removed
     * @return the column that was removed by the server
     */
    public Column removeColumn(final Board board, final Column column) throws ServerException {
        try (Client client = ClientBuilder.newClient()) {
            final Column removedColumn = client.target(serverIP)
                    .path("/columns")
                    .path("/remove")
                    .path(board.getJoinKey())
                    .path(String.valueOf(column.getId()))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(board.getPassword(), APPLICATION_JSON), Column.class);
            logger.info("Removed column sent to server: " + column.getHeading());
            return removedColumn;
        }
        catch (RuntimeException e) {
            throw new ServerException("The Column couldn't be removed from the Server: \n" + getServerIP());
        }
    }

    /**
     * Creates a card on the server
     * @param board the board to add the card to
     * @param column the column to add the card to
     * @param card the column to be added/created
     * @return the card that was created by the server (with id)
     */
    public Card addCard(final Board board, final Column column, final Card card) throws ServerException {
        try (Client client = ClientBuilder.newClient()) {
            final Card addedCard = client.target(serverIP)
                    .path("/cards")
                    .path("/add")
                    .path(board.getJoinKey())
                    .path(String.valueOf(column.getId()))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(new CardDTO(card, board.getPassword()), APPLICATION_JSON), Card.class);
            logger.info("Added card sent to server");
            return addedCard;
        }
        catch (RuntimeException e) {
            throw new ServerException("The Card couldn't be added to the Server: \n" + getServerIP());
        }
    }

    /**
     * Removes a card from the server
     * @param board the board to remove the card from
     * @param column the column to remove the card from
     * @param card the card to be removed
     * @return The card that was removed by the server
     */
    public Card removeCard(final Board board, final Column column, final Card card) throws ServerException {
        try (Client client = ClientBuilder.newClient()) {
            final Card removedCard = client.target(serverIP)
                    .path("/cards")
                    .path("/remove")
                    .path(board.getJoinKey())
                    .path(String.valueOf(column.getId()))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(new CardDTO(card, board.getPassword()), APPLICATION_JSON), Card.class);
            logger.info("Removed card sent to server");
            return removedCard;
        }
        catch (RuntimeException e) {
            throw new ServerException("The Card couldn't be removed from the Server: \n" + getServerIP());
        }
    }

    /**
     * Adds tag to board
     * @param board Board for joinKey
     * @param tag Tag to add
     * @return Tag from server
     */
    public Tag addTagToBoard(final Board board, final Tag tag) {
        try (Client client = ClientBuilder.newClient()) {
            final Tag addedTag = client.target(serverIP)
                .path("/tags")
                .path("/add")
                .path(board.getJoinKey())
                .request(APPLICATION_JSON)
                .post(Entity.entity(new TagDTO(tag, board.getPassword()), APPLICATION_JSON), Tag.class);
            logger.info("Added tag to board sent to server");
            return addedTag;
        }
    }

    /**
     * Removes tag from board
     * @param board Board for joinKey
     * @param tag Tag to remove
     * @return Tag from server
     */
    public Tag removeTagFromBoard(final Board board, final Tag tag) {
        try (Client client = ClientBuilder.newClient()) {
            final Tag addedTag = client.target(serverIP)
                .path("/tags")
                .path("/remove")
                .path(board.getJoinKey())
                .request(APPLICATION_JSON)
                .post(Entity.entity(new TagDTO(tag, board.getPassword()), APPLICATION_JSON), Tag.class);
            logger.info("Removed tag from board sent to server");
            return addedTag;
        }
    }

    /**
     * Edits tag
     * @param board Board for joinKey
     * @param tag Tag to edit
     */
    public void editTag(final Board board, final Tag tag) {
        session.send("/app/tags/edit/" +
                board.getJoinKey(),
                new TagDTO(tag, board.getPassword()));
        logger.info("Edited tag sent to server");
    }

    /**
     * Adds tag to card
     * @param board Board for joinKey
     * @param tag Tag to add
     * @param card Card added to
     * @return Tag from server
     */
    public Tag addTagToCard(final Board board, final Card card, final Tag tag) {
        try (Client client = ClientBuilder.newClient()) {
            final Tag addedTag = client.target(serverIP)
                .path("/tags")
                .path("/addToCard")
                .path(board.getJoinKey())
                .path(Long.toString(card.getId()))
                .request(APPLICATION_JSON)
                .post(Entity.entity(new TagDTO(tag, board.getPassword()), APPLICATION_JSON), Tag.class);
            logger.info("Added tag to card sent to server");
            return addedTag;
        }
    }

    /**
     * Removed tag from card
     * @param board Board for joinKey
     * @param tag Tag to remove
     * @param card Card removed from
     * @return Tag from server
     */
    public Tag removeTagFromCard(final Board board, final Card card, final Tag tag) {
        try (Client client = ClientBuilder.newClient()) {
            final Tag addedTag = client.target(serverIP)
                .path("/tags")
                .path("/removeFromCard")
                .path(board.getJoinKey())
                .path(Long.toString(card.getId()))
                .request(APPLICATION_JSON)
                .post(Entity.entity(new TagDTO(tag, board.getPassword()), APPLICATION_JSON), Tag.class);
            logger.info("Removed tag from card sent to server");
            return addedTag;
        }
    }

    /**
     * Updates the position of a card by posting a request to repositionCard endpoint
     *
     * @param board             current board in which card is being moved
     * @param column            column containing the card
     * @param destinationColumn column to which card is being moved
     * @param card              card to be moved
     * @param newPosition       new index of the card
     */
    public void repositionCard(final Board board, final Column column, final Column destinationColumn,
                               final Card card, final int newPosition) throws ServerException
    {
        try {
            session.send("/app/cards/reposition/" +
                            board.getJoinKey() + "/" +
                            column.getId() + "/" +
                            destinationColumn.getId() + "/" +
                            newPosition,
                    new CardDTO(card, board.getPassword()));
            logger.info("Repositioned card sent to server");
        }
        catch (RuntimeException e) {
            throw new ServerException("The Card couldn't be repositioned on the Server: \n" + getServerIP());
        }
    }

    /**
     * Edits the contents of a card by posting a request to editCard endpoint on server
     * @param board Board for joinKey
     * @param card Card to edit
     * @param column Column card is in
     */
    public void editCard(final Board board, final Card card, final Column column) throws ServerException {
        try {
            session.send("/app/cards/edit/" +
                            board.getJoinKey() + "/" +
                            column.getId(),
                    new CardDTO(card, board.getPassword()));
            logger.info("Edited card sent to server");
        }
        catch (RuntimeException e) {
            throw new ServerException("The Card couldn't be edited on the Server: \n" + getServerIP());
        }
    }

    /**
     * Renames column by posting a request to renameColumn endpoint on server
     * @param board Board for join key
     * @param column Column to rename
     * @param newName New name of column
     */
    public void renameColumn(final Board board, final Column column, final String newName) {
        try {
            session.send("/app/columns/rename/" +
                            board.getJoinKey() + "/" +
                            column.getId() + "/" +
                            newName,
                    board.getPassword());
            logger.info("Renamed column sent to server");
        }
        catch (RuntimeException e) {
            throw new ServerException("The Column couldn't be renamed on the Server: \n" + getServerIP());
        }
    }

    /**
     * Subscribes to loaded board
     * @param joinKey String key to board
     */
    public void subscribeToBoard(final String joinKey) {
        sessionHandler.subscribeToBoard(joinKey);
    }

    /**
     * Deletes a board server-side
     * @param board Board to delete
     * @return True if successful
     */
    public boolean deleteBoard(final Board board) {
        try (Client client = ClientBuilder.newClient()) {
            final Response response = client.target(serverIP)
                .path("/boards")
                .path("/delete")
                .path(board.getJoinKey())
                .request(APPLICATION_JSON)
                .delete();
            logger.info("Requested to delete board with join-key: " + board.getJoinKey());
            return response.getStatus() == 200;
        }
    }

    /**
     * Verifies password provided by user for switching to admin mode
     * @param adminPassword Password provided by user
     * @return correct/incorrect
     */
    public boolean verifyAdminPassword(final String adminPassword) {
        try (Client client = ClientBuilder.newClient()) {
            final Boolean isValid = client.target(serverIP)
                    .path("admin")
                    .path("verify")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(adminPassword, APPLICATION_JSON), Boolean.class);
            logger.info("Attempting to switch to god mode...\nPassword is valid ? " + isValid);
            return isValid != null && isValid;
        }
    }


    /**
     * Gets all boards from the server
     * @return the boards that were retrieved
     */
    public List<Board> adminGetAllBoards() {
        try (Client client = ClientBuilder.newClient()) {
            final List<Board> boards = client.target(serverIP)
                    .path("/admin")
                    .path("/getAllBoards")
                    .request(APPLICATION_JSON)
                    .get(new GenericType<>() { });
            logger.info("(admin) Sending request to server to get all boards");
            return boards;
        }
    }

    /**
     * Checks if the server is responding
     */
    public void checkConnection() {
        try (Client client = ClientBuilder.newClient()) {
            final Response response = client.target(serverIP)
                    .path("/checkConnection")
                    .request(APPLICATION_JSON)
                    .get();
            logger.info("Checking connection to server");
            if (response.getStatus() != 200) {
                throw new ServerException("The server is not responding");
            }
        }
    }

    /**
     * Gets all users from the server using long polling
     * @param boards List of boards to check
     *
     * @return List of boards that are still active
     */
    public List<String> getBoardsStatus(final List<String> boards) {
        try (Client client = ClientBuilder.newClient()) {
            final HashMap<String, Boolean> existingBoards = client.target(serverIP)
                    .path("/home")
                    .path("/getBoardsStatus")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(boards, APPLICATION_JSON), new GenericType<>() { });
            final List<String> activeBoards = new ArrayList<>();
            for (final String board : boards) {
                if (existingBoards.get(board)) {
                    activeBoards.add(board);
                }
            }
            logger.info("(long polling) Sending request to server to get status of boards");
            return activeBoards;
        }
    }

    /**
     * Adds subtask to card
     * @param currentBoard board containing card to which subtask is being added
     * @param card card to which subtask is being added
     * @param description description of subtask
     * @return added subtask
     */
    public SubTask addSubTask(final Board currentBoard, final Card card, final String description) {
        try (Client client = ClientBuilder.newClient()) {
            final SubTask subTask = client.target(serverIP)
                    .path("/subtasks")
                    .path("/add")
                    .path(currentBoard.getJoinKey())
                    .queryParam("cardId", Long.toString(card.getId()))
                    .queryParam("description", description)
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(currentBoard.getPassword(), APPLICATION_JSON), SubTask.class);
            logger.info("Add SubTask to card sent to server");
            return subTask;
        }
    }

    /**
     * removes subtask from card
     * @param currentBoard board containing card from which subtask is being removed
     * @param card card from which subtask is being removed
     * @param subTask subtask being removed
     * @return removed subtask
     */
    public SubTask removeSubTask(final Board currentBoard, final Card card, final SubTask subTask) {
        try (Client client = ClientBuilder.newClient()) {
            final SubTask returnedSubTask = client.target(serverIP)
                    .path("/subtasks")
                    .path("/remove")
                    .path(currentBoard.getJoinKey())
                    .queryParam("subTaskDTO", new SubTaskDTO(subTask, card.getId()))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(currentBoard.getPassword(), APPLICATION_JSON), SubTask.class);
            logger.info("Remove SubTask from card sent to server");
            return returnedSubTask;
        }
    }

    /**
     * Toggles state of subtask (done / not done)
     * @param currentBoard board containing card whose subtask is to be toggled
     * @param card card whose subtask is being toggled
     * @param subTask subtask being toggled
     * @return toggled subtask
     */
    public SubTask toggleSubTask(final Board currentBoard, final Card card, final SubTask subTask) {
        try (Client client = ClientBuilder.newClient()) {
            final SubTask resultSubTask = client.target(serverIP)
                    .path("/subtasks")
                    .path("/toggle")
                    .path(currentBoard.getJoinKey())
                    .queryParam("subTaskDTO", new SubTaskDTO(subTask, card.getId()))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(currentBoard.getPassword(), APPLICATION_JSON), SubTask.class);
            logger.info("Toggle SubTask sent to server");
            return resultSubTask;
        }
    }

    /**
     * Moves subtask within card
     * @param currentBoard current board
     * @param card containing the subtask
     * @param subTask to be moved
     * @param index new index of subtask
     * @return moved subtask
     */
    public SubTask moveSubTask(final Board currentBoard, final Card card, final SubTask subTask, final int index) {
        try (Client client = ClientBuilder.newClient()) {
            final SubTask resultSubTask = client.target(serverIP)
                    .path("/subtasks")
                    .path("/move")
                    .path(currentBoard.getJoinKey())
                    .queryParam("index", index)
                    .queryParam("subTaskDTO", new SubTaskDTO(subTask, card.getId()))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(currentBoard.getPassword(), APPLICATION_JSON), SubTask.class);
            logger.info("Move SubTask sent to server");
            return resultSubTask;
        }
    }
}
