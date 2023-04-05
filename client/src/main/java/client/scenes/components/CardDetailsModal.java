package client.scenes.components;

import client.Main;
import client.services.BoardService;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.io.IOException;

public class CardDetailsModal extends Modal {

    private final Card card;

    @FXML
    private Text cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private Button editDescriptionButton;


    public CardDetailsModal(final BoardService boardService, final Scene parentScene, final Card card) {
        super(boardService, parentScene);
        this.card = card;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/CardDetailsModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        editDescriptionButton.setOnAction(e -> cardDescription.setDisable(false)); // Temporarily enable editing of card text

        cardDescription.setOnKeyReleased(e -> { // Disable editing of card text when enter is pressed
            if (e.getCode() == KeyCode.ENTER) {
                cardDescription.setDisable(true);
                card.setDescription(cardDescription.getText());
                refresh();
            }
        });

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                card.setDescription(cardDescription.getText());
                refresh();
                cardDescription.setDisable(true);
            }
        });

        cardDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            card.setDescription(cardDescription.getText());
            refresh();
        });
        
        cardDescription.setDisable(true);
    }

    @FXML
    public void initialize() {
        super.initialize();
        this.cardTitle.setText(card.getTitle());
        this.cardDescription.setText(card.getDescription());
    }

    public void refresh() {
        cardDescription.setText(card.getDescription());
    }







}
