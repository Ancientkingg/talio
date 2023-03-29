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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerService {

    private URI serverIP;

    private Logger logger = LogManager.getLogger(ServerService.class);

    private SessionHandler sessionHandler;

    /**
     * Initializes client socket in a thread at the given serverIP
     */
    public void startSocket() {
        final SocketThread socketThread = new SocketThread(this, serverIP);
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
            sessionHandler.subscribeToBoard(joinKey);
            return board;
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
            return client.target(serverIP)
                    .path("/columns")
                    .path("/create")
                    .path(board.getJoinKey())
                    .path(column.getHeading())
                    .queryParam("index", column.getIndex())
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(board.getPassword(), APPLICATION_JSON), Column.class);
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
            return client.target(serverIP)
                    .path("/columns")
                    .path("/remove")
                    .path(board.getJoinKey())
                    .path(column.getHeading())
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(board.getPassword(), APPLICATION_JSON), Column.class);
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
            return client.target(serverIP)
                    .path("/cards")
                    .path("/add")
                    .path(board.getJoinKey())
                    .path(String.valueOf(column.getId()))
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(new CardDTO(card, board.getPassword()), APPLICATION_JSON), Card.class);
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
            return client.target(serverIP)
                    .path("/cards")
                    .path("/remove")
                    .path(board.getJoinKey())
                    .path(column.getHeading())
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(new CardDTO(card, board.getPassword()), APPLICATION_JSON), Card.class);
        }
    }

}
