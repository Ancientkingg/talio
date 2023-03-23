package client.scenes.components;

import client.Main;
import client.exceptions.BoardChangeException;
import client.models.BoardModel;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class CardComponent extends GridPane {
    private final BoardModel boardModel;
    private final Card card;
    private final ColumnComponent columnParent;

    @FXML
    private TextArea cardText;

    @FXML
    private Button deleteCardButton;

    /**
     * Constructor for CardComponent
     * @param boardModel BoardModel instance
     * @param card Card instance
     * @param columnParent ColumnComponent instance
     */
    public CardComponent(final BoardModel boardModel, final Card card, final ColumnComponent columnParent) {
        this.boardModel = boardModel;
        this.card = card;
        this.columnParent = columnParent;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/client/scenes/components/Card.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cardText.setText(card.getTitle());
        cardText.setWrapText(true);

        deleteCardButton.setOnAction(e -> {
            try {
                this.delete();
                columnParent.deleteCard(this);
            } catch (BoardChangeException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Deletes the card
     * @throws BoardChangeException If the card could not be deleted
     */
    public void delete() throws BoardChangeException {
        boardModel.removeCard(card, columnParent.getColumn());
    }


}
