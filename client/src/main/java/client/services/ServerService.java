package client.services;

import client.utils.SessionHandler;
import client.utils.SocketThread;
import commons.Board;
import commons.Card;
import commons.Column;
import commons.DTOs.CardDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import lombok.Getter;
import jakarta.ws.rs.core.GenericType;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerService {

    @Getter
    private URI serverIP;

    private Logger logger = LogManager.getLogger(ServerService.class);

    private SessionHandler sessionHandler;

    /**
     * Initializes client socket in a thread at the given serverIP
     * @param boardService BoardService that is passed on to SessionHandler through Socket
     */
    public void startSocket(final BoardService boardService) {
        final SocketThread socketThread = new SocketThread(this, serverIP, boardService);
        final Thread thread = new Thread(socketThread);
        thread.start();
    }

    /**
     * The handler passes itself to the serverService in order to subscribe client socket to boards
     * upon getBoard() and addBoard()
     * @param sessionHandler SessionHandler access to session
     */
    public void setHandler(final SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

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
    public Board getBoard(final String joinKey) {
        try (Client client = ClientBuilder.newClient()) {
            final Board board = client.target(serverIP)
                    .path("/boards")
                    .path("/get")
                    .path(joinKey)
                    .request(APPLICATION_JSON)
                    .get(Board.class);
            logger.info("Board request sent to server: " + joinKey);
            sessionHandler.subscribeToBoard(joinKey);
            return board;
        }
    }

    /**
     * Gets multiple boards by join-keys
     * @param joinKeys the join-keys used to identify the boards
     * @return the boards that were retrieved
     */
    public List<Board> getAllBoards(final List<String> joinKeys) {
        try (Client client = ClientBuilder.newClient()) {
            final List<Board> boards = client.target(serverIP)
                    .path("/boards")
                    .path("/getAll")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(joinKeys, APPLICATION_JSON), new GenericType<>() { });
            logger.info("Board request sent to server: " + joinKeys);
            return boards;
        }
    }

    /**
     * Creates a board on the server
     * @param board the board to be added/created
     * @return the board that was created by the server (with id)
     */
    public Board addBoard(final Board board) {
        try (Client client = ClientBuilder.newClient()) {
            final Board addedBoard =  client.target(serverIP)
                    .path("/boards")
                    .path("/create")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(board, APPLICATION_JSON), Board.class);
            logger.info("Created board sent to server: " + board.getJoinKey());
            sessionHandler.subscribeToBoard(addedBoard.getJoinKey());
            return addedBoard;
        }
    }

    /**
     * Creates a column on the server
     * @param board the board to add the column to
     * @param column the column to be added/created
     * @return the column that was created by the server (with id)
     */
    public Column addColumn(final Board board, final Column column) {
        try (Client client = ClientBuilder.newClient()) {
            final Column addedColumn = client.target(serverIP)
                    .path("/columns")
                    .path("/create")
                    .path(board.getJoinKey())
                    .path(column.getHeading())
                    .queryParam("index", column.getIndex())
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(board.getPassword(), APPLICATION_JSON), Column.class);
            logger.info("Added column sent to server: " + column.getHeading());
            return addedColumn;
        }
    }

    /**
     * Removes a column from the server
     * @param board the board to remove the column from
     * @param column the column to be removed
     * @return the column that was removed by the server
     */
    public Column removeColumn(final Board board, final Column column) {
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
    }

    /**
     * Creates a card on the server
     * @param board the board to add the card to
     * @param column the column to add the card to
     * @param card the column to be added/created
     * @return the card that was created by the server (with id)
     */
    public Card addCard(final Board board, final Column column, final Card card) {
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
    }

    /**
     * Removes a card from the server
     * @param board the board to remove the card from
     * @param column the column to remove the card from
     * @param card the card to be removed
     * @return The card that was removed by the server
     */
    public Card removeCard(final Board board, final Column column, final Card card) {
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
    }

    /**
     * Updates the position of a card by posting a request to repositionCard endpoint
     *
     * @param board             current board in which card is being moved
     * @param column            column containing the card
     * @param destinationColumn column to which card is being moved
     * @param card              card to be moved
     * @param newPosition       new index of the card
     * @return Card being moved ?? the method in the server returns the column containing the card which has been updated
     * but compiler was complaining when I made return type of this method Column.
     */
    public Card updatePosition(final Board board, final Column column, final Column destinationColumn, final Card card, final int newPosition) {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(serverIP)
                    .path("/cards")
                    .path("/updatePosition")
                    .path(board.getJoinKey())
                    .path(String.valueOf(column.getId()))
                    .path(String.valueOf(destinationColumn.getId()))
                    .path(String.valueOf(newPosition))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(new CardDTO(card, board.getPassword()), APPLICATION_JSON), Card.class);
        }
    }

    /**
     * Updates the contents of a card by posting a request to editCard endpoint on server
     * @param board current board containing the card
     * @param column column containing the card
     * @param card card to be updated
     * @return Updated card
     * */
    public Card update(final Board board, final Column column, final Card card) {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(serverIP)
                    .path("/cards")
                    .path("/update")
                    .path(board.getJoinKey())
                    .path(String.valueOf(column.getId()))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(new CardDTO(card, board.getPassword()), APPLICATION_JSON), Card.class);
        }
    }
}
