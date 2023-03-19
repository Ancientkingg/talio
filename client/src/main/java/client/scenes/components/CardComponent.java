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

    public CardComponent(BoardModel boardModel, Card card, ColumnComponent columnParent) {
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

    }

    public void delete() throws BoardChangeException {
        boardModel.removeCard(card, columnParent.getColumn());
    }


}
