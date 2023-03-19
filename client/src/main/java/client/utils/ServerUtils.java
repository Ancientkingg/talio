package client.utils;

import commons.Board;
import commons.Card;
import commons.Column;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;


import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * Gets all boards
     * @return a list of all boards
     */
    public List<Board> getBoards() {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(SERVER)
                    .path("/boards")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<>() {
                    });
        }
    }

    /**
     * Returns list of all columns
     * @return a list of columns
     */
    public List<Column> getColumns() {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(SERVER)
                    .path("/columns")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<>() {
                    });
        }
    }

    /**
     * Gets all cards
     * @return a list of cards
     */
    public List<Card> getCards() {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(SERVER)
                    .path("/cards")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<>() {
                    });
        }
    }

    /**
     * Adds a board to database
     * @param board the board to be added/ created
     * @return the board that was added
     */
    public Board addBoard(final Board board) {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(SERVER)
                    .path("/boards") //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .post(Entity.entity(board, APPLICATION_JSON), Board.class);
        }
    }

    /**
     * Adds a column to database
     * @param column the column to be added/ created
     * @return the column that was added
     */
    public Column addColumn(final Column column) {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(SERVER)
                    .path("/columns") //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .post(Entity.entity(column, APPLICATION_JSON), Column.class);
        }
    }

    /**
     * Adds a card to database
     * @param card the column to be added/ created
     * @return the card that was added
     */
    public Card addCard(final Card card) {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(SERVER)
                    .path("/cards") //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .post(Entity.entity(card, APPLICATION_JSON), Card.class);
        }
    }

}
